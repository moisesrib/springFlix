package com.spring.flix.perfis.repository;

import com.spring.flix.perfis.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    List<Perfil> findByUsuarioIdOrderByNomeAsc(Long usuarioId);

    Optional<Perfil> findByIdAndUsuarioId(Long id, Long usuarioId);

    long countByUsuarioId(Long usuarioId);
}
