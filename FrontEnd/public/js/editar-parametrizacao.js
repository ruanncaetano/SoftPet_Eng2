function formatarCnpj(cnpj) {
    return cnpj.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5');
}

function formatarData(data) {
    const dataObj = new Date(data);
    const dia = String(dataObj.getDate()).padStart(2, '0');
    const mes = String(dataObj.getMonth() + 1).padStart(2, '0'); // Os meses são indexados de 0 a 11
    const ano = dataObj.getFullYear();
    return `${dia}/${mes}/${ano}`;
}

document.addEventListener("DOMContentLoaded", () => {
    try {
        fetch("http://localhost:8080/empresa/get/profile", {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then((response) => response.json())
            .then((result) => {
                const empresaProfileSection = document.getElementById("empresaProfile")
                empresaProfileSection.innerHTML = /*html*/`
                    <div class="bg-white shadow-md rounded-lg p-6 max-w-sm mx-auto">
                        <img src="${result.logoPequena}" class="w-24 h-24 mx-auto mb-4 rounded-md" alt="Logo da Empresa">
                        <h2 class="text-xl font-semibold mb-2">${result.nome}</h2>
                        <p class="text-sm mb-2"><strong>Razão Social:</strong> ${result.razaoSocial}</p>
                        <p class="text-sm mb-2"><strong>CNPJ:</strong> ${formatarCnpj(result.cnpj)}</p>
                        <p class="text-sm mb-2"><strong>Endereço:</strong> ${result.endereco}</p>
                        <p class="text-sm mb-2"><strong>Bairro:</strong> ${result.bairro}</p>
                        <p class="text-sm mb-2"><strong>Cidade:</strong> ${result.cidade}</p>
                        <p class="text-sm mb-2"><strong>UF:</strong> ${result.uf}</p>
                        <p class="text-sm mb-2"><strong>Diretor:</strong> ${result.diretor}</p>
                        <p class="text-sm mb-2"><strong>Telefone:</strong> ${result.telefone}</p>
                        <p class="text-sm mb-2"><strong>Site:</strong> ${result.site}</p>
                        <div class="flex justify-center">
                            <button onclick="editarParametrizacao(${result.id})" class="bg-red-500 text-white py-2 px-2 rounded-lg hover:bg-red-600 focus:outline-none focus:bg-red-600 mt-4 transition duration-300">
                                Editar Parametrização
                            </button>
                        </div>
                    </div>
                `
            })
    } catch (error) {
        console.error("Erro ao buscar o perfil da empresa:", error)
    }
})

let empresaId = null;

function editarParametrizacao(id) {
    empresaId = id;

    fetch(`http://localhost:8080/empresa/get/${id}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then((response) => response.json())
        .then((result) => {
            const empresaProfileSection = document.getElementById("empresaProfile")
            empresaProfileSection.innerHTML = /*html*/``

            empresaProfileSection.innerHTML = /*html*/`
                <form onsubmit="atualizarParametrizacao(event)" id="param">
                    <div class="mb-4">
                        <label for="nome" class="block text-gray-700">Nome:</label>
                        <input type="text" id="nome" name="nome" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.nome}">
                    </div>
                    <div class="mb-4">
                        <label for="razaoSocial" class="block text-gray-700">Razão Social:</label>
                        <input type="text" id="razaoSocial" name="razaoSocial" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.razaoSocial}">
                    </div>
                    <div class="mb-4">
                        <label for="cnpj" class="block text-gray-700">CNPJ:</label>
                        <input type="text" id="cnpj" name="cnpj" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.cnpj}">
                    </div>
                    <div class="mb-4">
                        <label for="logoPequena" class="block text-gray-700">Logo Pequena:</label>
                        <input type="text" id="logoPequena" name="logoPequena" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.logoPequena}">
                    </div>
                    <div class="mb-4">
                        <label for="endereco" class="block text-gray-700">Endereço:</label>
                        <input type="text" id="endereco" name="endereco" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.endereco}">
                    </div>
                    <div class="mb-4">
                        <label for="bairro" class="block text-gray-700">Bairro:</label>
                        <input type="text" id="bairro" name="bairro" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.bairro}">
                    </div>
                    <div class="mb-4">
                        <label for="cidade" class="block text-gray-700">Cidade:</label>
                        <input type="text" id="cidade" name="cidade" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.cidade}">
                    </div>
                    <div class="mb-4">
                        <label for="uf" class="block text-gray-700">UF:</label>
                        <input type="text" id="uf" name="uf" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.uf}">
                    </div>
                    <div class="mb-4">
                        <label for="diretor" class="block text-gray-700">Diretor:</label>
                        <input type="text" id="diretor" name="diretor" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.diretor}"diretor>
                    </div>
                    <div class="mb-4">
                        <label for="site" class="block text-gray-700">Site:</label>
                        <input type="text" id="site" name="site" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.site}">
                    </div>
                    <div class="mb-6">
                        <label for="telefone" class="block text-gray-700">Telefone:</label>
                        <input type="text" id="telefone" name="telefone" required
                            class="w-full px-3 py-2 rounded-lg border-gray-300 focus:border-blue-500 focus:outline-none"
                            value="${result.telefone}">
                    </div>
                    <button type="button"
                        onclick="cancelar()"
                        class="bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-red-600 focus:outline-none focus:bg-red-600">
                        Calcelar
                    </button>
                    <button type="submit"
                        class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 focus:outline-none focus:bg-blue-600">
                        Salvar
                    </button>
                </form>
            `
        })

}

function cancelar() {
    window.location.href = "perfil.html"
}

function atualizarParametrizacao(event) {
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const razaoSocial = document.getElementById("razaoSocial").value;
    const cnpj = document.getElementById("cnpj").value;
    const logoPequena = document.getElementById("logoPequena").value;
    const endereco = document.getElementById("endereco").value;
    const bairro = document.getElementById("bairro").value;
    const cidade = document.getElementById("cidade").value;
    const uf = document.getElementById("uf").value;
    const diretor = document.getElementById("diretor").value;
    const site = document.getElementById("site").value;
    const telefone = document.getElementById("telefone").value;

    const empresa = JSON.stringify({
        id: empresaId, // garantir que está vindo da variável global
        nome,
        razaoSocial,
        cnpj,
        logoPequena,
        endereco,
        bairro,
        cidade,
        uf,
        diretor,
        site,
        telefone,
    });

    fetch(`http://localhost:8080/empresa/atualizar/${empresaId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: empresa,
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro na resposta do servidor");
            }
            return response.text(); // usar text() ao invés de json()
        })
        .then(() => {
            alert("Dados alterados com sucesso!");
            window.location.href = "perfil.html";
        })
        .catch(error => {
            console.error("Erro ao atualizar empresa:", error);
            alert("Erro ao atualizar. Verifique os dados e tente novamente.");
        });
}



