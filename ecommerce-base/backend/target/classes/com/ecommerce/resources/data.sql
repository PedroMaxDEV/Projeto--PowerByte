INSERT INTO produtos (nome, descricao, preco, estoque, categoria)
SELECT * FROM (
    SELECT 'Notebook Dell Inspiron', 'Notebook para estudos e trabalho', 3500.00, 10, 'Notebook'
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM produtos WHERE nome = 'Notebook Dell Inspiron'
);

INSERT INTO produtos (nome, descricao, preco, estoque, categoria)
SELECT * FROM (
    SELECT 'Mouse Gamer', 'Mouse com RGB e 6 botões', 120.00, 25, 'Periférico'
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM produtos WHERE nome = 'Mouse Gamer'
);

INSERT INTO produtos (nome, descricao, preco, estoque, categoria)
SELECT * FROM (
    SELECT 'Teclado Mecânico', 'Teclado ABNT2 com switches azuis', 280.00, 15, 'Periférico'
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM produtos WHERE nome = 'Teclado Mecânico'
);

INSERT INTO usuarios (nome, email, senha, tipo)
SELECT * FROM (
    SELECT 'Administrador', 'admin@admin.com', '123', 'ADMIN'
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE email = 'admin@admin.com'
);