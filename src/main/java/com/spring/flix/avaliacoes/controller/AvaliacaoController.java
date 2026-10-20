package com.spring.flix.avaliacoes.controller;

import com.spring.flix.avaliacoes.dto.AvaliacaoRequest;
import com.spring.flix.avaliacoes.dto.AvaliacaoResponse;
import com.spring.flix.avaliacoes.service.AvaliacaoService;
import com.spring.flix.usuarios.entity.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Avaliações")
@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @GetMapping
    public List<AvaliacaoResponse> listar(@RequestParam(required = false) Long filmeId) {
        return avaliacaoService.listar(filmeId);
    }

    @GetMapping("/{id}")
    public AvaliacaoResponse buscar(@PathVariable Long id) {
        return avaliacaoService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoResponse criar(@RequestBody @Valid AvaliacaoRequest request, @AuthenticationPrincipal Usuario usuario) {
        return avaliacaoService.criar(request, usuario);
    }

    @PutMapping("/{id}")
    public AvaliacaoResponse atualizar(@PathVariable Long id, @RequestBody @Valid AvaliacaoRequest request,
                                       @AuthenticationPrincipal Usuario usuario) {
        return avaliacaoService.atualizar(id, request, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        avaliacaoService.excluir(id, usuario);
    }
}
