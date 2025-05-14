document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form");

    form.addEventListener("submit", (event) => {
        event.preventDefault(); // Evita o envio padrão do formulário

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

            //limpando mensagens antigas
            document.getElementById("erroCpf").textContent = "";
            document.getElementById("erroSenha").textContent = "";

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
            {
            
                if(texto.includes("CPF"))
                    document.getElementById("erroCpf").textContent = texto;
                else
                    document.getElementById("erroSenha").textContent = texto;
            }
        })
        .catch((err) => {
                console.error("Erro ao conectar com o backend:", err);
                alert("Erro ao conectar com o servidor.");
        });
    });
});
