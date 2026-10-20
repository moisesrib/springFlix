package com.spring.flix.filmes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record FilmeRequest(
        @NotBlank @Size(max = 150) String titulo,
        @NotBlank @Size(max = 1000) String sinopse,
        @NotBlank @Size(max = 100) String diretor,
        @NotNull @Min(1888) @Max(2100) Integer anoLancamento,
        @NotNull @Positive Integer duracaoMinutos,
        @NotBlank @Pattern(regexp = "L|10|12|14|16|18", message = "deve ser L, 10, 12, 14, 16 ou 18") String classificacaoIndicativa,
        @NotEmpty Set<@NotNull Long> categoriaIds) {
}
