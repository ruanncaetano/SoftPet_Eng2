document.addEventListener('DOMContentLoaded', () => {
    const tabela = document.getElementById('tabela-doadores');

    fetch('http://localhost:8080/doador/listar')
    .then(res => res.json())
    .then(lista => {
        if(!Array.isArray(lista))
            throw new Error('A resposta da API não é uma lista');

        lista.forEach(item => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
            <td class="px-4 py-2">${item.pessoa?.nome || '-'}</td>
            <td class="px-4 py-2">${item.pessoa?.cpf || '-'}</td>
            <td class="px-4 py-2">${item.pessoa?.profissao || '-'}</td>
            <td class="px-4 py-2">${item.contato?.telefone || '-'}</td>
            <td class="px-4 py-2">${item.endereco?.cidade || '-'}</td>
            <td class="px-4 py-2 space-x-2">
                <button class="text-blue-600 hover:underline" onclick="editarDoador('${item.pessoa?.cpf}')">Editar</button>
                <button class="text-red-600 hover:underline" onclick="excluirDoador('${item.pessoa?.cpf}')">Excluir</button>
            </td>`;
            tabela.appendChild(tr);
      });
    })
    .catch(error => {
        console.error('Erro ao carregar doadores:', error);
        tabela.innerHTML = `
        <tr>
            <td colspan="6" class="px-4 py-2 text-center text-red-600">Erro ao carregar dados.</td>
        </tr>`;
    });
});

function editarDoador(cpf) 
{
    window.location.href = `/views/viewGuilherme/alterarDoador.html?cpf=${cpf}`;
}

function excluirDoador(cpf) 
{
    if(confirm('Tem certeza que deseja excluir este doador?')) 
    {
        fetch(`http://localhost:8080/doador/${cpf}`, {
            method: 'DELETE'
        })
        .then(response => {
            if(response.ok) 
                location.reload(); 
            else 
                alert('Erro ao excluir doador.');
        })
        .catch(err => {
            console.error(err);
            alert('Erro ao excluir doador.');
        });
    }
}
