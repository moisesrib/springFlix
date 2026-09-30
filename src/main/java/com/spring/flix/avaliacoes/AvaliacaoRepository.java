package com.spring.flix.avaliacoes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findAllByOrderByDataAvaliacaoDesc();

    List<Avaliacao> findByFilmeIdOrderByDataAvaliacaoDesc(Long filmeId);

    Optional<Avaliacao> findByPerfilIdAndFilmeId(Long perfilId, Long filmeId);

    Optional<Avaliacao> findByIdAndPerfilUsuarioId(Long id, Long usuarioId);
}
