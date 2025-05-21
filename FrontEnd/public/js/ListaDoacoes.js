document.addEventListener('DOMContentLoaded', () => {
    const tabela = document.getElementById('tabela-doacoes');

    fetch('http://localhost:8080/doacao/listar')
        .then(res => res.json())
        .then(lista => {
            if(!Array.isArray(lista))
                throw new Error('A resposta da API não é uma lista');

            if(lista.length === 0) 
            {
                tabela.innerHTML = `
                <tr>
                    <td colspan="9" class="px-4 py-2 text-center text-gray-500">Nenhuma doação encontrada.</td>
                </tr>`;
                return;
            }

            lista.forEach(item => {
                const doacao = item.doacao;
                const doador = item.doador;

                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td class="hidden">${doacao.id || '-'}</td>
                    <td class="px-4 py-2">${doacao.tipo || '-'}</td>
                    <td class="px-4 py-2">${doacao.nome || '-'}</td>
                    <td class="px-4 py-2">${doacao.qtde || '-'}</td>
                    <td class="px-4 py-2">${doacao.uniMedida || '-'}</td>
                    <td class="px-4 py-2">${formatarData(doacao.data)}</td>
                    <td class="px-4 py-2">${doador?.nome || '-'}</td>
                    <td class="px-4 py-2 space-x-2">
                        <button class="text-blue-600 hover:underline" onclick="editarDoacao(${doacao.id})">Editar</button>
                        <button class="text-red-600 hover:underline" onclick="excluirDoacao(${doacao.id})">Excluir</button>
                    </td>
                `;
                tabela.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar doações:', error);
            tabela.innerHTML = `
            <tr>
                <td colspan="9" class="px-4 py-2 text-center text-red-600">Erro ao carregar dados.</td>
            </tr>`;
        });
});

function formatarData(data) 
{
    if(!data)
        return '-';
    const [ano, mes, dia] = data.split("-");
    return `${dia}/${mes}/${ano}`;
}


function editarDoacao(id) 
{
    window.location.href = `/views/viewGuilherme/alterarDoacao.html?id=${id}`;
}

function excluirDoacao(id) 
{
    if(confirm("Tem certeza que deseja excluir esta doação?")) {
        fetch(`http://localhost:8080/doacao/${id}`, {
            method: 'DELETE'
        })
        .then(res => {
            if(!res.ok) 
                throw new Error("Erro ao excluir");
            alert("Doação excluída com sucesso!");
            location.reload();
        })
        .catch(error => {
            console.error("Erro ao excluir doação:", error);
            alert("Erro ao excluir doação.");
        });
    }
}