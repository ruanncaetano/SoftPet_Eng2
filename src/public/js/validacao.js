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
    var input = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(input)
    {
        input.addEventListener('input', () => {
            input.value = formatarCPF(input.value);
        });

        input.addEventListener('blur', () => {
            if(!validarCPF(input.value))
            {
                erro.textContent = 'CPF inválido.';
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

//função que valida a nome completo
function aplicarValidacaoNomeCompleto(idInput, idErro)
{
    var nome = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(nome)
    {
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

document.addEventListener('DOMContentLoaded', () => {
    //chamando validação de CPF
    aplicarValidacaoCPF('cpf', 'erroCpf'); // tela de login
    aplicarValidacaoCPF('cpf-doador', 'erroCpfDoador'); // tela de doador

    //chamando validação de senha
    aplicarValidacaoSenha('senha', 'erroSenha');
    aplicarValidacaoSenha('senhaLogin', 'erroSenhaLogin');

    //chamando validação de email
    aplicarValidacaoEmail('email', 'erroEmail');

    //chamando validação de nome completo
    aplicarValidacaoNomeCompleto('nome-doador', 'erroNomeDoador');
});