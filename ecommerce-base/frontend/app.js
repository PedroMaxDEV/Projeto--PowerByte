const API = "http://localhost:8080";

function salvarUsuarioLogado(usuario) {
    localStorage.setItem("usuarioLogado", JSON.stringify(usuario));
}

function obterUsuarioLogado() {
    return JSON.parse(localStorage.getItem("usuarioLogado"));
}

function logout() {
    localStorage.removeItem("usuarioLogado");
    alert("Logout realizado.");
    window.location.href = "login.html";
}

function obterCarrinho() {
    return JSON.parse(localStorage.getItem("carrinho")) || [];
}

function salvarCarrinho(carrinho) {
    localStorage.setItem("carrinho", JSON.stringify(carrinho));
}

function adicionarAoCarrinho(produto) {
    let carrinho = obterCarrinho();
    const itemExistente = carrinho.find(item => item.id === produto.id);

    if (itemExistente) {
        itemExistente.quantidade += 1;
    } else {
        carrinho.push({ ...produto, quantidade: 1 });
    }

    salvarCarrinho(carrinho);
    alert("Produto adicionado ao carrinho.");
}

function removerDoCarrinho(id) {
    let carrinho = obterCarrinho().filter(item => item.id !== id);
    salvarCarrinho(carrinho);
    renderCarrinho();
}

async function carregarProdutos() {
    const resposta = await fetch(`${API}/produtos`);
    const produtos = await resposta.json();

    const lista = document.getElementById("listaProdutos");
    if (!lista) return;

    lista.innerHTML = "";

    produtos.forEach(produto => {
        lista.innerHTML += `
            <div class="produto">
                <h3>${produto.nome}</h3>
                <p>${produto.descricao}</p>
                <p><strong>Categoria:</strong> ${produto.categoria}</p>
                <p><strong>Estoque:</strong> ${produto.estoque}</p>
                <p><strong>Preço:</strong> R$ ${Number(produto.preco).toFixed(2)}</p>
                <button onclick='adicionarAoCarrinho(${JSON.stringify(produto)})'>Adicionar ao carrinho</button>
            </div>
        `;
    });
}

function renderCarrinho() {
    const lista = document.getElementById("listaCarrinho");
    const totalEl = document.getElementById("totalCarrinho");
    if (!lista) return;

    const carrinho = obterCarrinho();
    lista.innerHTML = "";

    let total = 0;

    carrinho.forEach(item => {
        total += Number(item.preco) * item.quantidade;

        lista.innerHTML += `
            <div class="produto">
                <h3>${item.nome}</h3>
                <p>Preço: R$ ${Number(item.preco).toFixed(2)}</p>
                <p>Quantidade: ${item.quantidade}</p>
                <button onclick="removerDoCarrinho(${item.id})">Remover</button>
            </div>
        `;
    });

    if (totalEl) {
        totalEl.innerText = `Total: R$ ${total.toFixed(2)}`;
    }
}

async function finalizarCompra() {
    const usuario = obterUsuarioLogado();
    const carrinho = obterCarrinho();

    if (!usuario) {
        alert("Você precisa estar logado.");
        return;
    }

    if (carrinho.length === 0) {
        alert("Carrinho vazio.");
        return;
    }

    const payload = {
        usuarioId: usuario.id,
        itens: carrinho.map(item => ({
            produtoId: item.id,
            quantidade: item.quantidade
        }))
    };

    const resposta = await fetch(`${API}/pedidos`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    if (resposta.ok) {
        alert("Compra finalizada com sucesso.");
        localStorage.removeItem("carrinho");
        window.location.reload();
    } else {
        const erro = await resposta.text();
        alert("Erro: " + erro);
    }
}

async function cadastrarUsuario(event) {
    event.preventDefault();

    const usuario = {
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value,
        senha: document.getElementById("senha").value,
        tipo: "CLIENTE"
    };

    const resposta = await fetch(`${API}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(usuario)
    });

    const msg = document.getElementById("mensagem");

    if (resposta.ok) {
        msg.innerText = "Cadastro realizado com sucesso.";
        msg.className = "msg";
        document.getElementById("formCadastro").reset();
    } else {
        const erro = await resposta.json();
        msg.innerText = erro.mensagem || "Erro ao cadastrar.";
        msg.className = "msg erro";
    }
}

async function login(event) {
    event.preventDefault();

    const payload = {
        email: document.getElementById("email").value,
        senha: document.getElementById("senha").value
    };

    const resposta = await fetch(`${API}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    const msg = document.getElementById("mensagem");

    if (resposta.ok) {
        const usuario = await resposta.json();
        salvarUsuarioLogado(usuario);

        if (usuario.tipo === "ADMIN") {
            window.location.href = "admin.html";
        } else {
            window.location.href = "index.html";
        }
    } else {
        const erro = await resposta.json();
        msg.innerText = erro.mensagem || "Erro no login.";
        msg.className = "msg erro";
    }
}

async function cadastrarProduto(event) {
    event.preventDefault();

    const produto = {
        nome: document.getElementById("nome").value,
        descricao: document.getElementById("descricao").value,
        preco: parseFloat(document.getElementById("preco").value),
        estoque: parseInt(document.getElementById("estoque").value),
        categoria: document.getElementById("categoria").value
    };

    const resposta = await fetch(`${API}/produtos`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(produto)
    });

    const msg = document.getElementById("mensagem");

    if (resposta.ok) {
        msg.innerText = "Produto cadastrado com sucesso.";
        msg.className = "msg";
        document.getElementById("formProduto").reset();
        listarProdutosAdmin();
    } else {
        msg.innerText = "Erro ao cadastrar produto.";
        msg.className = "msg erro";
    }
}

async function listarProdutosAdmin() {
    const resposta = await fetch(`${API}/produtos`);
    const produtos = await resposta.json();

    const lista = document.getElementById("listaAdmin");
    if (!lista) return;

    lista.innerHTML = "";

    produtos.forEach(produto => {
        lista.innerHTML += `
            <div class="produto">
                <h3>${produto.nome}</h3>
                <p>${produto.descricao}</p>
                <p>Categoria: ${produto.categoria}</p>
                <p>Estoque: ${produto.estoque}</p>
                <p>Preço: R$ ${Number(produto.preco).toFixed(2)}</p>
                <button onclick="deletarProduto(${produto.id})">Excluir</button>
            </div>
        `;
    });
}

async function deletarProduto(id) {
    const resposta = await fetch(`${API}/produtos/${id}`, {
        method: "DELETE"
    });

    if (resposta.ok) {
        alert("Produto excluído.");
        listarProdutosAdmin();
    } else {
        alert("Erro ao excluir produto.");
    }
}