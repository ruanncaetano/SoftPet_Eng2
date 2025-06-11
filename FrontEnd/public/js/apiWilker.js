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

document.addEventListener("DOMContentLoaded", () => {
  const animalSelect = document.getElementById("animal-id");
  const vacinaSelect = document.getElementById("vacina-id");
  const form = document.getElementById("formVacinacao");

  async function carregarAnimais() {
    try {
      const response = await fetch("http://localhost:8080/animal/filtro");
      const animais = await response.json();

      animais.forEach(animal => {
        const option = document.createElement("option");
        option.value = animal.aniCod;
        option.textContent = `${animal.nome} (ID: ${animal.aniCod})`;
        animalSelect.appendChild(option);
      });
    } catch (error) {
      console.error("Erro ao carregar animais:", error);
    }
  }

  async function carregarVacinas() {
    try {
      const response = await fetch("http://localhost:8080/api/vacinas/listar");
      const vacinas = await response.json();

      vacinas.forEach(vacina => {
        const option = document.createElement("option");
        option.value = vacina.vacCod;
        option.textContent = `${vacina.nome} (ID: ${vacina.vacCod})`;
        vacinaSelect.appendChild(option);
      });
    } catch (error) {
      console.error("Erro ao carregar vacinas:", error);
    }
  }

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const animalCod = animalSelect.value;
    const vacinaCod = vacinaSelect.value;
    const dataAplicacao = document.getElementById("data-aplicacao").value;
    const doseNumerica = document.getElementById("dose").value;
    const voluntarioAplicadorCod = 1; // fixo ou recuperado do login

    const payload = {
      animalCod: parseInt(animalCod),
      vacinaCod: parseInt(vacinaCod),
      voluntarioAplicadorCod,
      dataAplicacao,
      doseNumerica,
      observacao: null
    };

    try {
      const response = await fetch("http://localhost:8080/animal-x-vacinacao", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        const resultado = await response.json();
        alert("Vacinação registrada com sucesso! Código: " + resultado.axvCod);
        form.reset();
      } else {
        const erro = await response.text();
        alert("Erro ao agendar vacinação: " + erro);
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
      alert("Erro de conexão com o servidor.");
    }
  });

  carregarAnimais();
  carregarVacinas();
});
