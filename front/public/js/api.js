async function salvarAnimal(event) {
  event.preventDefault();

  try {
    const nome = document.getElementById('nome').value.toUpperCase();
    const idade = document.getElementById('idade').value;
    const tipo = document.getElementById('tipo').value;
    const sexo = document.getElementById('sexo').value;
    const porte = document.getElementById('porte').value;
    const raca = document.getElementById('raca').value.toUpperCase();
    const pelagem = document.getElementById('pelagem').value.toUpperCase();
    const peso = document.getElementById('peso').value;
    const baia = document.getElementById('baia').value;
    const resgate = document.getElementById('resgate').value;
    const adocao = document.querySelector('input[name="adocao"]:checked').value;
    const foto = document.getElementById('foto').files[0];
alert(peso)
    const formData = new FormData();
    formData.append('nome', nome);
    formData.append('idade', idade);
    formData.append('tipo', tipo);
    formData.append('porte', porte);
    formData.append('sexo', sexo);
    formData.append('raca', raca);
    formData.append('pelagem', pelagem);
    formData.append('peso', peso);
    formData.append('baia', baia);
    formData.append('resgate', resgate);
    formData.append('adocao', adocao);

    if (foto) {
      formData.append('foto', foto); // Apenas o arquivo, sem base64
    }

    const response = await fetch('http://localhost:8080/animal/cadastrar', {
      method: 'POST',
      body: formData
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Erro ${response.status}: ${errorText}`);
    }

    const data = await response.json();
    console.log('Sucesso:', data);
    alert('Animal cadastrado com sucesso!');
    document.querySelector('form').reset();

  } catch (error) {
    console.error('Erro completo:', error);
    alert(`Erro: ${error.message}`);
  }
}

function teste()
{
    alert("chamou");
}



// Função para salvar um animal (pode ser criação ou atualização)
async function salvarAnimalAPI(animal, id = null) {
    try {
        const url = id ? `/api/animais/${id}` : '/api/animais';
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(animal)
        });

        if (!response.ok) {
            throw new Error(`Erro ao salvar animal: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Erro ao salvar animal:", error);
        throw error;
    }
}

// Exporta as funções para uso em outros arquivos
