const estado = {
    token: localStorage.getItem('token'),
    perfil: JSON.parse(localStorage.getItem('perfil') || 'null'),
    perfis: [],
    filmes: [],
    categorias: [],
    usuarios: [],
    minhaLista: [],
    tela: 'catalogo',
    abaAdmin: 'filmes',
    busca: '',
    categoriaId: null,
    modoCadastro: false
};

const CLASSIFICACOES = ['L', '10', '12', '14', '16', '18'];

function esc(valor) {
    return String(valor ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}

function usuarioLogado() {
    if (!estado.token) return null;
    const payload = JSON.parse(atob(estado.token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    return { email: payload.sub, role: payload.role };
}

function isAdmin() {
    return usuarioLogado()?.role === 'ADMIN';
}

function duracao(minutos) {
    return `${Math.floor(minutos / 60)}h ${minutos % 60}min`;
}

function numero(valor) {
    return valor === '' || valor === null ? null : Number(valor);
}

function naLista(filmeId) {
    return estado.minhaLista.some(filme => filme.id === filmeId);
}

async function api(metodo, rota, corpo) {
    const headers = { 'Content-Type': 'application/json' };
    if (estado.token) headers.Authorization = `Bearer ${estado.token}`;
    const resposta = await fetch(rota, { method: metodo, headers, body: corpo ? JSON.stringify(corpo) : undefined });
    if (resposta.status === 401 && estado.token) {
        sair();
        throw new Error('Sessão expirada. Entre novamente.');
    }
    const texto = await resposta.text();
    const dados = texto ? JSON.parse(texto) : null;
    if (resposta.status === 403) throw new Error('Você não tem permissão para esta ação.');
    if (!resposta.ok) throw new Error([dados?.mensagem || 'Erro inesperado', ...(dados?.erros || [])].join('\n'));
    return dados;
}

function aviso(mensagem, erro = false) {
    const toast = document.getElementById('toast');
    toast.textContent = mensagem;
    toast.className = erro ? 'toast erro' : 'toast';
    clearTimeout(aviso.timer);
    aviso.timer = setTimeout(() => toast.className = 'toast escondido', 4500);
}

async function executar(acao, mensagemSucesso) {
    try {
        await acao();
        if (mensagemSucesso) aviso(mensagemSucesso);
    } catch (e) {
        aviso(e.message, true);
    }
}

function mostrarModal(html) {
    document.getElementById('modal-conteudo').innerHTML = html;
    document.getElementById('modal').classList.remove('escondido');
}

function fecharModal(evento) {
    if (evento && evento.target.id !== 'modal') return;
    document.getElementById('modal').classList.add('escondido');
}

function render() {
    const app = document.getElementById('app');
    if (!estado.token) {
        app.innerHTML = telaLogin();
    } else if (!estado.perfil) {
        app.innerHTML = telaPerfis();
    } else {
        app.innerHTML = cabecalho() + `<main class="conteudo">${conteudoAtual()}</main>`;
    }
}

function conteudoAtual() {
    if (estado.tela === 'lista') return telaMinhaLista();
    if (estado.tela === 'admin') return telaAdmin();
    return telaCatalogo();
}

function telaLogin() {
    const cadastro = estado.modoCadastro;
    return `
    <div class="tela-login">
        <h1 class="logo grande">springFlix</h1>
        <form class="cartao-login" onsubmit="${cadastro ? 'registrar' : 'entrar'}(event)">
            <h2>${cadastro ? 'Criar conta' : 'Entrar'}</h2>
            ${cadastro ? '<input name="nome" placeholder="Nome">' : ''}
            <input name="email" type="email" placeholder="E-mail">
            <input name="senha" type="password" placeholder="Senha">
            <button class="botao primario">${cadastro ? 'Cadastrar' : 'Entrar'}</button>
            <p class="alternar">
                ${cadastro ? 'Já tem conta?' : 'Novo por aqui?'}
                <a href="#" onclick="alternarCadastro(event)">${cadastro ? 'Entrar' : 'Criar conta'}</a>
            </p>
            <p class="dica">ADMIN: admin@springflix.com / admin123<br>USER: usuario@springflix.com / usuario123</p>
        </form>
    </div>`;
}

function alternarCadastro(evento) {
    evento.preventDefault();
    estado.modoCadastro = !estado.modoCadastro;
    render();
}

async function entrar(evento) {
    evento.preventDefault();
    const dados = new FormData(evento.target);
    await executar(() => login(dados.get('email'), dados.get('senha')));
}

async function registrar(evento) {
    evento.preventDefault();
    const dados = new FormData(evento.target);
    await executar(async () => {
        await api('POST', '/auth/registrar', { nome: dados.get('nome'), email: dados.get('email'), senha: dados.get('senha') });
        estado.modoCadastro = false;
        await login(dados.get('email'), dados.get('senha'));
    }, 'Conta criada com sucesso');
}

async function login(email, senha) {
    const resposta = await api('POST', '/auth/login', { email, senha });
    estado.token = resposta.token;
    localStorage.setItem('token', resposta.token);
    await irParaPerfis();
}

function sair() {
    localStorage.clear();
    estado.token = null;
    estado.perfil = null;
    fecharModal();
    render();
}

async function irParaPerfis() {
    estado.perfil = null;
    localStorage.removeItem('perfil');
    estado.perfis = await api('GET', '/perfis');
    render();
}

function telaPerfis() {
    return `
    <div class="tela-perfis">
        <h1 class="logo">springFlix</h1>
        <h2>Quem está assistindo?</h2>
        <div class="perfis">
            ${estado.perfis.map(perfil => `
            <div class="perfil">
                <button class="avatar cor-${perfil.id % 6}" onclick="selecionarPerfil(${perfil.id})">${esc(perfil.nome.charAt(0).toUpperCase())}</button>
                <span>${esc(perfil.nome)} ${perfil.infantil ? '<small class="selo">Kids</small>' : ''}</span>
                <div class="acoes-perfil">
                    <a href="#" onclick="editarPerfil(event, ${perfil.id})">Editar</a>
                    <a href="#" onclick="excluirPerfil(event, ${perfil.id})">Excluir</a>
                </div>
            </div>`).join('')}
        </div>
        <form class="novo-perfil" onsubmit="criarPerfil(event)">
            <input name="nome" placeholder="Nome do novo perfil">
            <label><input type="checkbox" name="infantil"> Infantil</label>
            <button class="botao">Adicionar perfil</button>
        </form>
        <button class="botao link" onclick="sair()">Sair da conta</button>
    </div>`;
}

async function selecionarPerfil(id) {
    estado.perfil = estado.perfis.find(perfil => perfil.id === id);
    localStorage.setItem('perfil', JSON.stringify(estado.perfil));
    await ir('catalogo');
}

async function criarPerfil(evento) {
    evento.preventDefault();
    const dados = new FormData(evento.target);
    await executar(async () => {
        await api('POST', '/perfis', { nome: dados.get('nome'), infantil: dados.get('infantil') === 'on' });
        await irParaPerfis();
    }, 'Perfil criado');
}

async function editarPerfil(evento, id) {
    evento.preventDefault();
    const perfil = estado.perfis.find(p => p.id === id);
    const nome = prompt('Nome do perfil:', perfil.nome);
    if (nome === null) return;
    const infantil = confirm('Este perfil é infantil?');
    await executar(async () => {
        await api('PUT', `/perfis/${id}`, { nome, infantil });
        await irParaPerfis();
    }, 'Perfil atualizado');
}

async function excluirPerfil(evento, id) {
    evento.preventDefault();
    if (!confirm('Excluir este perfil?')) return;
    await executar(async () => {
        await api('DELETE', `/perfis/${id}`);
        await irParaPerfis();
    }, 'Perfil excluído');
}

async function ir(tela) {
    await executar(() => navegar(tela));
}

async function navegar(tela) {
    if (tela === 'admin' && !isAdmin()) tela = 'catalogo';
    estado.minhaLista = await api('GET', `/perfis/${estado.perfil.id}/minha-lista`);
    if (tela === 'catalogo') await carregarCatalogo();
    if (tela === 'admin') await carregarAdmin();
    estado.tela = tela;
    render();
    window.scrollTo(0, 0);
}

async function carregarCatalogo() {
    [estado.filmes, estado.categorias] = await Promise.all([
        api('GET', `/filmes?titulo=${encodeURIComponent(estado.busca)}`),
        api('GET', '/categorias')
    ]);
}

function cabecalho() {
    const aba = (tela, nome) => `<button class="${estado.tela === tela ? 'ativo' : ''}" onclick="ir('${tela}')">${nome}</button>`;
    return `
    <header class="topo">
        <span class="logo" onclick="ir('catalogo')">springFlix</span>
        <nav>
            ${aba('catalogo', 'Início')}
            ${aba('lista', 'Minha lista')}
            ${isAdmin() ? aba('admin', 'Administração') : ''}
        </nav>
        <div class="usuario">
            <span class="avatar pequeno cor-${estado.perfil.id % 6}">${esc(estado.perfil.nome.charAt(0).toUpperCase())}</span>
            <span>${esc(estado.perfil.nome)}</span>
            <button class="botao link" onclick="executar(irParaPerfis)">Trocar perfil</button>
            <button class="botao link" onclick="sair()">Sair</button>
        </div>
    </header>`;
}

function telaCatalogo() {
    return `
    <div class="filtros">
        <input class="busca" placeholder="Buscar por título" value="${esc(estado.busca)}" oninput="buscar(this.value)">
        <div class="chips">
            <button class="chip ${estado.categoriaId ? '' : 'ativo'}" onclick="filtrarCategoria(null)">Todas</button>
            ${estado.categorias.map(c => `<button class="chip ${estado.categoriaId === c.id ? 'ativo' : ''}" onclick="filtrarCategoria(${c.id})">${esc(c.nome)}</button>`).join('')}
        </div>
    </div>
    <div id="vitrine">${vitrine()}</div>`;
}

function vitrine() {
    let filmes = estado.filmes;
    if (estado.categoriaId) filmes = filmes.filter(f => f.categorias.some(c => c.id === estado.categoriaId));
    if (!filmes.length) return '<p class="vazio">Nenhum filme encontrado.</p>';
    if (estado.busca || estado.categoriaId) return `<div class="grade">${filmes.map(cartao).join('')}</div>`;
    return estado.categorias.map(categoria => {
        const daCategoria = filmes.filter(f => f.categorias.some(c => c.id === categoria.id));
        if (!daCategoria.length) return '';
        return `<section class="fileira"><h3>${esc(categoria.nome)}</h3><div class="trilho">${daCategoria.map(cartao).join('')}</div></section>`;
    }).join('');
}

function cartao(filme) {
    return `
    <div class="cartao cor-${filme.id % 8}" onclick="abrirFilme(${filme.id})">
        <span class="classificacao c${filme.classificacaoIndicativa}">${filme.classificacaoIndicativa}</span>
        ${naLista(filme.id) ? '<span class="na-lista">✓</span>' : ''}
        <strong>${esc(filme.titulo)}</strong>
        <small>${filme.anoLancamento} · ${duracao(filme.duracaoMinutos)}</small>
    </div>`;
}

function buscar(valor) {
    clearTimeout(buscar.timer);
    buscar.timer = setTimeout(() => executar(async () => {
        estado.busca = valor.trim();
        await carregarCatalogo();
        document.getElementById('vitrine').innerHTML = vitrine();
    }), 300);
}

function filtrarCategoria(id) {
    estado.categoriaId = id;
    render();
}

function atualizarFundo() {
    const vitrineAtual = document.getElementById('vitrine');
    if (estado.tela === 'catalogo' && vitrineAtual) vitrineAtual.innerHTML = vitrine();
    if (estado.tela === 'lista') render();
}

async function abrirFilme(id) {
    await executar(() => mostrarFilme(id));
}

async function mostrarFilme(id) {
    const [filme, avaliacoes] = await Promise.all([
        api('GET', `/filmes/${id}`),
        api('GET', `/avaliacoes?filmeId=${id}`)
    ]);
    const minha = avaliacoes.find(a => a.perfilId === estado.perfil.id);
    const media = avaliacoes.length ? (avaliacoes.reduce((soma, a) => soma + a.nota, 0) / avaliacoes.length).toFixed(1) : null;
    mostrarModal(`
    <button class="fechar" onclick="fecharModal()">×</button>
    <div class="banner cor-${filme.id % 8}"><h2>${esc(filme.titulo)}</h2></div>
    <div class="detalhe">
        <p class="meta">
            <span class="classificacao c${filme.classificacaoIndicativa}">${filme.classificacaoIndicativa}</span>
            ${filme.anoLancamento} · ${duracao(filme.duracaoMinutos)} · ${esc(filme.diretor)}
            ${media ? `· <span class="estrelas">★ ${media}</span>` : ''}
        </p>
        <p class="chips">${filme.categorias.map(c => `<span class="chip">${esc(c.nome)}</span>`).join('')}</p>
        <p class="sinopse">${esc(filme.sinopse)}</p>
        ${naLista(filme.id)
            ? `<button class="botao" onclick="removerDaLista(${filme.id})">✓ Na minha lista</button>`
            : `<button class="botao primario" onclick="adicionarNaLista(${filme.id})">+ Minha lista</button>`}
        <h3>Avaliações</h3>
        <form class="form-avaliacao" onsubmit="salvarAvaliacao(event, ${filme.id}, ${minha ? minha.id : null})">
            <select name="nota">${[5, 4, 3, 2, 1].map(n => `<option value="${n}" ${minha?.nota === n ? 'selected' : ''}>${'★'.repeat(n)}</option>`).join('')}</select>
            <input name="comentario" placeholder="Comentário (opcional)" value="${esc(minha?.comentario)}">
            <button class="botao primario">${minha ? 'Atualizar' : 'Avaliar'}</button>
            ${minha ? `<button type="button" class="botao" onclick="excluirAvaliacao(${minha.id}, ${filme.id})">Excluir</button>` : ''}
        </form>
        <ul class="avaliacoes">
            ${avaliacoes.map(a => `
            <li>
                <strong>${esc(a.perfilNome)}</strong>
                <span class="estrelas">${'★'.repeat(a.nota)}${'☆'.repeat(5 - a.nota)}</span>
                ${a.comentario ? `<p>${esc(a.comentario)}</p>` : ''}
            </li>`).join('') || '<li class="vazio">Nenhuma avaliação ainda.</li>'}
        </ul>
    </div>`);
}

async function adicionarNaLista(filmeId) {
    await executar(async () => {
        estado.minhaLista = await api('POST', `/perfis/${estado.perfil.id}/minha-lista/${filmeId}`);
        await mostrarFilme(filmeId);
        atualizarFundo();
    }, 'Adicionado à minha lista');
}

async function removerDaLista(filmeId) {
    await executar(async () => {
        await api('DELETE', `/perfis/${estado.perfil.id}/minha-lista/${filmeId}`);
        estado.minhaLista = estado.minhaLista.filter(f => f.id !== filmeId);
        await mostrarFilme(filmeId);
        atualizarFundo();
    }, 'Removido da minha lista');
}

async function salvarAvaliacao(evento, filmeId, avaliacaoId) {
    evento.preventDefault();
    const dados = new FormData(evento.target);
    const corpo = { perfilId: estado.perfil.id, filmeId, nota: Number(dados.get('nota')), comentario: dados.get('comentario') };
    await executar(async () => {
        await api(avaliacaoId ? 'PUT' : 'POST', avaliacaoId ? `/avaliacoes/${avaliacaoId}` : '/avaliacoes', corpo);
        await mostrarFilme(filmeId);
    }, 'Avaliação salva');
}

async function excluirAvaliacao(avaliacaoId, filmeId) {
    if (!confirm('Excluir sua avaliação?')) return;
    await executar(async () => {
        await api('DELETE', `/avaliacoes/${avaliacaoId}`);
        await mostrarFilme(filmeId);
    }, 'Avaliação excluída');
}

function telaMinhaLista() {
    return `
    <h2>Minha lista de ${esc(estado.perfil.nome)}</h2>
    ${estado.minhaLista.length
        ? `<div class="grade">${estado.minhaLista.map(cartao).join('')}</div>`
        : '<p class="vazio">Sua lista está vazia. Abra um filme e clique em "+ Minha lista".</p>'}`;
}

async function carregarAdmin() {
    [estado.filmes, estado.categorias, estado.usuarios] = await Promise.all([
        api('GET', '/filmes'),
        api('GET', '/categorias'),
        api('GET', '/usuarios')
    ]);
    estado.busca = '';
}

async function recarregarAdmin() {
    await carregarAdmin();
    render();
}

function trocarAbaAdmin(aba) {
    estado.abaAdmin = aba;
    render();
}

function telaAdmin() {
    const aba = (id, nome) => `<button class="chip ${estado.abaAdmin === id ? 'ativo' : ''}" onclick="trocarAbaAdmin('${id}')">${nome}</button>`;
    const conteudo = { filmes: adminFilmes, categorias: adminCategorias, usuarios: adminUsuarios }[estado.abaAdmin]();
    return `
    <h2>Administração</h2>
    <div class="chips">${aba('filmes', 'Filmes')}${aba('categorias', 'Categorias')}${aba('usuarios', 'Usuários')}</div>
    ${conteudo}`;
}

function adminFilmes() {
    return `
    <button class="botao primario" onclick="abrirFormFilme(null)">+ Novo filme</button>
    <table>
        <thead><tr><th>Título</th><th>Ano</th><th>Class.</th><th>Categorias</th><th></th></tr></thead>
        <tbody>
            ${estado.filmes.map(f => `
            <tr>
                <td>${esc(f.titulo)}</td>
                <td>${f.anoLancamento}</td>
                <td><span class="classificacao c${f.classificacaoIndicativa}">${f.classificacaoIndicativa}</span></td>
                <td>${esc(f.categorias.map(c => c.nome).join(', '))}</td>
                <td class="acoes">
                    <button class="botao pequeno" onclick="abrirFormFilme(${f.id})">Editar</button>
                    <button class="botao pequeno perigo" onclick="excluirFilme(${f.id})">Excluir</button>
                </td>
            </tr>`).join('')}
        </tbody>
    </table>`;
}

function abrirFormFilme(id) {
    const filme = estado.filmes.find(f => f.id === id) || { categorias: [] };
    mostrarModal(`
    <button class="fechar" onclick="fecharModal()">×</button>
    <form class="formulario" onsubmit="salvarFilme(event, ${id})">
        <h2>${id ? 'Editar filme' : 'Novo filme'}</h2>
        <label>Título <input name="titulo" value="${esc(filme.titulo)}"></label>
        <label>Diretor <input name="diretor" value="${esc(filme.diretor)}"></label>
        <div class="linha">
            <label>Ano <input name="anoLancamento" type="number" value="${esc(filme.anoLancamento)}"></label>
            <label>Duração (min) <input name="duracaoMinutos" type="number" value="${esc(filme.duracaoMinutos)}"></label>
            <label>Classificação
                <select name="classificacaoIndicativa">
                    ${CLASSIFICACOES.map(c => `<option ${filme.classificacaoIndicativa === c ? 'selected' : ''}>${c}</option>`).join('')}
                </select>
            </label>
        </div>
        <label>Sinopse <textarea name="sinopse" rows="4">${esc(filme.sinopse)}</textarea></label>
        <fieldset>
            <legend>Categorias</legend>
            ${estado.categorias.map(c => `
            <label class="check">
                <input type="checkbox" name="categoriaIds" value="${c.id}" ${filme.categorias.some(x => x.id === c.id) ? 'checked' : ''}> ${esc(c.nome)}
            </label>`).join('')}
        </fieldset>
        <button class="botao primario">Salvar</button>
    </form>`);
}

async function salvarFilme(evento, id) {
    evento.preventDefault();
    const dados = new FormData(evento.target);
    const corpo = {
        titulo: dados.get('titulo'),
        diretor: dados.get('diretor'),
        anoLancamento: numero(dados.get('anoLancamento')),
        duracaoMinutos: numero(dados.get('duracaoMinutos')),
        classificacaoIndicativa: dados.get('classificacaoIndicativa'),
        sinopse: dados.get('sinopse'),
        categoriaIds: dados.getAll('categoriaIds').map(Number)
    };
    await executar(async () => {
        await api(id ? 'PUT' : 'POST', id ? `/filmes/${id}` : '/filmes', corpo);
        fecharModal();
        await recarregarAdmin();
    }, 'Filme salvo');
}

async function excluirFilme(id) {
    if (!confirm('Excluir este filme? As avaliações dele também serão excluídas.')) return;
    await executar(async () => {
        await api('DELETE', `/filmes/${id}`);
        await recarregarAdmin();
    }, 'Filme excluído');
}

function adminCategorias() {
    return `
    <form class="linha-form" onsubmit="criarCategoria(event)">
        <input name="nome" placeholder="Nome da nova categoria">
        <button class="botao primario">Adicionar</button>
    </form>
    <table>
        <thead><tr><th>Nome</th><th>Filmes</th><th></th></tr></thead>
        <tbody>
            ${estado.categorias.map(c => `
            <tr>
                <td>${esc(c.nome)}</td>
                <td>${estado.filmes.filter(f => f.categorias.some(x => x.id === c.id)).length}</td>
                <td class="acoes">
                    <button class="botao pequeno" onclick="renomearCategoria(${c.id})">Renomear</button>
                    <button class="botao pequeno perigo" onclick="excluirCategoria(${c.id})">Excluir</button>
                </td>
            </tr>`).join('')}
        </tbody>
    </table>`;
}

async function criarCategoria(evento) {
    evento.preventDefault();
    const dados = new FormData(evento.target);
    await executar(async () => {
        await api('POST', '/categorias', { nome: dados.get('nome') });
        await recarregarAdmin();
    }, 'Categoria criada');
}

async function renomearCategoria(id) {
    const categoria = estado.categorias.find(c => c.id === id);
    const nome = prompt('Novo nome da categoria:', categoria.nome);
    if (nome === null) return;
    await executar(async () => {
        await api('PUT', `/categorias/${id}`, { nome });
        await recarregarAdmin();
    }, 'Categoria atualizada');
}

async function excluirCategoria(id) {
    if (!confirm('Excluir esta categoria?')) return;
    await executar(async () => {
        await api('DELETE', `/categorias/${id}`);
        await recarregarAdmin();
    }, 'Categoria excluída');
}

function adminUsuarios() {
    return `
    <button class="botao primario" onclick="abrirFormUsuario(null)">+ Novo usuário</button>
    <table>
        <thead><tr><th>Nome</th><th>E-mail</th><th>Papel</th><th></th></tr></thead>
        <tbody>
            ${estado.usuarios.map(u => `
            <tr>
                <td>${esc(u.nome)}</td>
                <td>${esc(u.email)}</td>
                <td><span class="chip">${u.role}</span></td>
                <td class="acoes">
                    <button class="botao pequeno" onclick="abrirFormUsuario(${u.id})">Editar</button>
                    <button class="botao pequeno perigo" onclick="excluirUsuario(${u.id})">Excluir</button>
                </td>
            </tr>`).join('')}
        </tbody>
    </table>`;
}

function abrirFormUsuario(id) {
    const usuario = estado.usuarios.find(u => u.id === id) || { role: 'USER' };
    mostrarModal(`
    <button class="fechar" onclick="fecharModal()">×</button>
    <form class="formulario" onsubmit="salvarUsuario(event, ${id})">
        <h2>${id ? 'Editar usuário' : 'Novo usuário'}</h2>
        <label>Nome <input name="nome" value="${esc(usuario.nome)}"></label>
        <label>E-mail <input name="email" type="email" value="${esc(usuario.email)}"></label>
        <label>Senha <input name="senha" type="password" placeholder="Mínimo de 6 caracteres"></label>
        <label>Papel
            <select name="role">
                <option ${usuario.role === 'USER' ? 'selected' : ''}>USER</option>
                <option ${usuario.role === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
            </select>
        </label>
        <button class="botao primario">Salvar</button>
    </form>`);
}

async function salvarUsuario(evento, id) {
    evento.preventDefault();
    const dados = new FormData(evento.target);
    const corpo = { nome: dados.get('nome'), email: dados.get('email'), senha: dados.get('senha'), role: dados.get('role') };
    await executar(async () => {
        await api(id ? 'PUT' : 'POST', id ? `/usuarios/${id}` : '/usuarios', corpo);
        fecharModal();
        await recarregarAdmin();
    }, 'Usuário salvo');
}

async function excluirUsuario(id) {
    if (!confirm('Excluir este usuário? Os perfis e avaliações dele também serão excluídos.')) return;
    await executar(async () => {
        await api('DELETE', `/usuarios/${id}`);
        await recarregarAdmin();
    }, 'Usuário excluído');
}

async function iniciar() {
    if (!estado.token) {
        render();
        return;
    }
    await executar(async () => {
        try {
            if (estado.perfil) await navegar('catalogo');
            else await irParaPerfis();
        } catch (e) {
            if (estado.token) await irParaPerfis();
            else throw e;
        }
    });
}

iniciar();
