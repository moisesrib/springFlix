package com.spring.flix.ia.controller;

import com.spring.flix.ia.dto.IaResponse;
import com.spring.flix.ia.dto.IaStatusResponse;
import com.spring.flix.ia.service.FilmeIaService;
import com.spring.flix.ia.service.IaService;
import com.spring.flix.usuarios.entity.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "IA")
@RestController
@RequestMapping("/ia")
@RequiredArgsConstructor
public class IaController {

    private final IaService iaService;
    private final FilmeIaService filmeIaService;

    @Operation(summary = "Mostra se a IA está configurada (OPENAI_API_KEY) ou em modo simulado")
    @GetMapping("/status")
    public IaStatusResponse status() {
        return new IaStatusResponse(iaService.habilitada(), iaService.modelo());
    }

    @Operation(summary = "Sugere filmes para um perfil com base na minha lista e nas avaliações")
    @GetMapping("/sugestoes")
    public IaResponse sugestoes(@RequestParam Long perfilId, @AuthenticationPrincipal Usuario usuario) {
        return filmeIaService.sugerir(perfilId, usuario);
    }

    @Operation(summary = "Pesquisa filmes em linguagem natural (ex.: \"filme brasileiro sobre crime\")")
    @GetMapping("/pesquisa")
    public IaResponse pesquisa(@RequestParam String q) {
        return filmeIaService.pesquisar(q);
    }
}
