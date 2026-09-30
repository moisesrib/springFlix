CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE perfis (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    infantil BOOLEAN NOT NULL DEFAULT FALSE,
    usuario_id BIGINT NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE
);

CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE filmes (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    sinopse VARCHAR(1000) NOT NULL,
    diretor VARCHAR(100) NOT NULL,
    ano_lancamento INTEGER NOT NULL,
    duracao_minutos INTEGER NOT NULL,
    classificacao_indicativa VARCHAR(2) NOT NULL
);

CREATE TABLE filme_categorias (
    filme_id BIGINT NOT NULL REFERENCES filmes (id) ON DELETE CASCADE,
    categoria_id BIGINT NOT NULL REFERENCES categorias (id) ON DELETE CASCADE,
    PRIMARY KEY (filme_id, categoria_id)
);

CREATE TABLE minha_lista (
    perfil_id BIGINT NOT NULL REFERENCES perfis (id) ON DELETE CASCADE,
    filme_id BIGINT NOT NULL REFERENCES filmes (id) ON DELETE CASCADE,
    PRIMARY KEY (perfil_id, filme_id)
);

CREATE TABLE avaliacoes (
    id BIGSERIAL PRIMARY KEY,
    nota INTEGER NOT NULL CHECK (nota BETWEEN 1 AND 5),
    comentario VARCHAR(500),
    data_avaliacao TIMESTAMP NOT NULL,
    perfil_id BIGINT NOT NULL REFERENCES perfis (id) ON DELETE CASCADE,
    filme_id BIGINT NOT NULL REFERENCES filmes (id) ON DELETE CASCADE,
    UNIQUE (perfil_id, filme_id)
);
