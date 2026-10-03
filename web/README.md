# CEPA Web — Fase 2

Aplicação Java 17 com Servlets e JSP para Apache Tomcat 10.1. A interface segue o projeto CEPA apresentado na primeira entrega. O app Android não faz parte desta pasta.

## Executar

### Caminho mais fácil para a equipe (Windows)

1. Instale um [JDK 17 (Temurin)](https://adoptium.net/temurin/releases/?version=17), marcando a opção de adicionar Java ao `PATH` durante a instalação. Abra uma nova janela do Windows depois.
2. Na pasta principal do repositório, dê dois cliques em `iniciar-web.cmd` (ou use `web/iniciar.cmd`). Também é possível executar o arquivo pelo terminal da IDE.
3. Na primeira execução, aguarde o download automático de Maven, Tomcat e dependências.
4. Abra `http://localhost:8081/cepa/`. Para encerrar, pressione Ctrl+C na janela do inicializador.

O inicializador usa o Maven Wrapper (`mvnw.cmd`). Não é necessário instalar Maven nem Tomcat separadamente. O site usa a porta 8081 para não conflitar com um Tomcat local na porta 8080. É necessário acesso à internet na primeira execução.

### Execução manual com Tomcat instalado

1. Use Java 17, Maven 3.9+ e Tomcat 10.1. Nesta máquina foram instalados Temurin 17, Maven 3.9.16 e Tomcat 10.1.60. Abra um novo terminal para carregar o `PATH` e as variáveis de usuário.
2. Na pasta `web`, execute `mvn clean package`.
3. Copie `target/cepa.war` para `%CATALINA_HOME%\webapps\cepa.war` e inicie o Tomcat com `%CATALINA_HOME%\bin\catalina.bat run`.
4. Abra `http://localhost:8080/cepa/`. Para encerrar o servidor em modo `run`, pressione Ctrl+C no terminal correspondente.

Instalações nesta máquina: JDK em `C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot`; Maven e Tomcat em `C:\Users\eduar\AppData\Local\CepaDevTools`. O Tomcat foi instalado como ZIP para execução manual, sem criar serviço de inicialização automática.

Sem configuração cloud, o catálogo usa dados de demonstração em memória. O painel permite cadastrar vinhos durante a execução, mas esses dados desaparecem ao reiniciar o servidor. O cabeçalho da aplicação mostra o modo ativo.

## Ativar o Cloud Firestore

1. Crie um projeto Firebase no plano Spark e um banco Cloud Firestore (Standard).
2. Crie uma conta de serviço com acesso ao Firestore e guarde o arquivo JSON **fora deste projeto e fora do pacote de entrega**.
3. Configure as variáveis de ambiente `GOOGLE_APPLICATION_CREDENTIALS` com o caminho absoluto do JSON e `FIREBASE_PROJECT_ID` com o ID do projeto antes de iniciar o Tomcat.
4. Reinicie o Tomcat. A aplicação cria uma coleção `vinhos` com os dados iniciais caso esteja vazia. Alterações feitas no painel passam a ser persistidas no Firestore.

Configure também `CEPA_ADMIN_PASSWORD` antes de iniciar o Tomcat. No modo local, a senha demonstrativa é `cepa-demo`. No modo cloud, a senha é obrigatória e não há valor padrão. Este controle simples serve à demonstração acadêmica; uma implantação pública exigiria autenticação de usuários e controles adicionais.

O serviço usa credenciais apenas no servidor. Nunca coloque a chave JSON em `src/main/webapp`, no WAR ou no Word da entrega. O projeto continua executando em modo local se a configuração não estiver presente. Se as variáveis estiverem presentes mas o Firestore falhar, a aplicação mostra o erro em vez de fingir que a operação cloud funcionou.

## Fluxos demonstráveis

- Início → quiz → catálogo filtrado pelo perfil → ficha do vinho.
- Catálogo → carrinho → confirmação demonstrativa → Minha Adega.
- Painel da Vinheria → cadastro de um vinho → catálogo atualizado.

O checkout não processa pagamento. Temperatura, transporte e indicadores de vendas não são medidos nesta fase: qualquer informação sobre esses temas na interface é marcada como visão futura ou exemplo.
