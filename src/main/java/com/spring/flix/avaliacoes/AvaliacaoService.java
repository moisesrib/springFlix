package com.spring.flix.avaliacoes;

import com.spring.flix.core.exception.RecursoNaoEncontradoException;
import com.spring.flix.core.exception.RegraNegocioException;
import com.spring.flix.filmes.Filme;
import com.spring.flix.filmes.FilmeService;
import com.spring.flix.perfis.Perfil;
import com.spring.flix.perfis.PerfilService;
import com.spring.flix.usuarios.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PerfilService perfilService;
    private final FilmeService filmeService;

    public List<AvaliacaoResponse> listar(Long filmeId) {
        List<Avaliacao> avaliacoes = filmeId == null
                ? avaliacaoRepository.findAllByOrderByDataAvaliacaoDesc()
                : avaliacaoRepository.findByFilmeIdOrderByDataAvaliacaoDesc(filmeId);
        return avaliacoes.stream().map(AvaliacaoResponse::from).toList();
    }

    public AvaliacaoResponse buscar(Long id) {
        return AvaliacaoResponse.from(avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Avaliação não encontrada")));
    }

    public AvaliacaoResponse criar(AvaliacaoRequest request, Usuario usuario) {
        Avaliacao avaliacao = new Avaliacao();
        preencher(avaliacao, request, usuario);
        avaliacaoRepository.save(avaliacao);
        log.info("Avaliação {} criada para o filme {}", avaliacao.getId(), request.filmeId());
        return AvaliacaoResponse.from(avaliacao);
    }

    public AvaliacaoResponse atualizar(Long id, AvaliacaoRequest request, Usuario usuario) {
        Avaliacao avaliacao = buscarDoUsuario(id, usuario);
        preencher(avaliacao, request, usuario);
        log.info("Avaliação {} atualizada", id);
        return AvaliacaoResponse.from(avaliacao);
    }

    public void excluir(Long id, Usuario usuario) {
        avaliacaoRepository.delete(buscarDoUsuario(id, usuario));
        log.info("Avaliação {} excluída", id);
    }

    private Avaliacao buscarDoUsuario(Long id, Usuario usuario) {
        return avaliacaoRepository.findByIdAndPerfilUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Avaliação não encontrada"));
    }

    private void preencher(Avaliacao avaliacao, AvaliacaoRequest request, Usuario usuario) {
        Perfil perfil = perfilService.buscarDoUsuario(request.perfilId(), usuario);
        Filme filme = filmeService.buscarFilme(request.filmeId());
        avaliacaoRepository.findByPerfilIdAndFilmeId(perfil.getId(), filme.getId())
                .filter(existente -> !existente.getId().equals(avaliacao.getId()))
                .ifPresent(existente -> {
                    throw new RegraNegocioException("Este perfil já avaliou este filme");
                });
        avaliacao.setPerfil(perfil);
        avaliacao.setFilme(filme);
        avaliacao.setNota(request.nota());
        avaliacao.setComentario(request.comentario());
        avaliacao.setDataAvaliacao(LocalDateTime.now());
    }
}
