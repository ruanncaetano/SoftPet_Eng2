document.addEventListener('DOMContentLoaded', () => {
  const urlParams = new URLSearchParams(window.location.search);
  const cpf = urlParams.get('cpf');

  if (!cpf) {
    alert('CPF não fornecido!');
    return;
  }

  fetch(`http://localhost:8080/doador/${cpf}`)
    .then(res => res.json())
    .then(data => {
      document.getElementById('nomeAtt').value = (data.pessoa?.nome || '').toLocaleUpperCase();
      document.getElementById('cpfAtt').value = data.pessoa?.cpf || '';
      document.getElementById('profissaoAtt').value = data.pessoa?.profissao || '';
      document.getElementById('telefoneAtt').value = data.contato?.telefone || '';
      document.getElementById('emailAtt').value = data.contato?.email || '';
      document.getElementById('cidadeAtt').value = data.endereco?.cidade || '';
      document.getElementById('rgAtt').value = data.pessoa?.rg || '';
      document.getElementById('cepAtt').value = data.endereco?.cep || '';
      document.getElementById('ruaAtt').value = data.endereco?.rua || '';
      document.getElementById('numeroAtt').value = data.endereco?.numero || '';
      document.getElementById('bairroAtt').value = data.endereco?.bairro || '';
      document.getElementById('complementoAtt').value = data.endereco?.complemento || '';
      document.getElementById('ufAtt').value = data.endereco?.uf || '';
    })
    .catch(err => {
      console.error(err);
      alert('Erro ao carregar dados do adotante.');
    });

  document.getElementById('formAtualizarAdotante').addEventListener('submit', async function (event) {
    event.preventDefault();

    try {
      const adotanteCompleto = {
        pessoa: {
          cpf: document.getElementById('cpfAtt').value.trim(),
          nome: document.getElementById('nomeAtt').value.trim().toUpperCase(),
          profissao: document.getElementById('profissaoAtt').value.trim(),
          rg: document.getElementById('rgAtt').value.trim()
        },
        contato: {
          telefone: document.getElementById('telefoneAtt').value.trim(),
          email: document.getElementById('emailAtt').value.trim()
        },
        endereco: {
          cep: document.getElementById('cepAtt').value.trim(),
          rua: document.getElementById('ruaAtt').value.trim(),
          numero: document.getElementById('numeroAtt').value.trim(),
          bairro: document.getElementById('bairroAtt').value.trim(),
          cidade: document.getElementById('cidadeAtt').value.trim(),
          uf: document.getElementById('ufAtt').value.trim(),
          complemento: document.getElementById('complementoAtt').value.trim()
        }
      };

      const response = await fetch('http://localhost:8080/doador/alterar', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(adotanteCompleto)
      });

      if (response.ok) {
        alert('Adotante atualizado com sucesso!');
        window.location.href = '../../views/viewWilker/listarAdotantes.html';
      } else {
        const errorText = await response.text();
        alert(errorText);
        console.error('Erro do servidor:', errorText);
      }
    } catch (error) {
      console.error('Erro geral:', error);
      alert(`Erro ao atualizar adotante: ${error.message}`);
    }
  });
});
