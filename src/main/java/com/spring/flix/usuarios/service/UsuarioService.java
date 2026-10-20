package com.spring.flix.usuarios.service;

import com.spring.flix.core.exception.RecursoNaoEncontradoException;
import com.spring.flix.core.exception.RegraNegocioException;
import com.spring.flix.usuarios.dto.UsuarioRequest;
import com.spring.flix.usuarios.dto.UsuarioResponse;
import com.spring.flix.usuarios.entity.Usuario;
import com.spring.flix.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioResponse::from).toList();
    }

    public UsuarioResponse buscar(Long id) {
        return UsuarioResponse.from(buscarUsuario(id));
    }

    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }
        Usuario usuario = new Usuario();
        preencher(usuario, request);
        usuarioRepository.save(usuario);
        log.info("Usuário {} criado com o perfil de acesso {}", usuario.getEmail(), usuario.getRole());
        return UsuarioResponse.from(usuario);
    }

    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscarUsuario(id);
        if (usuarioRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }
        preencher(usuario, request);
        log.info("Usuário {} atualizado", id);
        return UsuarioResponse.from(usuario);
    }

    public void excluir(Long id) {
        usuarioRepository.delete(buscarUsuario(id));
        log.info("Usuário {} excluído", id);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    private void preencher(Usuario usuario, UsuarioRequest request) {
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(request.role());
    }
}
