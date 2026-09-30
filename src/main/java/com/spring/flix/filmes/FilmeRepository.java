package com.spring.flix.filmes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmeRepository extends JpaRepository<Filme, Long> {

    List<Filme> findByTituloContainingIgnoreCaseOrderByTituloAsc(String titulo);

    boolean existsByCategoriasId(Long categoriaId);
}
