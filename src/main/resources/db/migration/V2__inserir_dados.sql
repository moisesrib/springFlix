INSERT INTO usuarios (nome, email, senha, role) VALUES
    ('Administrador', 'admin@springflix.com', '$2a$10$1j/zV.M.Y0qvVgi97q1HGeBOyD9d.mM20qeTwCHA99kM7GyKYAnaG', 'ADMIN'),
    ('Usuário Demo', 'usuario@springflix.com', '$2a$10$/DUbZDa89zZJbfsfa2rVSulnEfNqISXD.fTNMtE/tWEtAUE/GHhf2', 'USER');

INSERT INTO categorias (nome) VALUES
    ('Ação'),
    ('Animação'),
    ('Aventura'),
    ('Comédia'),
    ('Crime'),
    ('Drama'),
    ('Fantasia'),
    ('Ficção Científica'),
    ('Guerra'),
    ('Nacional'),
    ('Romance'),
    ('Suspense'),
    ('Terror');

INSERT INTO filmes (titulo, sinopse, diretor, ano_lancamento, duracao_minutos, classificacao_indicativa) VALUES
    ('O Poderoso Chefão', 'Nos anos 1940, o envelhecido Don Vito Corleone comanda uma das famílias mais poderosas da máfia de Nova York. Quando a disputa com clãs rivais se acirra, seu filho caçula Michael, que sempre quis distância dos negócios da família, precisa decidir de que lado está.', 'Francis Ford Coppola', 1972, 175, '14'),
    ('Um Sonho de Liberdade', 'Condenado à prisão perpétua pela morte da esposa e do amante dela, crime que jura não ter cometido, o banqueiro Andy Dufresne enfrenta a dura rotina da penitenciária de Shawshank e constrói uma amizade improvável com o veterano detento Red.', 'Frank Darabont', 1994, 142, '16'),
    ('Batman: O Cavaleiro das Trevas', 'Com o apoio do tenente Jim Gordon e do promotor Harvey Dent, Batman avança na luta contra o crime organizado de Gotham. A aliança é posta à prova quando surge o Coringa, um criminoso imprevisível disposto a mergulhar a cidade no caos.', 'Christopher Nolan', 2008, 152, '12'),
    ('A Origem', 'Dom Cobb é um ladrão especializado em roubar segredos do subconsciente enquanto as pessoas sonham. Em troca da chance de voltar para casa e rever os filhos, ele aceita uma missão tida como impossível: implantar uma ideia na mente de um poderoso herdeiro.', 'Christopher Nolan', 2010, 148, '14'),
    ('Interestelar', 'Em um futuro próximo, com a Terra castigada por pragas e tempestades de poeira, o ex-piloto Cooper deixa a família para trás e se junta a uma equipe de astronautas que atravessa um buraco de minhoca em busca de um novo lar para a humanidade.', 'Christopher Nolan', 2014, 169, '10'),
    ('Matrix', 'O programador Thomas Anderson leva uma vida dupla como o hacker Neo até ser procurado pelo misterioso Morpheus, líder de um grupo rebelde que oferece a ele respostas perturbadoras sobre a verdadeira natureza da realidade.', 'Lana Wachowski e Lilly Wachowski', 1999, 136, '14'),
    ('Pulp Fiction: Tempo de Violência', 'Em Los Angeles, as trajetórias de dois matadores de aluguel, um boxeador em apuros, a esposa de um poderoso chefão do crime e um casal de assaltantes se cruzam em histórias marcadas por violência, humor ácido e reviravoltas.', 'Quentin Tarantino', 1994, 154, '18'),
    ('O Resgate do Soldado Ryan', 'Logo após o desembarque aliado na Normandia, em 1944, o capitão John Miller recebe a missão de guiar um pequeno grupo de soldados por território inimigo para encontrar o paraquedista James Ryan, cujos três irmãos morreram em combate.', 'Steven Spielberg', 1998, 169, '14'),
    ('Forrest Gump: O Contador de Histórias', 'Sentado em um banco de ponto de ônibus, Forrest Gump, um homem de bom coração e raciocínio simples, conta a desconhecidos a trajetória improvável que o levou a presenciar marcos da história americana sem nunca esquecer seu grande amor, Jenny.', 'Robert Zemeckis', 1994, 142, '14'),
    ('O Senhor dos Anéis: A Sociedade do Anel', 'Na Terra-média, o jovem hobbit Frodo herda um anel cobiçado pelo senhor sombrio Sauron. Ao lado de uma comitiva de companheiros, ele parte em uma jornada perigosa para destruir o objeto nas chamas da Montanha da Perdição.', 'Peter Jackson', 2001, 178, '14'),
    ('Cidade de Deus', 'Buscapé cresce na Cidade de Deus, no Rio de Janeiro, entre os anos 1960 e 1980, e acompanha pela lente de sua câmera a escalada do tráfico e da violência que transforma a vida dos amigos de infância.', 'Fernando Meirelles e Kátia Lund', 2002, 130, '16'),
    ('Central do Brasil', 'Dora, uma ex-professora amarga que escreve cartas para analfabetos na estação Central do Brasil, acaba acompanhando o menino Josué em uma viagem pelo sertão nordestino em busca do pai que ele nunca conheceu.', 'Walter Salles', 1998, 113, '12'),
    ('Tropa de Elite', 'No Rio de Janeiro de 1997, o capitão Nascimento, do BOPE, precisa encontrar um substituto antes do nascimento do filho, enquanto comanda operações arriscadas nas favelas às vésperas da visita do papa.', 'José Padilha', 2007, 115, '16'),
    ('O Auto da Compadecida', 'No sertão da Paraíba, os amigos João Grilo e Chicó sobrevivem aplicando golpes em padres, cangaceiros e patrões, até que as trapaças os levam a um julgamento inusitado diante de Jesus e do diabo.', 'Guel Arraes', 2000, 104, '12'),
    ('Ainda Estou Aqui', 'No Rio de Janeiro de 1971, durante a ditadura militar, Eunice Paiva vê a vida da família mudar quando o marido, o ex-deputado Rubens Paiva, é levado por agentes do governo e não volta para casa.', 'Walter Salles', 2024, 137, '14'),
    ('Toy Story', 'Woody, o boneco caubói favorito de Andy, sente o seu posto ameaçado com a chegada de Buzz Lightyear, um patrulheiro espacial que não sabe que é um brinquedo. A rivalidade coloca os dois em uma aventura longe de casa.', 'John Lasseter', 1995, 81, 'L'),
    ('O Rei Leão', 'O jovem leão Simba, herdeiro do trono das Terras do Reino, foge após uma tragédia armada pelo tio Scar. Longe de casa, ele cresce sem responsabilidades até precisar decidir se volta para assumir o seu lugar.', 'Roger Allers e Rob Minkoff', 1994, 88, 'L'),
    ('Procurando Nemo', 'Quando o filho Nemo é capturado por um mergulhador, o superprotetor peixe-palhaço Marlin atravessa o oceano ao lado da esquecida Dory para trazê-lo de volta do aquário de um dentista em Sydney.', 'Andrew Stanton', 2003, 100, 'L'),
    ('Divertida Mente', 'Dentro da mente da menina Riley, as emoções Alegria, Tristeza, Raiva, Medo e Nojinho tentam guiá-la durante a mudança da família para São Francisco, até que um acidente tira Alegria e Tristeza da sala de controle.', 'Pete Docter', 2015, 95, 'L'),
    ('A Viagem de Chihiro', 'Durante uma mudança, a menina Chihiro entra com os pais em um mundo misterioso de espíritos. Depois que os pais são transformados em porcos, ela precisa trabalhar em uma casa de banhos mágica para encontrar um jeito de salvá-los.', 'Hayao Miyazaki', 2001, 125, 'L'),
    ('Homem-Aranha no Aranhaverso', 'Miles Morales, um adolescente do Brooklyn, ganha poderes após a picada de uma aranha e descobre que não é o único herói aracnídeo. Ao lado de versões do Homem-Aranha vindas de outras dimensões, ele precisa deter uma ameaça que coloca todas as realidades em risco.', 'Bob Persichetti, Peter Ramsey e Rodney Rothman', 2018, 117, '10'),
    ('Vingadores: Ultimato', 'Depois de uma derrota devastadora que apagou metade da vida no universo, os Vingadores que restaram se reúnem para uma última e arriscada missão que pode reverter o desastre e trazer de volta quem se foi.', 'Anthony Russo e Joe Russo', 2019, 181, '12'),
    ('Parasita', 'Uma família pobre de Seul arma um plano para se infiltrar, um a um, entre os funcionários de uma família rica, fingindo ser profissionais qualificados. O que parecia um golpe perfeito logo começa a sair do controle.', 'Bong Joon-ho', 2019, 132, '16'),
    ('Coringa', 'Arthur Fleck, um palhaço de aluguel que sonha em ser comediante, enfrenta a solidão e o descaso em uma Gotham decadente. A cada rejeição, sua saúde mental se deteriora e ele começa a trilhar um caminho sombrio e sem volta.', 'Todd Phillips', 2019, 122, '16'),
    ('Titanic', 'Em 1912, Rose, uma jovem da alta sociedade presa a um noivado arranjado, conhece o artista sem dinheiro Jack a bordo do Titanic, e os dois vivem um romance que desafia as barreiras de classe durante a viagem inaugural do navio.', 'James Cameron', 1997, 195, '12'),
    ('Gladiador', 'Traído pelo ambicioso filho do imperador, o general romano Máximo perde tudo e é vendido como escravo. Transformado em gladiador, ele conquista as arenas enquanto busca vingança contra o homem que destruiu sua vida.', 'Ridley Scott', 2000, 155, '14'),
    ('De Volta para o Futuro', 'O adolescente Marty McFly é enviado acidentalmente de 1985 para 1955 em uma máquina do tempo criada por seu amigo cientista. Preso no passado, ele precisa garantir que seus pais se apaixonem e achar um jeito de voltar para casa.', 'Robert Zemeckis', 1985, 116, '12'),
    ('Jurassic Park: O Parque dos Dinossauros', 'Um grupo de visitantes, entre eles dois paleontólogos e um matemático, é convidado a avaliar um parque temático em uma ilha remota onde dinossauros foram recriados a partir de DNA pré-histórico. A visita vira uma luta pela sobrevivência quando a segurança falha.', 'Steven Spielberg', 1993, 127, '10'),
    ('O Silêncio dos Inocentes', 'A jovem agente do FBI Clarice Starling busca a ajuda do brilhante e perigoso psiquiatra Hannibal Lecter, preso por seus crimes, para capturar um assassino em série que está aterrorizando o país.', 'Jonathan Demme', 1991, 118, '14'),
    ('Corra!', 'Chris, um jovem fotógrafo negro, viaja para conhecer a família de sua namorada branca em uma propriedade no interior. A recepção calorosa demais e comportamentos estranhos começam a indicar que algo muito sinistro está acontecendo.', 'Jordan Peele', 2017, 104, '14');

