document.getElementById('cadastroFormAdotante').addEventListener('submit', async function (event) {
  event.preventDefault();

  try {
    const nome = document.getElementById('nome-adotante').value.trim().toUpperCase();
    const cpf = document.getElementById('cpf-adotante').value.trim();
    const rg = document.getElementById('rg-adotante').value.trim();
    const profissao = document.getElementById('profissao-adotante').value.trim();

    const telefone = document.getElementById('fone-adotante').value.trim();
    const email = document.getElementById('email-adotante').value.trim();

    const cep = document.getElementById('cep-adotante').value.trim();
    const rua = document.getElementById('rua-adotante').value.trim();
    const numero = document.getElementById('numero-adotante').value.trim();
    const bairro = document.getElementById('bairro-adotante').value.trim();
    const cidade = document.getElementById('cidade-adotante').value.trim();
    const uf = document.getElementById('uf-adotante').value.trim();
    const complemento = document.getElementById('complemento-adotante').value.trim();

    const adotanteCompleto = {
      pessoa: {
        cpf: cpf,
        nome: nome,
        profissao: profissao,
        rg: rg
      },
      contato: {
        telefone: telefone,
        email: email
      },
      endereco: {
        cep: cep,
        rua: rua,
        numero: numero,
        bairro: bairro,
        cidade: cidade,
        uf: uf,
        complemento: complemento
      }
    };

    const response = await fetch('http://localhost:8080/doador/cadastro', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(adotanteCompleto)
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Erro ${response.status}: ${errorText}`);
    }

    const data = await response.json();
    console.log('Sucesso:', data);
    alert('Adotante cadastrado com sucesso!');
    document.getElementById('cadastroFormAdotante').reset();

  } catch (error) {
    console.error('Erro ao cadastrar adotante:', error);
    alert(`Erro: ${error.message}`);
  }
});
