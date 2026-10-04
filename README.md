# CEPA — Vinheria Agnello

Projeto acadêmico da Fase 2 de Engenharia de Software da FIAP. O CEPA apresenta uma experiência digital de descoberta de vinhos, com quiz de paladar, catálogo, fichas de produtos, carrinho demonstrativo e painel da vinheria.

- `web/`: aplicação em Java 17, JSP, Servlets e CSS.
- `android/`: aplicativo em Kotlin e Jetpack Compose com login, criação de conta e home/catálogo.

Extraia o ZIP completo antes de executar. A primeira execução de cada projeto precisa de internet para baixar dependências.

## Executar o web

1. Instale o [JDK 17](https://adoptium.net/temurin/releases/?version=17). No Windows, habilite a opção de adicionar o Java ao `PATH`.
2. Na raiz do projeto, dê dois cliques em `iniciar-web.cmd`.
3. Aguarde a inicialização e abra [http://localhost:8081/cepa/](http://localhost:8081/cepa/).

Mantenha a janela aberta enquanto utiliza o site. Para encerrar, pressione `Ctrl+C`. Maven e Tomcat são baixados automaticamente; não é necessário instalar uma IDE para executar o web.

Também é possível iniciar pelo PowerShell, na raiz do projeto:

```powershell
.\iniciar-web.cmd
```

No macOS ou Linux, abra um terminal na pasta `web/` e execute:

```bash
sh ./mvnw package cargo:run
```

Para acessar o painel da vinheria na demonstração local, use a senha `cepa-demo`. Os cadastros ficam em memória e são perdidos ao reiniciar. O carrinho não processa pagamentos ou entregas reais.

## Executar o mobile

1. Instale o [Android Studio](https://developer.android.com/studio) e conclua a instalação do Android SDK.
2. Escolha **Open** e abra a pasta `android/` do projeto.
3. Aguarde o **Gradle Sync**. Instale o **Android SDK Platform 36** se solicitado e use o Gradle JDK integrado ao Android Studio.
4. Em **Tools → Device Manager**, crie e inicie um emulador de telefone com Android **API 35 ou 36**.
5. Selecione a configuração **app**, escolha o emulador e clique em **Run** (▶).

Para entrar na demonstração, informe um e-mail válido e uma senha não vazia. Login e cadastro são demonstrativos. O aplicativo funciona independentemente do web e da AWS.

## AWS EC2

O projeto inclui um roteiro de implantação para a instância Ubuntu EC2 da equipe, com armazenamento do catálogo em arquivo no volume EBS. A execução local do web e do mobile não exige uma conta AWS.

Para preparar o web na EC2, envie ou clone o projeto na instância e execute na pasta `web/`:

```bash
bash ./deploy-ec2.sh
```

O roteiro instala Java e Tomcat, configura a persistência e verifica a resposta da aplicação dentro da instância. Essa etapa exige acesso autorizado à EC2 e internet. Mantenha a porta 8080 fechada ao público enquanto não houver HTTPS e controle de acesso adequado.
