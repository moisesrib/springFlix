package com.spring.flix.filmes;

import com.spring.flix.categorias.Categoria;
import com.spring.flix.categorias.CategoriaRepository;
import com.spring.flix.core.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FilmeService {

    private final FilmeRepository filmeRepository;
    private final CategoriaRepository categoriaRepository;

    public List<FilmeResponse> listar(String titulo) {
        return filmeRepository.findByTituloContainingIgnoreCaseOrderByTituloAsc(titulo).stream()
                .map(FilmeResponse::from)
                .toList();
    }

    public FilmeResponse buscar(Long id) {
        return FilmeResponse.from(buscarFilme(id));
    }

    public FilmeResponse criar(FilmeRequest request) {
        Filme filme = new Filme();
        preencher(filme, request);
        filmeRepository.save(filme);
        log.info("Filme {} criado", filme.getTitulo());
        return FilmeResponse.from(filme);
    }

    public FilmeResponse atualizar(Long id, FilmeRequest request) {
        Filme filme = buscarFilme(id);
        preencher(filme, request);
        log.info("Filme {} atualizado", id);
        return FilmeResponse.from(filme);
    }

    public void excluir(Long id) {
        filmeRepository.delete(buscarFilme(id));
        log.info("Filme {} excluído", id);
    }

    public Filme buscarFilme(Long id) {
        return filmeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Filme não encontrado"));
    }

    private void preencher(Filme filme, FilmeRequest request) {
        List<Categoria> categorias = categoriaRepository.findAllById(request.categoriaIds());
        if (categorias.size() != request.categoriaIds().size()) {
            throw new RecursoNaoEncontradoException("Categoria não encontrada");
        }
        filme.setTitulo(request.titulo());
        filme.setSinopse(request.sinopse());
        filme.setDiretor(request.diretor());
        filme.setAnoLancamento(request.anoLancamento());
        filme.setDuracaoMinutos(request.duracaoMinutos());
        filme.setClassificacaoIndicativa(request.classificacaoIndicativa());
        filme.setCategorias(new HashSet<>(categorias));
    }
}
