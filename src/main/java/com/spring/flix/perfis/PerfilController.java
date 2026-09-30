package com.spring.flix.perfis;

import com.spring.flix.filmes.FilmeResponse;
import com.spring.flix.usuarios.Usuario;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Perfis")
@RestController
@RequestMapping("/perfis")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public List<PerfilResponse> listar(@AuthenticationPrincipal Usuario usuario) {
        return perfilService.listar(usuario);
    }

    @GetMapping("/{id}")
    public PerfilResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return perfilService.buscar(id, usuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilResponse criar(@RequestBody @Valid PerfilRequest request, @AuthenticationPrincipal Usuario usuario) {
        return perfilService.criar(request, usuario);
    }

    @PutMapping("/{id}")
    public PerfilResponse atualizar(@PathVariable Long id, @RequestBody @Valid PerfilRequest request,
                                    @AuthenticationPrincipal Usuario usuario) {
        return perfilService.atualizar(id, request, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        perfilService.excluir(id, usuario);
    }

    @GetMapping("/{id}/minha-lista")
    public List<FilmeResponse> listarMinhaLista(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return perfilService.listarMinhaLista(id, usuario);
    }

    @PostMapping("/{id}/minha-lista/{filmeId}")
    public List<FilmeResponse> adicionarNaLista(@PathVariable Long id, @PathVariable Long filmeId,
                                                @AuthenticationPrincipal Usuario usuario) {
        return perfilService.adicionarNaLista(id, filmeId, usuario);
    }

    @DeleteMapping("/{id}/minha-lista/{filmeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerDaLista(@PathVariable Long id, @PathVariable Long filmeId,
                               @AuthenticationPrincipal Usuario usuario) {
        perfilService.removerDaLista(id, filmeId, usuario);
    }
}
