// menu.js
document.write(`
    <aside class="w-72 bg-noturno text-neve flex flex-col shadow-xl">
      <div class="p-6 border-b border-areia flex items-center gap-3">
        <svg data-lucide="paw-print" class="w-6 h-6 text-pastel"></svg>
        <a href="../../views/viewGeral/home.html" class="text-2xl font-bold tracking-wide text-pastel">ADAPV</a>
      </div>
      <nav class="p-6 flex-1 overflow-y-auto space-y-4">

        <!-- Dropdown Animais -->
      <div>
        <button onclick="toggleMenu('menu-animais', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
          <span class="flex gap-2 items-center"><svg data-lucide="dog" class="w-5 h-5"></svg>Animais</span>
          <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
        </button>
        <ul id="menu-animais" class="ml-6 mt-2 space-y-2 hidden">
          <li><a href="../../views/viewRuan/cadastrarAnimal.html" class="block hover:text-carmesim">Cadastrar</a></li>
          <li><a href="../../views/viewRuan/consultaAnimal.html" class="block hover:text-carmesim">Consultar</a></li>
        </ul>
      </div>
      <div>
      <button onclick="toggleMenu('menu-produtos', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
        <span class="flex gap-2 items-center"><svg data-lucide="package" class="w-5 h-5"></svg>Produtos</span>
        <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
      </button>
      <ul id="menu-produtos" class="ml-6 mt-2 space-y-2 hidden">
        <li><a href="../../views/viewLuan/RegistrarProduto.html" class="block hover:text-carmesim">Cadastrar</a></li>
        <li><a href="../../views/viewLuan/ConsultarProduto.html" class="block hover:text-carmesim">Consultar</a></li>
        <li><a href="../../views/viewLuan/AlterarProduto.html" class="block hover:text-carmesim">Alterar</a></li>
        <li><a href="../../views/viewLuan/ExcluirProduto.html" class="block hover:text-carmesim">Excluir</a></li>
        <li><a href="../../views/viewLuan/BanirAdotante.html" class="block hover:text-carmesim">Banir adotante</a></li>
      </ul>
    </div>

    <!-- Dropdown adoção -->
      <div>
        <button onclick="toggleMenu('menu-adocao', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
          <span class="flex gap-2 items-center"><svg data-lucide="heart-handshake" class="w-5 h-5"></svg>Adoção</span>
          <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
        </button>
        <ul id="menu-adocao" class="ml-6 mt-2 space-y-2 hidden">
          <li><a href="../../views/viewRuan/adocao.html" class="block hover:text-carmesim">Nova Adoção</a></li>
          <li><a href="../../views/viewRuan/buscarAdocao.html" class="block hover:text-carmesim">Ver Adoções</a></li>
        </ul>
      </div>

      <!-- Dropdown Pessoas -->
      <div>
        <button onclick="toggleMenu('menu-pessoa', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
          <span class="flex gap-2 items-center"><svg data-lucide="heart-handshake" class="w-5 h-5"></svg>Pessoas</span>
          <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
        </button>
        <ul id="menu-pessoa" class="ml-6 mt-2 space-y-2 hidden">
        <li><a href="../../views/viewWilker/adotante.html" class="block hover:text-carmesim">Cadastrar Adotante</a></li>
          <li><a href="../../views/viewGuilherme/doador.html" class="block hover:text-carmesim">Cadastrar Doador</a></li>
        </ul>
      </div>

      <!-- Dropdown Doação -->
      <div>
        <button onclick="toggleMenu('menu-doacao', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
          <span class="flex gap-2 items-center"><svg data-lucide="hand-heart" class="w-5 h-5"></svg>Doação</span>
          <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
        </button>
        <ul id="menu-doacao" class="ml-6 mt-2 space-y-2 hidden">
          <li><a href="../../views/viewGuilherme/registrarDoacao.html" class="block hover:text-carmesim">Registrar Doação</a></li>
          <li><a href="../../views/viewGuilherme/listarDoacoes.html" class="block hover:text-carmesim">Consultar Doação</a></li>
        </ul>
      </div>
      <!-- Dropdown Vacinas -->
      <div>
        <button onclick="toggleMenu('menu-vacina', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
          <span class="flex gap-2 items-center"><svg data-lucide="dog" class="w-5 h-5"></svg>Vacinas</span>
          <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
        </button>
        <ul id="menu-vacina" class="ml-6 mt-2 space-y-2 hidden">
          <li><a href="../../views/ViewPedro/cadastrarVacina.html" class="block hover:text-carmesim">Cadastrar</a></li>
          <li><a href="../../views/viewWilker/agendarVacinacao.html" class="block hover:text-carmesim">Agendar</a></li>
        </ul>
      </div>

      <!-- Acesso à página de cores -->
      <div>
        <a href="../../views/viewGeral/cores.html" class="block text-left hover:text-carmesim transition flex items-center gap-2">
          <svg data-lucide="palette" class="w-5 h-5"></svg>Cores
        </a>
      </div>

      <!-- Dropdown Relatórios -->
      <div>
        <button onclick="toggleMenu('menu-relatorios', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
          <span class="flex gap-2 items-center"><svg data-lucide="file-text" class="w-5 h-5"></svg>Relatórios</span>
          <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
        </button>
        <ul id="menu-relatorios" class="ml-6 mt-2 space-y-2 hidden">

          <!-- Relatórios de Animais -->
          <div>
            <button onclick="toggleMenu('menu-relatorios-animais', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
              <span class="flex gap-2 items-center"><svg data-lucide="paw-print" class="w-5 h-5"></svg>Animais</span>
              <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
            </button>
            <ul id="menu-relatorios-animais" class="ml-6 mt-2 space-y-2 hidden">
              <li><a href="#" class="block hover:text-carmesim">Relatório de Cadastro</a></li>
              <li><a href="#" class="block hover:text-carmesim">Relatório de Adoção</a></li>
            </ul>
          </div>

          <!-- Relatórios de Adoção -->
          <div>
            <button onclick="toggleMenu('menu-relatorios-adocao', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
              <span class="flex gap-2 items-center"><svg data-lucide="heart" class="w-5 h-5"></svg>Adoção</span>
              <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
            </button>
            <ul id="menu-relatorios-adocao" class="ml-6 mt-2 space-y-2 hidden">
              <li><a href="#" class="block hover:text-carmesim">Relatório de Adoção Realizada</a></li>
              <li><a href="#" class="block hover:text-carmesim">Relatório de Adoção Pendente</a></li>
            </ul>
          </div>

          <!-- Relatórios de Doações -->
          <div>
            <button onclick="toggleMenu('menu-relatorios-doacao', this)" class="w-full flex justify-between items-center text-left hover:text-carmesim transition">
              <span class="flex gap-2 items-center"><svg data-lucide="gift" class="w-5 h-5"></svg>Doações</span>
              <svg data-lucide="chevron-down" class="w-4 h-4 transition-transform duration-300"></svg>
            </button>
            <ul id="menu-relatorios-doacao" class="ml-6 mt-2 space-y-2 hidden">
              <li><a href="#" class="block hover:text-carmesim">Relatório de Doações Recebidas</a></li>
              <li><a href="#" class="block hover:text-carmesim">Relatório de Doações Utilizadas</a></li>
            </ul>
          </div>

        </ul>
      </div>

      <div>
        <a href="../../views/viewLuan/Perfil.html" class="w-full flex justify-start items-center gap-2 text-left hover:text-carmesim transition">
          <svg data-lucide="file-text" class="w-5 h-5"></svg>
          Perfil Empresa
        </a>
      </div>

      <!-- Botão de Logout -->
        <div class="mt-4 border-t border-areia pt-4">
          <button onclick="logout()" class="w-full text-left flex items-center gap-2 text-red-600 hover:text-red-700 font-semibold transition">
            <svg data-lucide="log-out" class="w-5 h-5"></svg>
            Sair   
          </button>
          
        </div>
      </nav>
    </aside>
    <script src="/public/js/apiGuilherme.js"></script>
    <script>
      function toggleMenu(id, btn) {
        const menu = document.getElementById(id);
        menu.classList.toggle('hidden');
        const icon = btn.querySelector('[data-lucide="chevron-down"]');
        icon.classList.toggle('rotate-180');
      }
    </script>
  `);