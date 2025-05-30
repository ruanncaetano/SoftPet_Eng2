function validateLogin(event) {
    event.preventDefault()
    const email = document.getElementById("email").value
    const password = document.getElementById("password").value
    console.log({ email, password })

    if (email == "admin" && password == "admin") {

        fetch('http://localhost:8080/api/empresa/existe-param', { method: 'GET', })
            .then((response) => response.json())
            .then((result) => {
                const { existe } = result

                if (existe) {
                    window.location.href = "index.html"
                }
                else {
                    window.location.href = "form-param.html"
                }
            })
    }
}

function parametrizar(event) {
    event.preventDefault()

    const nome = document.getElementById("nome").value
    const razaoSocial = document.getElementById("razaoSocial").value
    const cnpj = document.getElementById("cnpj").value
    const logoPequena = document.getElementById("logoPequena").value
    const logoGrande = document.getElementById("logoGrande").value
    const endereco = document.getElementById("endereco").value
    const bairro = document.getElementById("bairro").value
    const cidade = document.getElementById("cidade").value
    const uf = document.getElementById("uf").value
    const diretor = document.getElementById("diretor").value
    const site = document.getElementById("site").value
    const telefone = document.getElementById("telefone").value

    const empresa = JSON.stringify({
        "nome": nome,
        "razaoSocial": razaoSocial,
        "cnpj": cnpj,
        "logoPequena": logoPequena,
        "logoGrande": logoGrande,
        "endereco": endereco,
        "bairro": bairro,
        "cidade": cidade,
        "uf": uf,
        "diretor": diretor,
        "site": site,
        "telefone": telefone,
    })

    const requestOptions = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: empresa,
        redirect: "follow"
    }

    fetch("http://localhost:8080/api/empresa/parametrizar", requestOptions)
        .then((response) => response.json())
        .then((result) => {
            window.location.href = "perfil.html"
        })
}