INSERT INTO filme_categorias (filme_id, categoria_id)
SELECT f.id, c.id
FROM (VALUES
    ('O Poderoso Chefão', 'Crime'),
    ('O Poderoso Chefão', 'Drama'),
    ('Um Sonho de Liberdade', 'Drama'),
    ('Um Sonho de Liberdade', 'Crime'),
    ('Batman: O Cavaleiro das Trevas', 'Ação'),
    ('Batman: O Cavaleiro das Trevas', 'Crime'),
    ('Batman: O Cavaleiro das Trevas', 'Suspense'),
    ('A Origem', 'Ficção Científica'),
    ('A Origem', 'Ação'),
    ('A Origem', 'Suspense'),
    ('Interestelar', 'Ficção Científica'),
    ('Interestelar', 'Aventura'),
    ('Interestelar', 'Drama'),
    ('Matrix', 'Ficção Científica'),
    ('Matrix', 'Ação'),
    ('Pulp Fiction: Tempo de Violência', 'Crime'),
    ('Pulp Fiction: Tempo de Violência', 'Drama'),
    ('O Resgate do Soldado Ryan', 'Guerra'),
    ('O Resgate do Soldado Ryan', 'Drama'),
    ('Forrest Gump: O Contador de Histórias', 'Drama'),
    ('Forrest Gump: O Contador de Histórias', 'Comédia'),
    ('Forrest Gump: O Contador de Histórias', 'Romance'),
    ('O Senhor dos Anéis: A Sociedade do Anel', 'Fantasia'),
    ('O Senhor dos Anéis: A Sociedade do Anel', 'Aventura'),
    ('Cidade de Deus', 'Crime'),
    ('Cidade de Deus', 'Drama'),
    ('Cidade de Deus', 'Nacional'),
    ('Central do Brasil', 'Drama'),
    ('Central do Brasil', 'Nacional'),
    ('Tropa de Elite', 'Ação'),
    ('Tropa de Elite', 'Crime'),
    ('Tropa de Elite', 'Nacional'),
    ('O Auto da Compadecida', 'Comédia'),
    ('O Auto da Compadecida', 'Nacional'),
    ('Ainda Estou Aqui', 'Drama'),
    ('Ainda Estou Aqui', 'Nacional'),
    ('Toy Story', 'Animação'),
    ('Toy Story', 'Aventura'),
    ('Toy Story', 'Comédia'),
    ('O Rei Leão', 'Animação'),
    ('O Rei Leão', 'Aventura'),
    ('O Rei Leão', 'Drama'),
    ('Procurando Nemo', 'Animação'),
    ('Procurando Nemo', 'Aventura'),
    ('Procurando Nemo', 'Comédia'),
    ('Divertida Mente', 'Animação'),
    ('Divertida Mente', 'Comédia'),
    ('Divertida Mente', 'Drama'),
    ('A Viagem de Chihiro', 'Animação'),
    ('A Viagem de Chihiro', 'Fantasia'),
    ('A Viagem de Chihiro', 'Aventura'),
    ('Homem-Aranha no Aranhaverso', 'Animação'),
    ('Homem-Aranha no Aranhaverso', 'Ação'),
    ('Homem-Aranha no Aranhaverso', 'Aventura'),
    ('Vingadores: Ultimato', 'Ação'),
    ('Vingadores: Ultimato', 'Aventura'),
    ('Vingadores: Ultimato', 'Ficção Científica'),
    ('Parasita', 'Drama'),
    ('Parasita', 'Suspense'),
    ('Parasita', 'Comédia'),
    ('Coringa', 'Drama'),
    ('Coringa', 'Crime'),
    ('Coringa', 'Suspense'),
    ('Titanic', 'Romance'),
    ('Titanic', 'Drama'),
    ('Gladiador', 'Ação'),
    ('Gladiador', 'Aventura'),
    ('Gladiador', 'Drama'),
    ('De Volta para o Futuro', 'Ficção Científica'),
    ('De Volta para o Futuro', 'Aventura'),
    ('De Volta para o Futuro', 'Comédia'),
    ('Jurassic Park: O Parque dos Dinossauros', 'Aventura'),
    ('Jurassic Park: O Parque dos Dinossauros', 'Ficção Científica'),
    ('Jurassic Park: O Parque dos Dinossauros', 'Ação'),
    ('O Silêncio dos Inocentes', 'Suspense'),
    ('O Silêncio dos Inocentes', 'Crime'),
    ('O Silêncio dos Inocentes', 'Terror'),
    ('Corra!', 'Terror'),
    ('Corra!', 'Suspense')
) AS v (titulo, categoria)
JOIN filmes f ON f.titulo = v.titulo
JOIN categorias c ON c.nome = v.categoria;

