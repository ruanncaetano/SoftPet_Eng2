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
                    alert("Login realizado com sucesso!");
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





export async function atualizarDoador(doadorCompleto) 
{
    try
    {
        const cpf = doadorCompleto.doador.cpf;

        const response = await fetch(`http://localhost:8080/doador/${encodeURIComponent(cpf)}`, {
            method: 'PUT',
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
        return data;
    }
    catch(error)
    {
        throw error;
    }
    }
