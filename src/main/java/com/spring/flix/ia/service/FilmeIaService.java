package com.spring.flix.ia.service;

import com.spring.flix.avaliacoes.entity.Avaliacao;
import com.spring.flix.avaliacoes.repository.AvaliacaoRepository;
import com.spring.flix.categorias.entity.Categoria;
import com.spring.flix.core.exception.RegraNegocioException;
import com.spring.flix.filmes.dto.FilmeResponse;
import com.spring.flix.filmes.entity.Filme;
import com.spring.flix.filmes.repository.FilmeRepository;
import com.spring.flix.ia.dto.IaResponse;
import com.spring.flix.perfis.entity.Perfil;
import com.spring.flix.perfis.service.PerfilService;
import com.spring.flix.usuarios.entity.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Sugestão de filmes por perfil e pesquisa em linguagem natural.
 * Usa a IA quando ela está configurada; caso contrário (ou se a IA falhar),
 * resolve com um algoritmo local para a aplicação continuar funcionando.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FilmeIaService {

    private static final int LIMITE_SUGESTOES = 5;
    private static final int LIMITE_PESQUISA = 8;
    private static final Set<String> CLASSIFICACOES_INFANTIS = Set.of("L", "10");
    private static final Pattern IDS = Pattern.compile("IDS:\\s*([\\d,\\s]*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern EXPLICACAO = Pattern.compile("EXPLICA[CÇ][AÃ]O:\\s*(.+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Set<String> PALAVRAS_IGNORADAS = Set.of(
            "filme", "filmes", "com", "sem", "uma", "uns", "umas", "que", "para", "por", "dos", "das",
            "quero", "algum", "alguma", "sobre", "tipo", "assistir", "ver", "mais", "muito", "bem");
    private static final Map<String, String> SINONIMOS = Map.ofEntries(
            Map.entry("brasileiro", "nacional"), Map.entry("brasileira", "nacional"),
            Map.entry("brasil", "nacional"), Map.entry("engracado", "comedia"),
            Map.entry("engracada", "comedia"), Map.entry("desenho", "animacao"),
            Map.entry("romantico", "romance"), Map.entry("romantica", "romance"),
            Map.entry("medo", "terror"), Map.entry("horror", "terror"),
            Map.entry("espaco", "ficcao"), Map.entry("futuro", "ficcao"));

    private final FilmeRepository filmeRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final PerfilService perfilService;
    private final IaService iaService;

    // ------------------------------------------------------------------ sugestão

    public IaResponse sugerir(Long perfilId, Usuario usuario) {
        Perfil perfil = perfilService.buscarDoUsuario(perfilId, usuario);
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByPerfilId(perfilId);

        Set<Long> jaVistos = new LinkedHashSet<>();
        perfil.getMinhaLista().forEach(f -> jaVistos.add(f.getId()));
        avaliacoes.forEach(a -> jaVistos.add(a.getFilme().getId()));

        List<Filme> candidatos = filmeRepository.findAll().stream()
                .filter(f -> !jaVistos.contains(f.getId()))
                .filter(f -> !perfil.isInfantil() || CLASSIFICACOES_INFANTIS.contains(f.getClassificacaoIndicativa()))
                .toList();

        if (candidatos.isEmpty()) {
            return new IaResponse(modo(false), "Este perfil já conhece todos os filmes disponíveis.", List.of());
        }

        Map<String, Object> variaveis = Map.of(
                "perfil", perfil.getNome(),
                "infantil", perfil.isInfantil() ? "É um perfil infantil: sugira apenas filmes livres ou 10 anos." : "",
                "minhaLista", listarOuNenhum(perfil.getMinhaLista().stream().map(Filme::getTitulo).sorted().toList()),
                "avaliacoes", listarOuNenhum(avaliacoes.stream()
                        .map(a -> a.getFilme().getTitulo() + " (nota " + a.getNota() + ")").toList()),
                "catalogo", candidatos.stream().map(this::linhaSugestao).collect(Collectors.joining("\n")));

        Optional<IaResponse> respostaIa = iaService.executar("sugerir-filmes.st", variaveis)
                .flatMap(texto -> interpretar(texto, candidatos, LIMITE_SUGESTOES));
        if (respostaIa.isPresent()) {
            log.info("Sugestões geradas pela IA para o perfil {}", perfilId);
            return respostaIa.get();
        }
        return sugerirLocalmente(perfil, avaliacoes, candidatos);
    }

    private IaResponse sugerirLocalmente(Perfil perfil, List<Avaliacao> avaliacoes, List<Filme> candidatos) {
        // Peso de cada categoria conforme o histórico do perfil
        Map<String, Integer> pesos = new HashMap<>();
        perfil.getMinhaLista().forEach(f -> somarCategorias(pesos, f, 2));
        avaliacoes.forEach(a -> somarCategorias(pesos, a.getFilme(), a.getNota() >= 4 ? a.getNota() : a.getNota() <= 2 ? -2 : 0));

        List<Filme> sugeridos = candidatos.stream()
                .sorted(Comparator.<Filme>comparingInt(f -> pontuarPorCategoria(f, pesos)).reversed()
                        .thenComparing(Filme::getAnoLancamento, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Filme::getTitulo))
                .limit(LIMITE_SUGESTOES)
                .toList();

        List<String> favoritas = pesos.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        String explicacao = favoritas.isEmpty()
                ? "Ainda não conhecemos o gosto deste perfil, então separamos os lançamentos mais recentes do catálogo."
                : "Sugestões baseadas nas categorias que este perfil mais curte: " + String.join(", ", favoritas) + ".";
        return new IaResponse(modo(false), explicacao, sugeridos.stream().map(FilmeResponse::from).toList());
    }

    // ------------------------------------------------------------------ pesquisa

    public IaResponse pesquisar(String pergunta) {
        if (pergunta == null || pergunta.isBlank()) {
            throw new RegraNegocioException("Informe o que deseja pesquisar");
        }
        List<Filme> catalogo = filmeRepository.findAll();

        Map<String, Object> variaveis = Map.of(
                "pergunta", pergunta.trim(),
                "catalogo", catalogo.stream().map(this::linhaPesquisa).collect(Collectors.joining("\n")));

        Optional<IaResponse> respostaIa = iaService.executar("pesquisar-filmes.st", variaveis)
                .flatMap(texto -> interpretar(texto, catalogo, LIMITE_PESQUISA));
        if (respostaIa.isPresent()) {
            log.info("Pesquisa '{}' respondida pela IA", pergunta);
            return respostaIa.get();
        }
        return pesquisarLocalmente(pergunta, catalogo);
    }

    private IaResponse pesquisarLocalmente(String pergunta, List<Filme> catalogo) {
        List<String> termos = Arrays.stream(normalizar(pergunta).split("[^a-z0-9]+"))
                .filter(t -> t.length() >= 3 || t.matches("\\d{4}"))
                .filter(t -> !PALAVRAS_IGNORADAS.contains(t))
                .map(t -> SINONIMOS.getOrDefault(t, t))
                .distinct()
                .toList();

        Map<Filme, Integer> pontuacao = new HashMap<>();
        for (Filme filme : catalogo) {
            int pontos = pontuarPesquisa(filme, termos);
            if (pontos > 0) {
                pontuacao.put(filme, pontos);
            }
        }

        List<FilmeResponse> encontrados = pontuacao.entrySet().stream()
                .sorted(Map.Entry.<Filme, Integer>comparingByValue().reversed()
                        .thenComparing(e -> e.getKey().getTitulo()))
                .limit(LIMITE_PESQUISA)
                .map(e -> FilmeResponse.from(e.getKey()))
                .toList();

        String explicacao = encontrados.isEmpty()
                ? "Nenhum filme combinou com \"" + pergunta.trim() + "\"."
                : "Filmes cujo título, diretor, categoria ou sinopse combinam com \"" + pergunta.trim() + "\".";
        return new IaResponse(modo(false), explicacao, encontrados);
    }

    private int pontuarPesquisa(Filme filme, List<String> termos) {
        String titulo = normalizar(filme.getTitulo());
        String diretor = normalizar(filme.getDiretor());
        String sinopse = normalizar(filme.getSinopse());
        String categorias = normalizar(filme.getCategorias().stream().map(Categoria::getNome).collect(Collectors.joining(" ")));
        String ano = String.valueOf(filme.getAnoLancamento());

        int pontos = 0;
        for (String termo : termos) {
            if (titulo.contains(termo)) pontos += 5;
            if (categorias.contains(termo)) pontos += 4;
            if (diretor.contains(termo)) pontos += 3;
            if (sinopse.contains(termo)) pontos += 1;
            if (ano.equals(termo)) pontos += 3;
        }
        return pontos;
    }

    // ------------------------------------------------------------------ apoio

    /**
     * Lê a resposta no formato "IDS: 1, 2\nEXPLICACAO: ..." e mantém só ids que existem
     * entre os candidatos, para a IA nunca inventar filmes.
     */
    private Optional<IaResponse> interpretar(String texto, List<Filme> candidatos, int limite) {
        Matcher ids = IDS.matcher(texto);
        if (!ids.find()) {
            log.warn("Resposta da IA fora do formato esperado: {}", texto);
            return Optional.empty();
        }
        Map<Long, Filme> porId = candidatos.stream().collect(Collectors.toMap(Filme::getId, Function.identity()));
        List<FilmeResponse> filmes = Arrays.stream(ids.group(1).split("[,\\s]+"))
                .filter(s -> !s.isBlank())
                .map(Long::valueOf)
                .distinct()
                .map(porId::get)
                .filter(f -> f != null)
                .limit(limite)
                .map(FilmeResponse::from)
                .toList();

        Matcher explicacao = EXPLICACAO.matcher(texto);
        String textoExplicacao = explicacao.find() ? explicacao.group(1).trim() : "";
        return Optional.of(new IaResponse(modo(true), textoExplicacao, filmes));
    }

    private void somarCategorias(Map<String, Integer> pesos, Filme filme, int valor) {
        filme.getCategorias().forEach(c -> pesos.merge(c.getNome(), valor, Integer::sum));
    }

    private int pontuarPorCategoria(Filme filme, Map<String, Integer> pesos) {
        return filme.getCategorias().stream().mapToInt(c -> pesos.getOrDefault(c.getNome(), 0)).sum();
    }

    private String linhaSugestao(Filme f) {
        return String.join(" | ", String.valueOf(f.getId()), f.getTitulo(), String.valueOf(f.getAnoLancamento()),
                nomesCategorias(f), f.getClassificacaoIndicativa());
    }

    private String linhaPesquisa(Filme f) {
        return String.join(" | ", String.valueOf(f.getId()), f.getTitulo(), String.valueOf(f.getAnoLancamento()),
                f.getDiretor(), nomesCategorias(f), f.getSinopse());
    }

    private String nomesCategorias(Filme f) {
        return f.getCategorias().stream().map(Categoria::getNome).sorted().collect(Collectors.joining(", "));
    }

    private String listarOuNenhum(List<String> itens) {
        return itens.isEmpty() ? "(nenhum)" : itens.stream().map(i -> "- " + i).collect(Collectors.joining("\n"));
    }

    private String modo(boolean ia) {
        return ia ? "ia" : "simulado";
    }

    private static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}
