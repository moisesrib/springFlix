package com.spring.flix.categorias;

import com.spring.flix.core.exception.RecursoNaoEncontradoException;
import com.spring.flix.core.exception.RegraNegocioException;
import com.spring.flix.filmes.FilmeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final FilmeRepository filmeRepository;

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll(Sort.by("nome")).stream().map(CategoriaResponse::from).toList();
    }

    public CategoriaResponse buscar(Long id) {
        return CategoriaResponse.from(buscarCategoria(id));
    }

    public CategoriaResponse criar(CategoriaRequest request) {
        if (categoriaRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new RegraNegocioException("Categoria já cadastrada");
        }
        Categoria categoria = new Categoria();
        categoria.setNome(request.nome());
        categoriaRepository.save(categoria);
        log.info("Categoria {} criada", categoria.getNome());
        return CategoriaResponse.from(categoria);
    }

    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarCategoria(id);
        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(request.nome(), id)) {
            throw new RegraNegocioException("Categoria já cadastrada");
        }
        categoria.setNome(request.nome());
        log.info("Categoria {} atualizada", id);
        return CategoriaResponse.from(categoria);
    }

    public void excluir(Long id) {
        Categoria categoria = buscarCategoria(id);
        if (filmeRepository.existsByCategoriasId(id)) {
            throw new RegraNegocioException("Categoria possui filmes vinculados e não pode ser excluída");
        }
        categoriaRepository.delete(categoria);
        log.info("Categoria {} excluída", id);
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada"));
    }
}
