function aplicarFiltro(tabelaId, camposColuna) 
{
    const campo = document.getElementById("campoFiltro").value;
    const valor = document.getElementById("valorFiltro").value.toLowerCase();
    const linhas = document.querySelectorAll(`#${tabelaId} tr`);

    linhas.forEach(linha => {
        const celulas = linha.querySelectorAll("td");
        const indice = camposColuna[campo];
        const valorCampo = celulas[indice]?.textContent.toLowerCase();

        if(valorCampo.includes(valor))
            linha.style.display = "";
        else
            linha.style.display = "none";
    });
}

function limparFiltro(tabelaId) 
{
    document.getElementById("valorFiltro").value = "";
    document.querySelectorAll(`#${tabelaId} tr`).forEach(linha => {
        linha.style.display = "";
    });
}

function aplicarFiltroDoador() 
{
    aplicarFiltro("tabela-doadores", {
        nome: 0,
        cpf: 1,
        profissao: 2,
        telefone: 3,
        cidade: 4
    });
}

function limparFiltroDoador() 
{
    limparFiltro("tabela-doadores");
}

function aplicarFiltroDoacao() 
{
    aplicarFiltro("tabela-doacoes", {
        tipo: 1,
        nome: 2,
        nomeDoador: 6
    });
}

function limparFiltroDoacao() 
{
    limparFiltro("tabela-doacoes");
}
