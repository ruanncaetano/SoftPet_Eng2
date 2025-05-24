document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form");

    form.addEventListener("submit", (event) => {
        event.preventDefault();

        const cpf = document.getElementById("cpf").value;
        const senha = document.getElementById("senha").value;

        fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ cpf, senha }),
        })
        .then(async (res) => {
            const texto = await res.text();
            if(res.ok)
            {
                try
                {
                    const json = JSON.parse(texto);
                    localStorage.setItem("token", json.token);
                    //alert("Login realizado com sucesso!");
                    window.location.href = "../../views/viewGeral/home.html";
                }
                catch
                {
                    alert("Login bem-sucedido, mas resposta inválida.");
                }
            }
            else
                alert("Credenciais inválidas!");
        })
        .catch((err) => {
                console.error("Erro ao conectar com o backend:", err);
                alert("Erro ao conectar com o servidor.");
        });
    });
});

function logout() 
{
  localStorage.removeItem("token");
  localStorage.removeItem("usuario");
  
  window.location.href = "../../views/viewGuilherme/login.html";
}


//conexao do front com o backend na pagina de doador
document.getElementById('cadastroForm').addEventListener('submit', async function (event) {
  event.preventDefault();

    try 
    {
        const nome = document.getElementById('nome-doador').value.trim().toUpperCase();
        const cpf = document.getElementById('cpf-doador').value.trim();
        const rg = document.getElementById('rg-doador').value.trim();
        const profissao = document.getElementById('profissao-doador').value.trim();

        const telefone = document.getElementById('fone-doador').value.trim();

        const cep = document.getElementById('cep-doador').value.trim();
        const rua = document.getElementById('rua-doador').value.trim();
        const numero = document.getElementById('numero-doador').value.trim();
        const bairro = document.getElementById('bairro-doador').value.trim();
        const cidade = document.getElementById('cidade-doador').value.trim();
        const uf = document.getElementById('uf-doador').value.trim();
        const complemento = document.getElementById('complemento-doador').value.trim();

        //objeto para requisicao
        const doadorCompleto = {
            doador: {
                cpf: cpf,
                nome: nome,
                profissao: profissao,
                rg: rg
            },
            contato: {
                telefone: telefone
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
        body: JSON.stringify(doadorCompleto)
        });

        if(!response.ok) 
        {
            const errorText = await response.text();
            throw new Error(`Erro ${response.status}: ${errorText}`);
        }

        const data = await response.json();
        console.log('Sucesso:', data);
        alert('Doador cadastrado com sucesso!');
        document.getElementById('cadastroForm').reset();

    } 
    catch(error) 
    {
        console.error('Erro ao cadastrar doador:',error);
        alert(`Erro: ${error.message}`);
    }
});

//fazer a função para quando a pessoa clicar no botão procurar o doador com aquele cpf
async function buscarDoador() 
{
    const cpf = document.getElementById('buscaDoadorCpf').value.trim();
    const erroEl = document.getElementById('erroCpfBusca');
    const dadosEl = document.getElementById('dadosDoador');
    const nomeInput = document.getElementById('nomeBuscaDoacao');
    const cpfInput = document.getElementById('cpfBuscaDoacao');
    const idInput = document.getElementById('idDoadorBusca');

    erroEl.textContent = '';
    dadosEl.classList.add('hidden');
    nomeInput.value = '';
    cpfInput.value = '';
    idInput.value = '';

    if(!cpf) 
    {
        erroEl.textContent = 'Por favor, informe o CPF do doador.';
        return;
    }

    try 
    {
        const response = await fetch(`http://localhost:8080/doador/${cpf}`);

        if(!response.ok)
            throw new Error('Doador não encontrado.');

        const data = await response.json();
        nomeInput.value = data.doador?.nome || '-';
        cpfInput.value = data.doador?.cpf || '-';
        idInput.value = data.doador?.id || '';

        dadosEl.classList.remove('hidden');

    }
    catch(error) 
    {
        erroEl.textContent = error.message;
    }
}

//função que salva uma doação
async function registrarDoacao() 
{
    const idDoador = document.getElementById('idDoadorBusca').value;
    if(!idDoador) 
    {
        alert('Por favor, busque e selecione um doador antes de registrar a doação.');
        return;
    }

    const tipo = document.getElementById('tipoDoacao').value.toUpperCase();
    const nome = document.getElementById('itemDoacao').value.toUpperCase();
    const qtde = parseInt(document.getElementById('qtdeDoacao').value);
    const dataDoacao = document.getElementById('dataDoacao').value;
    const dataValidade = document.getElementById('dataValidade').value;
    const uniMedida = document.getElementById('uniMedida').value.toUpperCase();

    if(!tipo || !nome || isNaN(qtde) || !dataDoacao || !dataValidade || !uniMedida) 
    {
        alert('Preencha todos os campos obrigatórios corretamente.');
        return;
    }

    const doacao = {
        tipo,
        qtde,
        nome,
        data: dataDoacao,
        dataValidade,
        uniMedida
    };
    const payload = {
        doacao,
        doador: {
            id: parseInt(idDoador)
        }
    };

    try 
    {
        const response = await fetch('http://localhost:8080/doacao/cadastro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
            body: JSON.stringify(payload)
        });

        if(!response.ok)
            throw new Error('Erro ao salvar a doação.');

        alert('Doação registrada com sucesso!');
        document.getElementById('cadastroDoacao').reset();

    }
    catch(error) 
    {
        alert(error.message);
    }
}

