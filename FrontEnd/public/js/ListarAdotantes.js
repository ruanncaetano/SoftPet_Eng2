document.addEventListener('DOMContentLoaded', () => {
    const tabela = document.getElementById('tabela-adotantes');

    fetch('http://localhost:8080/adotante/listar')
    .then(res => res.json())
    .then(lista => {
        if(!Array.isArray(lista))
            throw new Error('A resposta da API não é uma lista');

        lista.forEach(item => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
            <td class="px-4 py-2">${item.adotante?.nome || '-'}</td>
            <td class="px-4 py-2">${item.adotante?.cpf || '-'}</td>
            <td class="px-4 py-2">${item.adotante?.profissao || '-'}</td>
            <td class="px-4 py-2">${item.contato?.telefone || '-'}</td>
            <td class="px-4 py-2">${item.endereco?.cidade || '-'}</td>
            <td class="px-4 py-2 space-x-2">
                <button class="text-blue-600 hover:underline" onclick="editarAdotante('${item.adotante?.cpf}')">Editar</button>
                <button class="text-red-600 hover:underline" onclick="excluirAdotante('${item.adotante?.cpf}')">Excluir</button>
            </td>`;
            tabela.appendChild(tr);
      });
    })
    .catch(error => {
        console.error('Erro ao carregar adotante:', error);
        tabela.innerHTML = `
        <tr>
            <td colspan="6" class="px-4 py-2 text-center text-red-600">Erro ao carregar dados.</td>
        </tr>`;
    });
});

function editarAdotante(cpf) 
{
    window.location.href = `/views/viewGuilherme/alterarAdotante.html?cpf=${cpf}`;
}

function excluirAdotante(cpf) 
{
    if(confirm('Tem certeza que deseja excluir este adotante?')) 
    {
        fetch(`http://localhost:8080/adotante/${cpf}`, {
            method: 'DELETE'
        })
        .then(response => {
            if(response.ok) 
                location.reload(); 
            else 
                alert('Erro ao excluir adotante.');
        })
        .catch(err => {
            console.error(err);
            alert('Erro ao excluir adotante.');
        });
    }
}
