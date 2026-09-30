package com.spring.flix.core.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(LocalDateTime timestamp, int status, String mensagem, List<String> erros) {
}
