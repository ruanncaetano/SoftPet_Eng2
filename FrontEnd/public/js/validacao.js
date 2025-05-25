async function registrarProduto() {
  event.preventDefault(); // Evita o envio padrão do formulário

  // Captura os valores dos inputs
  const tipo = document.getElementById("tipoDoacao").value;
  const descricao = document.getElementById("itemDoacao").value;
  const quantidadeEstoque = parseInt(document.getElementById("qtdeDoacao").value);
  const dataValidade = document.getElementById("dataValidade").value;
  const unidadeMedida = document.getElementById("uniMedida").value;

  // Monta o objeto como esperado pelo backend (ProdutoDTO com produto dentro)
  const produtoDTO = {
    produto: {
      tipo: tipo,
      descricao: descricao,
      quantidadeEstoque: quantidadeEstoque,
      dataValidade: dataValidade,
      unidadeMedida: unidadeMedida
    }
  };

  try {
    const response = await fetch("http://localhost:8080/produtos/cadastro", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(produtoDTO)
    });

    if (response.ok) {
      alert("Produto registrado com sucesso!");
      document.getElementById("cadastroDoacao").reset(); // Limpa o formulário
    } else {
      const erro = await response.text();
      alert("Erro ao registrar produto: " + erro);
    }
  } catch (err) {
    console.error("Erro de rede:", err);
    alert("Erro ao conectar com o servidor.");
  }
}

async function consultarProdutos() {
  try {
    const response = await fetch("http://localhost:8080/produtos/listar"); // <-- ajustado aqui
    if (!response.ok) throw new Error("Erro na requisição");

    const produtos = await response.json();

    const corpoTabela = document.getElementById("tabelaProdutos");
    corpoTabela.innerHTML = ""; // limpa a tabela antes de preencher

    produtos.forEach(p => {
      const linha = document.createElement("tr");
      linha.innerHTML = `
        <td class="border px-4 py-2">${p.produto.tipo}</td>
        <td class="border px-4 py-2">${p.produto.descricao}</td>
        <td class="border px-4 py-2">${p.produto.quantidadeEstoque}</td>
        <td class="border px-4 py-2">${p.produto.dataValidade}</td>
        <td class="border px-4 py-2">${p.produto.unidadeMedida}</td>
      `;
      corpoTabela.appendChild(linha);
    });

  } catch (erro) {
    console.error("Erro ao consultar produtos:", erro);
    alert("Erro ao buscar produtos.");
  }
}



//função de formatação do cpf
function formatarCPF(cpf)
{
    cpf = cpf.replace(/\D/g, '');
    if(cpf.length > 11)
        cpf = cpf.slice(0, 11);
    cpf = cpf.replace(/(\d{3})(\d)/, '$1.$2');
    cpf = cpf.replace(/(\d{3})(\d)/, '$1.$2');
    cpf = cpf.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    return cpf;
}

//função que valida o cpf matematicamente
function validarCPF(cpf)
{
    cpf = cpf.replace(/[^\d]+/g, '');
    if(cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf))
        return false;

    var soma = 0;
    var i;
    for(i = 0; i < 9; i++)
        soma += parseInt(cpf.charAt(i)) * (10 - i);
    var digito1 = 11 - (soma % 11);
    if(digito1 >= 10) 
        digito1 = 0;
    if(digito1 !== parseInt(cpf.charAt(9))) 
        return false;

    soma = 0;
    for(i = 0; i < 10; i++)
        soma += parseInt(cpf.charAt(i)) * (11 - i);

    var digito2 = 11 - (soma % 11);
    if(digito2 >= 10)
        digito2 = 0;

    return digito2 === parseInt(cpf.charAt(10));
}

//função que aplica a validação de cpf
function aplicarValidacaoCPF(idInput, idErro)
{
    var cpf = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(cpf)
    {
        cpf.addEventListener('input', () => {
            cpf.value = formatarCPF(cpf.value);
        });

        cpf.addEventListener('blur', () => {
            if(!validarCPF(cpf.value))
            {
                erro.textContent = 'CPF inválido.';
                cpf.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                cpf.classList.remove('erro-input');
            }
        });
    }
}

//função que valida o email
function validarEmail(email)
{
    var valida1 = email.includes('@');
    var valida2 = email.endsWith('.com');
  
    if(!valida1 && !valida2) 
        return 'O e-mail deve conter "@" e terminar com ".com".';
    if(!valida1)
        return 'O e-mail deve conter "@".';
    if(!valida2)
        return 'O e-mail deve terminar com ".com".';
  
    return ''; //se estiver ok
}