INSERT INTO perfis (nome, infantil, usuario_id) VALUES
    ('Admin', FALSE, (SELECT id FROM usuarios WHERE email = 'admin@springflix.com')),
    ('Ana', FALSE, (SELECT id FROM usuarios WHERE email = 'usuario@springflix.com')),
    ('Kids', TRUE, (SELECT id FROM usuarios WHERE email = 'usuario@springflix.com'));

INSERT INTO minha_lista (perfil_id, filme_id)
SELECT p.id, f.id
FROM (VALUES
    ('Admin', 'Interestelar'),
    ('Admin', 'Parasita'),
    ('Ana', 'Cidade de Deus'),
    ('Ana', 'A Origem'),
    ('Ana', 'Ainda Estou Aqui'),
    ('Kids', 'Toy Story'),
    ('Kids', 'Procurando Nemo'),
    ('Kids', 'O Rei Leão')
) AS v (perfil, titulo)
JOIN perfis p ON p.nome = v.perfil
JOIN filmes f ON f.titulo = v.titulo;

INSERT INTO avaliacoes (nota, comentario, data_avaliacao, perfil_id, filme_id)
SELECT v.nota, v.comentario, LOCALTIMESTAMP, p.id, f.id
FROM (VALUES
    ('Admin', 'Interestelar', 5, 'Trilha sonora e visual impressionantes.'),
    ('Admin', 'Matrix', 4, 'Revolucionou os filmes de ação.'),
    ('Ana', 'O Poderoso Chefão', 5, 'Uma obra-prima do cinema.'),
    ('Ana', 'Cidade de Deus', 5, 'Retrato forte e muito bem dirigido.'),
    ('Ana', 'Titanic', 3, 'Bonito, mas longo demais.'),
    ('Kids', 'Toy Story', 5, 'Muito divertido!'),
    ('Kids', 'Procurando Nemo', 4, 'A Dory é a melhor.')
) AS v (perfil, titulo, nota, comentario)
JOIN perfis p ON p.nome = v.perfil
JOIN filmes f ON f.titulo = v.titulo;
