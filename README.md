# CEPA — Vinheria Agnello

Projeto acadêmico da fase 2 de Engenharia de Software da FIAP. O CEPA leva para o ambiente digital parte da experiência de atendimento da Vinheria Agnello: ajuda o cliente a descobrir vinhos de acordo com seu paladar e apresenta informações para uma escolha mais segura.

## O que o projeto oferece

A aplicação web reúne um quiz de paladar, catálogo com filtros e recomendações, fichas dos vinhos, carrinho demonstrativo, área **Minha Adega** e um painel para a vinheria cadastrar produtos. O fluxo principal é: responder ao quiz → explorar as sugestões → conhecer um vinho → adicioná-lo ao carrinho.

O projeto está dividido em duas partes:

- `web/`: aplicação Java 17 com Servlets, JSP e CSS, executada no Tomcat 10.1.
- `android/`: aplicativo demonstrativo em Kotlin e Jetpack Compose com login, criação de conta e home/catálogo.

O checkout não processa pagamentos nem entregas reais. Informações sobre temperatura, transporte e indicadores de vendas são demonstrações ou propostas futuras, não medições em tempo real.

## Como executar a aplicação web no computador

Não é necessário instalar uma IDE, Maven ou Tomcat para usar o caminho abaixo.

1. Baixe este repositório pelo botão **Code → Download ZIP** do GitHub e extraia o ZIP. Se preferir, clone o repositório com Git. Use a versão mais recente, que deve conter as pastas `web/` e `android/`.
2. Instale o [JDK 17 (Temurin)](https://adoptium.net/temurin/releases/?version=17). No Windows, marque a opção de adicionar o Java ao `PATH` e abra uma nova janela após a instalação.
3. Na pasta principal do projeto, dê dois cliques em `iniciar-web.cmd`.
4. Aguarde a mensagem de inicialização do Tomcat e abra **http://localhost:8081/cepa/** no navegador. Mantenha a janela do inicializador aberta enquanto usa o site; para encerrar, pressione **Ctrl+C** nela.

Na primeira execução, é preciso ter acesso à internet: o Maven Wrapper baixa o Maven e o projeto baixa o Tomcat e as dependências automaticamente. Nas próximas execuções, os arquivos já baixados são reutilizados. Se a porta 8081 estiver ocupada, encerre o outro programa que a utiliza antes de iniciar o CEPA.

Quem usa macOS ou Linux pode abrir um terminal na pasta `web/` e executar `sh ./mvnw package cargo:run`; o endereço do site é o mesmo. É necessário ter o JDK 17 instalado.

### Dados usados na demonstração local

O site funciona sem conta cloud ou credenciais: nesse caso, usa dados locais em memória. Produtos cadastrados no painel aparecem durante a execução, mas não permanecem após reiniciar o servidor. A senha demonstrativa do painel, **somente no modo local**, é `cepa-demo`.

Para testar a persistência **no próprio computador**, defina `CEPA_DATA_FILE` como caminho absoluto para `vinhos.xml` em uma pasta gravável e `CEPA_ADMIN_PASSWORD` como uma senha própria **antes** de iniciar o Tomcat. O arquivo é criado automaticamente com seis vinhos iniciais e os cadastros permanecem após reiniciar o servidor. Se o arquivo existente estiver corrompido, a aplicação informa erro em vez de sobrescrevê-lo. Não coloque o arquivo de dados nem a senha no GitHub.

## AWS EC2 usada pela equipe

O relatório AWS da equipe documenta uma instância **EC2 Ubuntu Server 26.04 ARM (t4g.small)** com volume **EBS de 8 GiB**. O código agora está preparado para executar o JSP no Tomcat 10 dessa instância e salvar o catálogo em `/var/lib/cepa/vinhos.xml`, no EBS. O EBS mantém os dados ao **parar e iniciar** a instância; por padrão, o volume raiz pode ser excluído quando a instância é **terminada**. Faça backup antes de terminar a instância.

**Estado de verificação:** a compilação, o teste automatizado de persistência e a execução local são verificáveis neste repositório. O roteiro abaixo **não foi executado nem validado na conta AWS da equipe**. A instância documentada não deve ser apresentada como aplicação implantada até alguém realizar e registrar o teste nela.

Um integrante com acesso autorizado à EC2 pode executar, pelo **EC2 Instance Connect**, após confirmar as verificações da instância e enviar a versão atualizada do código ao GitHub:

```bash
git clone https://github.com/dudazorzi/cepa-vinheria-agnello.git
cd cepa-vinheria-agnello/web
bash ./deploy-ec2.sh
```

O script instala Java 17 e Tomcat 10 com o gerenciador de pacotes do Ubuntu, compila o WAR, configura uma senha aleatória em `/etc/cepa/cepa.env` (somente leitura de root), habilita o arquivo de catálogo persistente e testa `http://127.0.0.1:8080/cepa/inicio` **dentro da instância**. Não é necessário criar uma conta Firebase nem enviar chaves da AWS para o projeto. Para consultar a senha do painel, somente o administrador da instância deve executar `sudo grep CEPA_ADMIN_PASSWORD /etc/cepa/cepa.env`. Se o script falhar, leia a mensagem exibida e `sudo journalctl -u tomcat10 -n 80 --no-pager`.

Mantenha a porta 8080 fechada para a internet enquanto não houver HTTPS e controle de acesso adequado: o painel usa senha e não deve ser exposto via HTTP público. O teste interno com `curl` demonstra que a aplicação respondeu na EC2, mas **não** demonstra acesso público. Não compartilhe login da AWS, senha do painel ou arquivos de credenciais com professor/colegas; eles podem reproduzir o projeto localmente pelos passos acima. Verifique os custos da instância e do EBS na conta AWS e pare a instância quando não estiver em uso.

## Como executar o aplicativo mobile no Android Studio

1. Instale o [Android Studio](https://developer.android.com/studio) e conclua o assistente inicial para instalar o Android SDK e aceitar as licenças. Para usar IDE e emulador juntos, o mínimo indicado pelo Android é **16 GB de RAM e 16 GB livres em disco**; em computador mais limitado, use um aparelho Android físico para testar.
2. Baixe/extraia o repositório. No Android Studio, escolha **Open** (**File → Open** se já houver projeto aberto) e selecione **exatamente a pasta `android/`**, que contém `settings.gradle.kts`. Não selecione a raiz nem `android/app`. Confirme **Trust Project** se solicitado.
3. Aguarde o **Gradle Sync**. Na primeira vez, mantenha internet disponível para baixar Gradle e bibliotecas. Se a IDE pedir, instale **Android SDK Platform 36** e os Build Tools. Use o **Gradle JDK integrado ao Android Studio** ou outro JDK 17 ou superior compatível; não é necessário instalar Gradle separadamente.
4. Abra **Tools → Device Manager → + → Create Virtual Device**. Em **Phone**, escolha **Pixel 8 ou Pixel 9** (ou outro perfil disponível); selecione uma imagem **Android API 35 ou 36**, de preferência *Google APIs*, e conclua. Inicie o emulador pelo botão ▶ no Device Manager.
5. Na barra superior da IDE, selecione a configuração **app**, escolha o emulador criado e clique em **Run** (▶). Aguarde a tela de login do CEPA. Não é necessário iniciar o site, o Tomcat ou a AWS.

O projeto inclui o Gradle Wrapper. Para apenas compilar pelo terminal, dentro de `android/`, use `gradlew.bat :app:assembleDebug` no Windows ou `sh ./gradlew :app:assembleDebug` no macOS/Linux. O APK de teste fica em `android/app/build/outputs/apk/debug/`. O arquivo `android/local.properties` é criado por cada instalação do Android Studio e **não deve ser copiado de outro computador nem enviado ao GitHub**.

As três telas desta fase são **Login**, **Criar conta** e **Home/catálogo**. A Home usa `Scaffold` com `TopAppBar`, `BottomAppBar` e `FloatingActionButton`; os componentes reutilizáveis ficam em `components/` e as telas em `screens/`. O botão flutuante abre uma indicação rápida com dados locais. Para testar, digite um e-mail válido e uma senha não vazia. Login e cadastro validam os campos, mas **não criam contas reais nem autenticam usuários**. O aplicativo não se conecta ao JSP nem à AWS nesta fase.

Se a sincronização falhar por falta do SDK, abra **Settings → Languages & Frameworks → Android SDK** e instale a plataforma 36. Se não aparecer um aparelho para executar, crie-o no Device Manager. Se o emulador não iniciar no Windows, confira se a virtualização está habilitada no computador. As capturas prontas estão em `android/screenshots/`: `login.png`, `criar-conta.png`, `home.png`, `catalogo.png` e `indicacao-rapida.png`. Para a entrega, inclua pelo menos as três telas obrigatórias no documento Word, junto com o projeto Android.
