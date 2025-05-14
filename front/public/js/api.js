function salvarAnimal(event) {
  event.preventDefault();

  // Coleta os valores
  const nome = document.getElementById('nome').value;
  const idade = document.getElementById('idade').value;
  const tipo = document.getElementById('tipo').value;
  const sexo = document.getElementById('sexo').value;
  const porte = document.getElementById('porte').value
  const raca = document.getElementById('raca').value;
  const pelagem = document.getElementById('pelagem').value;
  const peso = document.getElementById('peso').value;
  const baia = document.getElementById('baia').value;
  const resgate = document.getElementById('resgate').value;
  const adocao = document.querySelector('input[name="adocao"]:checked').value;
  const foto = document.getElementById('foto').files[0];

  const formData = new FormData();
  formData.append('nome', nome);
  formData.append('idade', idade);
  formData.append('tipo', tipo);
  formData.append('porte',porte);
  formData.append('sexo', sexo);
  formData.append('raca', raca);
  formData.append('pelagem', pelagem);
  formData.append('peso', peso);
  formData.append('baia', baia);
  formData.append('adocao', adocao);
  // Tratamento especial para a data
  const resgateInput = document.getElementById('resgate').value;
  formData.append('resgate', resgateInput); // Já deve estar no formato yyyy-MM-dd
  // tratamento pata a foto em byte
  if (foto) formData.append('foto', foto);

  for (let [key, value] of formData.entries()) {
  console.log(key, value);
}
  fetch('http://localhost:8080/animal/cadastrar', {
  method: 'POST',
  body: formData
})
.then(async response => {
  if (!response.ok) {
    // Tenta ler o corpo da resposta como texto para ver a mensagem de erro
    const errorText = await response.text();
    throw new Error(`Erro ${response.status}: ${errorText}`);
  }
  return response.json();
})
.then(data => {
  console.log('Sucesso:', data);
  alert('Animal cadastrado com sucesso!');
  document.querySelector('form').reset();
})
.catch(error => {
  console.error('Erro completo:', error);
  alert(error.message); // Mostra a mensagem detalhada
});
}


async function filtrarAnimais({ nome, porte, tipo, sexo, status }) {
    const params = new URLSearchParams();

    if (nome) params.append('nome', nome);
    if (porte) params.append('porte', porte);
    if (tipo) params.append('tipo', tipo);
    if (sexo) params.append('sexo', sexo);
    if (status) params.append('status', status);

    try {
        const response = await fetch(`http://localhost:8080/animal/filtro?${params.toString()}`);
        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }

        const dados = await response.json();
        return dados;
    } catch (error) {
        console.error("Erro ao buscar animais:", error);
        return [];
    }
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
export { filtrarAnimais, salvarAnimalAPI };