//função que formata o cpf em tempo real
function aplicarMascaraCPF(inputElement)
{
    inputElement.addEventListener('input', () => {
        let cpf = inputElement.value.replace(/\D/g, '');
        if(cpf.length > 11)
            cpf = cpf.slice(0, 11);
  
        if(cpf.length <= 11)
        {
            cpf = cpf.replace(/(\d{3})(\d)/, '$1.$2');
            cpf = cpf.replace(/(\d{3})(\d)/, '$1.$2');
            cpf = cpf.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        }
        inputElement.value = cpf;
    });
}

//chamando a função que formata o cpf
document.addEventListener('DOMContentLoaded', () => {
    var cpfInput = document.getElementById('cpf');
    if(cpfInput)
        aplicarMascaraCPF(cpfInput);
});

//função que valida se o cpf é valido matematicamente
function validarCPF(cpf)
{
    cpf = cpf.replace(/[^\d]+/g, '');
  
    if(cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf))
        return false;
  
    var soma = 0;
    var i;
    for(i = 0; i < 9; i++)
        soma += parseInt(cpf.charAt(i)) * (10 - i);
  
    let digito1 = 11 - (soma % 11);
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

//chamando a função que valida o cpf
document.getElementById('cpf').addEventListener('blur', function () {
    var cpf = this.value;
    var erro = document.getElementById('erroCpf');
  
    if(!validarCPF(cpf))
    {
        erro.textContent = 'CPF inválido.';
        this.classList.add('erro-input');
    }
    else
    {
        erro.textContent = '';
        this.classList.remove('erro-input');
    }
});

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

//chamando a função de validação de email
document.getElementById('email').addEventListener('blur', function () {
    var email = this.value;
    var erro = document.getElementById('erroEmail');
    var mensagemErro = validarEmail(email);
  
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
  
    return ''; //se estiver ok
}

//chamando a função de validação da senha
document.getElementById('senha').addEventListener('blur', function () {
    var senha = this.value;
    var erro = document.getElementById('erroSenha');
    var mensagemErro = validarSenha(senha);
  
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
  