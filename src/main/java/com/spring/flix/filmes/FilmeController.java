package com.spring.flix.filmes;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

@Tag(name = "Filmes")
@RestController
@RequestMapping("/filmes")
@RequiredArgsConstructor
public class FilmeController {

    private final FilmeService filmeService;

    @GetMapping
    public List<FilmeResponse> listar(@RequestParam(defaultValue = "") String titulo) {
        return filmeService.listar(titulo);
    }

    @GetMapping("/{id}")
    public FilmeResponse buscar(@PathVariable Long id) {
        return filmeService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmeResponse criar(@RequestBody @Valid FilmeRequest request) {
        return filmeService.criar(request);
    }

    @PutMapping("/{id}")
    public FilmeResponse atualizar(@PathVariable Long id, @RequestBody @Valid FilmeRequest request) {
        return filmeService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        filmeService.excluir(id);
    }
}
