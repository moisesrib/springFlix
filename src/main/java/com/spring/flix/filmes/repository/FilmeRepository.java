package com.spring.flix.filmes.repository;

import com.spring.flix.filmes.entity.Filme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmeRepository extends JpaRepository<Filme, Long> {

    List<Filme> findByTituloContainingIgnoreCaseOrderByTituloAsc(String titulo);

    boolean existsByCategoriasId(Long categoriaId);
}