//função que aplica a validação de email
function aplicarValidacaoEmail(idInput, idErro)
{
    var email = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(email && erro)
    {
        email.addEventListener('blur', function () {
            var mensagemErro = validarEmail(this.value);

            if (mensagemErro)
            {
                erro.textContent = mensagemErro;
                this.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                this.classList.remove('erro-input');
            }
        });
    }
}

//função que valida a senha
function validarSenha(senha)
{
    var maiuscula = /[A-Z]/.test(senha);
    var minuscula = /[a-z]/.test(senha);
    var numero = /[0-9]/.test(senha);
    var especial = /[!@#$%^&*(),.?":{}|<>]/.test(senha);
  
    var mensagens = [];

    if(!maiuscula)
        mensagens.push('letra maiúscula');
    if(!minuscula)
        mensagens.push('letra minúscula');
    if(!numero)
        mensagens.push('número');
    if(!especial)
        mensagens.push('caractere especial');
  
    if(mensagens.length > 0)
        return 'A senha deve conter: ' + mensagens.join(', ') + '.';

    return ''; //senha válida
}

//função que valida a senha
function aplicarValidacaoSenha(idInput, idErro)
{
    var input = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(input && erro)
    {
        input.addEventListener('blur', function () {
            var mensagemErro = validarSenha(this.value);

            if(mensagemErro)
            {
                erro.textContent = mensagemErro;
                this.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                this.classList.remove('erro-input');
            }
        });
    }
}

//função que valida a nome completo
function validarNomeCompleto(nome)
{
    if(nome)
    {
        var partes = nome.trim().split(/\s+/);

        return partes.length >= 2 && partes.every(parte => /^[A-Za-zÀ-ÿ]+$/.test(parte));
    }
    return false;
}

//função que aplica a validação do nome completo
function aplicarValidacaoNomeCompleto(idInput, idErro)
{
    var nome = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(nome)
    {
        nome.addEventListener('input', () => {
            nome.value = nome.value.replace(/[0-9]/g, '');
        });
        
        nome.addEventListener('blur', () => {
            if(!validarNomeCompleto(nome.value)) 
            {
                erro.textContent = 'Digite o nome completo (nome e sobrenome).';
                nome.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                nome.classList.remove('erro-input');
            }
        });
    }
}

//função de formatação do RG
function formatarRG(rg)
{
    rg = rg.replace(/\D/g, '');
    if(rg.length > 9)
        rg = rg.slice(0, 9);
    rg = rg.replace(/(\d{2})(\d)/, '$1.$2');
    rg = rg.replace(/(\d{3})(\d)/, '$1.$2');
    rg = rg.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    return rg;
}

//função que valida o RG
function validarRG(rg)
{
    rg = rg.replace(/\D/g, '');

    return rg.length === 9;
}

//função que aplica a validação do RG
function aplicarValidacaoRG(idInput, idErro)
{
    var rg = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(rg)
    {
        rg.addEventListener('input', () => {
            rg.value = formatarRG(rg.value);
        });

        rg.addEventListener('blur', () => {
            if(!validarRG(rg.value))
            {
                erro.textContent = 'RG inválido.';
                rg.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                rg.classList.remove('erro-input');
            }
        });
    }
}

//função de formatação de telefone
function formatarTelefone(telefone)
{
    telefone = telefone.replace(/\D/g, '');
    if(telefone.length <= 2)
        return '(' + telefone;
    if(telefone.length <= 6)
        return '(' + telefone.slice(0, 2) + ') ' + telefone.slice(2);

    return '(' + telefone.slice(0, 2) + ') ' + telefone.slice(2, 7) + '-' + telefone.slice(7, 11);
}

//função que valida o número de telefone
function validarTelefone(telefone)
{
    var regex = /^\(\d{2}\)\s\d{4,5}-\d{4}$/;

    return regex.test(telefone);
}

//função que aplica a validação de telefone
function aplicarValidacaoTelefone(idInput, idErro)
{
    var input = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(input)
    {
        input.addEventListener('input', () => {
            input.value = formatarTelefone(input.value);
        });

        input.addEventListener('blur', () => {
            if(!validarTelefone(input.value))
            {
                erro.textContent = 'Número de telefone inválido.';
                input.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                input.classList.remove('erro-input');
            }
        });
    }
}

//função de formatação do cep
function formatarCEP(cep)
{
    cep = cep.replace(/\D/g, '');
    if(cep.length <= 5)
        return cep;
    
    return cep.slice(0, 5) + '-' + cep.slice(5, 8);
}

//função que valida o CEP
function validarCEP(cep)
{
    var regex = /^\d{5}-\d{3}$/;

    return regex.test(cep);
}

//função que aplica a validação de CEP
function aplicarValidacaoCEP(idInput, idErro)
{
    var cep = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(cep)
    {
        cep.addEventListener('input', () => {
            cep.value = formatarCEP(cep.value);
        });

        cep.addEventListener('blur', () => {
            if(!validarCEP(cep.value))
            {
                erro.textContent = 'CEP inválido.';
                cep.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                cep.classList.remove('erro-input');
            }
        });
    }
}

function validarQuantidade(idInput, idErro) 
{
    const quantidade = document.getElementById(idInput);
    const erro = document.getElementById(idErro);

    if(quantidade) 
    {
        quantidade.addEventListener('input', () => {
        if(quantidade.value < 1)
            quantidade.value = '';
        });

        quantidade.addEventListener('blur', () => {
            const valor = Number(quantidade.value);

            if(isNaN(valor) || valor < 1) 
            {
                erro.textContent = 'Quantidade inválida. Deve ser maior ou igual a 1.';
                quantidade.classList.add('erro-input');
            } 
            else 
            {
                erro.textContent = '';
                quantidade.classList.remove('erro-input');
            }
        });
    }
}

function aplicarValidacaoDataValidade(idInput, idErro) 
{
    const input = document.getElementById(idInput);
    const erro = document.getElementById(idErro);
    var interagiu = false;

    function validarData() 
    {
        const valor = input.value;
        const dataSelecionada = new Date(valor);
        const hoje = new Date();

        hoje.setHours(0, 0, 0, 0);
        dataSelecionada.setHours(0, 0, 0, 0);

        if(interagiu) 
        {
            if(!valor || dataSelecionada <= hoje)
                erro.textContent = "A data de validade deve ser maior que a data atual.";
            else
                erro.textContent = "";
        }
    }
    input.addEventListener('input', () => {
        interagiu = true;
        validarData();
    });
}


document.addEventListener('DOMContentLoaded', () => {
    //chamando validação de CPF
    aplicarValidacaoCPF('cpf', 'erroCpf'); //tela de login
    aplicarValidacaoCPF('cpf-doador', 'erroCpfDoador'); //tela de doador
    aplicarValidacaoCPF('cpf-doadorPOP', 'erroCpfDoadorPOP');
    aplicarValidacaoCPF('cpf-busca', 'erroCpfAtualizar');
    aplicarValidacaoCPF('buscaDoadorCpf', 'erroCpfBusca');

    //chamando validação de senha
    aplicarValidacaoSenha('senha', 'erroSenha');
    aplicarValidacaoSenha('senhaLogin', 'erroSenhaLogin');

    //chamando validação de email
    aplicarValidacaoEmail('email', 'erroEmail');

    //chamando validação de nome completo
    aplicarValidacaoNomeCompleto('nome-doador', 'erroNomeDoador');
    aplicarValidacaoNomeCompleto('nome-doadorPOP', 'erroNomeDoadorPOP');
    aplicarValidacaoNomeCompleto('nomeAtt', 'erroNomeDoador');

    //chamando a validação do RG
    aplicarValidacaoRG('rg-doador', 'erroRgDoador');
    aplicarValidacaoRG('rg-doadorPOP', 'erroRgDoadorPOP');
    aplicarValidacaoRG('rgAtt', 'erroRgDoador');

    //chamando a validação do numero de telefone
    aplicarValidacaoTelefone('fone-doador', 'erroFoneDoador');
    aplicarValidacaoTelefone('fone-doadorPOP', 'erroFoneDoadorPOP');
    aplicarValidacaoTelefone('telefoneAtt', 'erroFoneDoador');

    //chamando a validação do CEP
    aplicarValidacaoCEP('cep-doador', 'erroCepDoador');
    aplicarValidacaoCEP('cep-doadorPOP', 'erroCepDoadorPOP');
    aplicarValidacaoCEP('cepAtt', 'erroCepDoador');

    //chamando a validação de quantiade
    validarQuantidade('qtdeDoacao', 'erroQtdeDoacao');

    //chamando a validação da data de validade
    aplicarValidacaoDataValidade('dataValidade', 'erroDataValidade');
});