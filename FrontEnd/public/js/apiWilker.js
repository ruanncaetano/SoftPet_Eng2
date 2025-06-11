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

/*

    lucide.createIcons();

    // Simulação de preenchimento com dados da API
    async function carregarDados() {
      const animaisSelect = document.getElementById('animal-cod');
      const vacinasSelect = document.getElementById('vacina-cod');

      // Substituir pelos endpoints reais da sua API
      const animais = await fetch('/api/animais').then(r => r.json());
      const vacinas = await fetch('/api/vacinas').then(r => r.json());

      animais.forEach(a => {
        animaisSelect.innerHTML += `<option value="${a.an_cod}">${a.an_nome}</option>`;
      });

      vacinas.forEach(v => {
        vacinasSelect.innerHTML += `<option value="${v.va_cod}" data-desc="${v.va_desc}" data-data="${v.va_dt_aplicacao}">${v.va_nome} - Dose ${v.va_dose}</option>`;
      });
    }

    // Preenche dados da vacina ao selecionar
    document.getElementById('vacina-cod').addEventListener('change', function () {
      const selected = this.selectedOptions[0];
      document.getElementById('descricao-vacina').value = selected.dataset.desc || '';
      document.getElementById('data-aplicacao').value = selected.dataset.data || '';
    });

    // Envio do formulário
    document.getElementById('formVacinaAnimal').addEventListener('submit', async function (e) {
      e.preventDefault();

      const dados = {
        animais_an_cod: document.getElementById('animal-cod').value,
        vacinas_va_cod: document.getElementById('vacina-cod').value,
        vac_dose: document.getElementById('dose-vacina').value
      };

      const resposta = await fetch('/api/animal-vacina', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
      });

      if (resposta.ok) {
        alert('Vacinação registrada com sucesso!');
        this.reset();
      } else {
        alert('Erro ao registrar vacinação.');
      }
    });

    carregarDados();

    document.addEventListener("DOMContentLoaded", () => {
  carregarAnimais();
  carregarVacinas();

  const form = document.getElementById("formVacinacao");
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const animalId = document.getElementById("animal-id").value;
    const vacinaId = document.getElementById("vacina-id").value;
    const dataAplicacao = document.getElementById("data-aplicacao").value;
    const dose = document.getElementById("dose").value.trim();

    if (!animalId || !vacinaId || !dataAplicacao || !dose) {
      alert("Preencha todos os campos obrigatórios!");
      return;
    }

    const vacinaPayload = {
      vaCod: vacinaId,
      vaDtAplicacao: dataAplicacao,
      vaDose: dose
    };

    try {
      // 1. Cadastrar a vacina
      const respVacina = await fetch("/api/vacinas", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(vacinaPayload)
      });

      if (!respVacina.ok) {
        throw new Error("Erro ao cadastrar vacina.");
      }

      const vacinaSalva = await respVacina.json();

      // 2. Relacionar com animal
      const relacao = {
        animaisAnCod: parseInt(animalId),
        vacinasVaCod: vacinaSalva.vaCod,
        vacDose: parseInt(dose)
      };

      const respRelacionamento = await fetch("/api/animal-vacina", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(relacao)
      });

      if (!respRelacionamento.ok) {
        throw new Error("Erro ao vincular vacina ao animal.");
      }

      alert("Vacinação agendada com sucesso!");
      form.reset();
    } catch (error) {
      console.error("Erro:", error);
      alert("Erro ao agendar vacinação.");
    }
  });
});

async function carregarAnimais() {
  const select = document.getElementById("animal-id");
  try {
    const response = await fetch("/api/animais");
    const animais = await response.json();

    animais.forEach((animal) => {
      const option = document.createElement("option");
      option.value = animal.anCod;
      option.textContent = `${animal.anNome} (ID: ${animal.anCod})`;
      select.appendChild(option);
    });
  } catch (error) {
    console.error("Erro ao carregar animais:", error);
  }
}

async function carregarVacinas() {
  const select = document.getElementById("vacina-id");
  try {
    const response = await fetch("/api/vacinas");
    const vacinas = await response.json();

    vacinas.forEach((vacina) => {
      const option = document.createElement("option");
      option.value = vacina.vaCod;
      option.textContent = `${vacina.vaNome}`;
      select.appendChild(option);
    });
  } catch (error) {
    console.error("Erro ao carregar vacinas:", error);
  }
}
*/

document.addEventListener("DOMContentLoaded", () => {
  const animalSelect = document.getElementById("animal-id");
  const vacinaSelect = document.getElementById("vacina-id");
  const form = document.getElementById("formVacinacao");

  // Função para carregar opções de animais
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

  // Função para carregar opções de vacinas
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

  // Evento de envio do formulário
  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const animalCod = animalSelect.value;
    const vacinaCod = vacinaSelect.value;
    const dataAplicacao = document.getElementById("data-aplicacao").value;
    const doseNumerica = document.getElementById("dose").value;

    // Ajuste se o voluntário for fixo ou derivado de login
    const voluntarioAplicadorCod = 1; // ou recupere de um usuário logado

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

  // Inicializa os selects
  carregarAnimais();
  carregarVacinas();
});
