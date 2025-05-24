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
  
  window.location.href = "../../views/view/login.html";
}


//conexao do front com o backend na pagina de adotante
document.getElementById('cadastroForm').addEventListener('submit', async function (event) {
  event.preventDefault();

    try 
    {
        const nome = document.getElementById('nome-adotante').value.trim().toUpperCase();
        const cpf = document.getElementById('cpf-adotante').value.trim();
        const rg = document.getElementById('rg-adotante').value.trim();
        const profissao = document.getElementById('profissao-adotante').value.trim();

        const telefone = document.getElementById('fone-adotante').value.trim();

        const cep = document.getElementById('cep-adotante').value.trim();
        const rua = document.getElementById('rua-adotante').value.trim();
        const numero = document.getElementById('numero-adotante').value.trim();
        const bairro = document.getElementById('bairro-adotante').value.trim();
        const cidade = document.getElementById('cidade-adotante').value.trim();
        const uf = document.getElementById('uf-adotante').value.trim();
        const complemento = document.getElementById('complemento-adotante').value.trim();

        //objeto para requisicao
        const adotanteCompleto = {
            adotante: {
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

        const response = await fetch('http://localhost:8080/adotante/cadastro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(adotanteCompleto)
        });

        if (!response.ok) {
        let errorMsg = `Erro ${response.status}`;
        try {
            const errorData = await response.json();
            errorMsg += `: ${errorData.message || JSON.stringify(errorData)}`;
        } catch {
            const errorText = await response.text();
            errorMsg += `: ${errorText}`;
        }
        throw new Error(errorMsg);
        }

});

//fazer a função para quando a pessoa clicar no botão procurar o adotante com aquele cpf
async function buscarAdotante() 
{
    const cpf = document.getElementById('buscaAdotanteCpf').value.trim();
    const erroEl = document.getElementById('erroCpfBusca');
    const dadosEl = document.getElementById('dadosAdotante');
    //---------------------------------------------------------------
    const nomeInput = document.getElementById('nomeBuscaDoacao');
    const cpfInput = document.getElementById('cpfBuscaDoacao');
    const idInput = document.getElementById('idAdotanteBusca');

    erroEl.textContent = '';
    dadosEl.classList.add('hidden');
    nomeInput.value = '';
    cpfInput.value = '';
    idInput.value = '';

    if(!cpf) 
    {
        erroEl.textContent = 'Por favor, informe o CPF do adotante.';
        return;
    }

    try 
    {
        const response = await fetch(`http://localhost:8080/adotante/${cpf}`);

        if(!response.ok)
            throw new Error('Adotante não encontrado.');

        const data = await response.json();
        nomeInput.value = data.adotante?.nome || '-';
        cpfInput.value = data.adotante?.cpf || '-';
        idInput.value = data.adotante?.id || '';

        dadosEl.classList.remove('hidden');

    }
    catch(error) 
    {
        erroEl.textContent = error.message;
    }
}



