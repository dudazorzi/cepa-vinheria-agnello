# CEPA — Vinheria Agnello

Projeto acadêmico da fase 2 de Engenharia de Software da FIAP. O CEPA propõe uma experiência digital de curadoria de vinhos inspirada no atendimento da Vinheria Agnello.

## Estrutura

- [`web/`](web/) — aplicação Java com Servlets, JSP, HTML e CSS. Inclui catálogo, quiz de paladar, ficha do vinho, carrinho demonstrativo, Minha Adega e painel da vinheria.
- `android/` — reservado para a interface mobile em Kotlin e Jetpack Compose prevista nesta fase.

As instruções para compilar, executar e configurar o Cloud Firestore estão no [README da aplicação web](web/README.md).

## Estado do projeto

A aplicação web pode funcionar com dados de demonstração locais ou com o catálogo persistido no Cloud Firestore. A conexão com um projeto Firebase da equipe precisa ser configurada separadamente. O checkout é demonstrativo: não realiza pagamentos nem entregas reais.

Não inclua chaves de serviço, senhas ou outros segredos no repositório.
