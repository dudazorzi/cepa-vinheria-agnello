# CEPA — Vinheria Agnello

Projeto acadêmico da fase 2 de Engenharia de Software da FIAP. O CEPA leva para o ambiente digital parte da experiência de atendimento da Vinheria Agnello: ajuda o cliente a descobrir vinhos de acordo com seu paladar e apresenta informações para uma escolha mais segura.

## O que o projeto oferece

A aplicação web reúne um quiz de paladar, catálogo com filtros e recomendações, fichas dos vinhos, carrinho demonstrativo, área **Minha Adega** e um painel para a vinheria cadastrar produtos. O fluxo principal é: responder ao quiz → explorar as sugestões → conhecer um vinho → adicioná-lo ao carrinho.

O projeto está dividido em duas partes:

- `web/`: aplicação Java 17 com Servlets, JSP, HTML e CSS, executada no Tomcat 10.1.
- `android/`: espaço previsto para as telas mobile em Kotlin e Jetpack Compose. As instruções de execução serão adicionadas quando essa parte estiver pronta.

O checkout não processa pagamentos nem entregas reais. Informações sobre temperatura, transporte e indicadores de vendas são demonstrações ou propostas futuras, não medições em tempo real.

## Como executar a aplicação web

Não é necessário instalar uma IDE, Maven ou Tomcat para usar o caminho abaixo.

1. Baixe este repositório pelo botão **Code → Download ZIP** do GitHub e extraia o ZIP. Se preferir, clone o repositório com Git.
2. Instale o [JDK 17 (Temurin)](https://adoptium.net/temurin/releases/?version=17). No Windows, marque a opção de adicionar o Java ao `PATH` e abra uma nova janela após a instalação.
3. Na pasta principal do projeto, dê dois cliques em `iniciar-web.cmd`.
4. Aguarde a mensagem de inicialização do Tomcat e abra **http://localhost:8081/cepa/** no navegador. Mantenha a janela do inicializador aberta enquanto usa o site; para encerrar, pressione **Ctrl+C** nela.

Na primeira execução, é preciso ter acesso à internet: o Maven Wrapper baixa o Maven e o projeto baixa o Tomcat e as dependências automaticamente. Nas próximas execuções, os arquivos já baixados são reutilizados. Se a porta 8081 estiver ocupada, encerre o outro programa que a utiliza antes de iniciar o CEPA.

Quem usa macOS ou Linux pode abrir um terminal na pasta `web/` e executar `sh ./mvnw package cargo:run`; o endereço do site é o mesmo. É necessário ter o JDK 17 instalado.

### Dados usados na demonstração

O site funciona sem conta cloud ou credenciais: nesse caso, usa dados locais em memória. Produtos cadastrados no painel aparecem durante a execução, mas não permanecem após reiniciar o servidor. A senha demonstrativa do painel, no modo local, é `cepa-demo`.

Para persistir o catálogo no Cloud Firestore, a equipe pode configurar um projeto Firebase separadamente. Antes de iniciar a aplicação, defina `GOOGLE_APPLICATION_CREDENTIALS` com o caminho absoluto para a chave JSON de uma conta de serviço, `FIREBASE_PROJECT_ID` com o ID do projeto e `CEPA_ADMIN_PASSWORD` com a senha do painel. Com o Firestore configurado, os produtos ficam na coleção `vinhos`; se a conexão falhar, a aplicação informa o erro. **Nunca inclua a chave JSON, senhas ou outros segredos neste repositório ou na entrega.**

## Como executar o aplicativo mobile

*Seção reservada para a próxima etapa.* Aqui serão incluídos os requisitos do Android Studio, como abrir o projeto, iniciar um emulador e executar as telas em Jetpack Compose. Nesta fase, as telas mobile são de interface e não precisam se conectar ao servidor ou ao Cloud Firestore.
