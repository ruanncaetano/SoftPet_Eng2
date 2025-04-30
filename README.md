   <h1>SoftPet - Guia para Colaboradores</h1>
        <p>Este guia contém as instruções necessárias para você configurar seu repositório local e começar a colaborar no projeto <strong>SoftPet</strong>.</p>
        <h2>1. Clonando o repositório principal</h2>
        <p>Se você é um colaborador e já tem um repositório remoto configurado (no GitHub), basta clonar o repositório para sua máquina:</p>
        <pre><code>git clone git@github.com:ruanncaetano/SoftPet_Eng2.git</code></pre>
        <p>Isso irá criar uma cópia local do repositório remoto.</p>
        <h3>2. Entrando na pasta do repositório</h3>
        <p>Depois de clonar, entre na pasta do repositório com o comando:</p>
        <pre><code>cd SoftPet_Eng2</code></pre>
        <h2>3. Inicializando o repositório (se você não tem um repositório local)</h2>
        <p>Caso você não tenha o repositório local configurado, siga os passos abaixo:</p>
        <h3>1. Inicializar o repositório local</h3>
        <p>Dentro da pasta onde você deseja trabalhar, execute:</p>
        <pre><code>git init</code></pre>
        <h3>2. Conectar o repositório local ao repositório remoto</h3>
        <p>Após inicializar, você precisa conectar o repositório local ao repositório remoto. Execute:</p>
        <pre><code>git remote add origin git@github.com:ruanncaetano/SoftPet_Eng2.git</code></pre>
        <h2>4. Trabalhando com Branches</h2>
        <p>Agora que o repositório está configurado, é hora de criar uma branch para começar a trabalhar isolado da <code>main</code>.</p>
        <h3>1. Criar uma branch para o seu trabalho</h3>
        <p>Crie uma branch com o nome do seu projeto ou seu nome para manter suas alterações organizadas:</p>
        <pre><code>git checkout -b seu-nome</code></pre>
        <h3>2. Fazer alterações no seu projeto</h3>
        <p>Agora você pode modificar ou adicionar arquivos ao projeto.</p>
        <h3>3. Adicionar arquivos ao Git</h3>
        <p>Após modificar ou adicionar arquivos, você precisa adicioná-los ao controle do Git:</p>
        <pre><code>git add .</code></pre>
        <h3>4. Commitar suas alterações</h3>
        <p>Faça um commit com uma descrição do que foi alterado:</p>
        <pre><code>git commit -m "Descrição das alterações"</code></pre>
        <h3>5. Subir as alterações para o repositório remoto</h3>
        <p>Agora, envie suas alterações para o repositório remoto na branch que você criou:</p>
        <pre><code>git push origin seu-nome</code></pre>
        <h2>5. Criar um Pull Request (PR)</h2>
        <p>Após enviar suas alterações para o repositório remoto, vá até o GitHub e crie um Pull Request para integrar suas alterações à branch <code>main</code>.</p>
        <h2>6. Fluxo de Trabalho do Git</h2>
        <ul>
            <li>Crie uma nova <strong>branch</strong> antes de começar a trabalhar (<code>git checkout -b nome-da-sua-branch</code>).</li>
            <li><strong>Evite fazer alterações diretamente na branch <code>main</code>.</strong></li>
            <li><strong>Faça commits claros e significativos</strong> com mensagens que descrevem as mudanças feitas.</li>
            <li>Após fazer o <code>push</code>, crie um Pull Request no GitHub para revisar e integrar suas mudanças.</li>
        </ul>
        <h2>7. Contribuindo com o projeto</h2>
        <p>Se você deseja adicionar novas funcionalidades ou corrigir problemas, basta criar uma nova branch, fazer as alterações e submeter um Pull Request. Assim, sua contribuição será revisada.</p>
