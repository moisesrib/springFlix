package com.spring.flix.perfis.service;

import com.spring.flix.core.exception.RecursoNaoEncontradoException;
import com.spring.flix.core.exception.RegraNegocioException;
import com.spring.flix.filmes.dto.FilmeResponse;
import com.spring.flix.filmes.entity.Filme;
import com.spring.flix.filmes.service.FilmeService;
import com.spring.flix.perfis.dto.PerfilRequest;
import com.spring.flix.perfis.dto.PerfilResponse;
import com.spring.flix.perfis.entity.Perfil;
import com.spring.flix.perfis.repository.PerfilRepository;
import com.spring.flix.usuarios.entity.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PerfilService {

    private static final int LIMITE_PERFIS = 5;
    private static final Set<String> CLASSIFICACOES_INFANTIS = Set.of("L", "10");

    private final PerfilRepository perfilRepository;
    private final FilmeService filmeService;

    public List<PerfilResponse> listar(Usuario usuario) {
        return perfilRepository.findByUsuarioIdOrderByNomeAsc(usuario.getId()).stream()
                .map(PerfilResponse::from)
                .toList();
    }

    public PerfilResponse buscar(Long id, Usuario usuario) {
        return PerfilResponse.from(buscarDoUsuario(id, usuario));
    }

    public PerfilResponse criar(PerfilRequest request, Usuario usuario) {
        if (perfilRepository.countByUsuarioId(usuario.getId()) >= LIMITE_PERFIS) {
            throw new RegraNegocioException("Limite de " + LIMITE_PERFIS + " perfis por usuário atingido");
        }
        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);
        perfil.setNome(request.nome());
        perfil.setInfantil(request.infantil());
        perfilRepository.save(perfil);
        log.info("Perfil {} criado para o usuário {}", perfil.getNome(), usuario.getEmail());
        return PerfilResponse.from(perfil);
    }

    public PerfilResponse atualizar(Long id, PerfilRequest request, Usuario usuario) {
        Perfil perfil = buscarDoUsuario(id, usuario);
        perfil.setNome(request.nome());
        perfil.setInfantil(request.infantil());
        log.info("Perfil {} atualizado", id);
        return PerfilResponse.from(perfil);
    }

    public void excluir(Long id, Usuario usuario) {
        perfilRepository.delete(buscarDoUsuario(id, usuario));
        log.info("Perfil {} excluído", id);
    }

    public List<FilmeResponse> listarMinhaLista(Long id, Usuario usuario) {
        return buscarDoUsuario(id, usuario).getMinhaLista().stream()
                .sorted(Comparator.comparing(Filme::getTitulo))
                .map(FilmeResponse::from)
                .toList();
    }

    public List<FilmeResponse> adicionarNaLista(Long id, Long filmeId, Usuario usuario) {
        Perfil perfil = buscarDoUsuario(id, usuario);
        Filme filme = filmeService.buscarFilme(filmeId);
        if (perfil.isInfantil() && !CLASSIFICACOES_INFANTIS.contains(filme.getClassificacaoIndicativa())) {
            throw new RegraNegocioException("Perfil infantil só pode adicionar filmes com classificação L ou 10");
        }
        perfil.getMinhaLista().add(filme);
        log.info("Filme {} adicionado à lista do perfil {}", filmeId, id);
        return listarMinhaLista(id, usuario);
    }

    public void removerDaLista(Long id, Long filmeId, Usuario usuario) {
        Perfil perfil = buscarDoUsuario(id, usuario);
        if (!perfil.getMinhaLista().removeIf(filme -> filme.getId().equals(filmeId))) {
            throw new RecursoNaoEncontradoException("Filme não está na lista do perfil");
        }
        log.info("Filme {} removido da lista do perfil {}", filmeId, id);
    }

    public Perfil buscarDoUsuario(Long id, Usuario usuario) {
        return perfilRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado"));
    }
}
