function aplicarFiltro() 
{
    const campo = document.getElementById("campoFiltro").value;
    const valor = document.getElementById("valorFiltro").value.toLowerCase();
    const linhas = document.querySelectorAll("#tabela-doadores tr");

    linhas.forEach(linha => {
        const celulas = linha.querySelectorAll("td");
        let valorCampo = "";

        switch(campo) 
        {
            case "nome":
                valorCampo = celulas[0]?.textContent.toLowerCase();
                break;

            case "cpf":
                valorCampo = celulas[1]?.textContent.toLowerCase();
                break;

            case "profissao":
                valorCampo = celulas[2]?.textContent.toLowerCase();
                break;

            case "telefone":
                valorCampo = celulas[3]?.textContent.toLowerCase();
                break;

            case "cidade":
                valorCampo = celulas[4]?.textContent.toLowerCase();
                break;
        }

        if(valorCampo.includes(valor))
            linha.style.display = "";
        else
            linha.style.display = "none";
    });
}

function limparFiltro() 
{
    document.getElementById("valorFiltro").value = "";
    document.querySelectorAll("#tabela-doadores tr").forEach(linha => {
        linha.style.display = "";
    });
}