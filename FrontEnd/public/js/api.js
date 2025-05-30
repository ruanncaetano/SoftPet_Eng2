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
