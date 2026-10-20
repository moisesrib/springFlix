package com.spring.flix.auth.service;

import com.spring.flix.auth.dto.LoginRequest;
import com.spring.flix.auth.dto.RegistroRequest;
import com.spring.flix.auth.dto.TokenResponse;
import com.spring.flix.core.security.TokenService;
import com.spring.flix.usuarios.dto.UsuarioRequest;
import com.spring.flix.usuarios.dto.UsuarioResponse;
import com.spring.flix.usuarios.entity.Role;
import com.spring.flix.usuarios.entity.Usuario;
import com.spring.flix.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        Usuario usuario = (Usuario) authentication.getPrincipal();
        log.info("Login realizado por {}", usuario.getEmail());
        return new TokenResponse(tokenService.gerarToken(usuario), "Bearer");
    }

    public UsuarioResponse registrar(RegistroRequest request) {
        return usuarioService.criar(new UsuarioRequest(request.nome(), request.email(), request.senha(), Role.USER));
    }
}
