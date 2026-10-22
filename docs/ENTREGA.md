# Entrega do FIXER

## Arquivos

- **Projeto completo:** raiz deste repositório; para compactar, use **Code > Download ZIP** no GitHub.
- **Aplicativo:** [apk/FIXER.apk](../apk/FIXER.apk?raw=true).
- **Prints atuais:** [docs/screenshots](screenshots/).
- **Objetivo, funcionalidades e integrantes:** [README](../README.md).
- **Imagens da primeira versão:** preservadas em [screenshots/historico](screenshots/historico/).

O repositório contém os arquivos necessários para compilar o aplicativo. Pastas de cache, resultados temporários de compilação, configurações do computador e credenciais não são incluídos.

## Roteiro para demonstração de 5 minutos

1. **0:00–0:30:** apresentar os integrantes e o objetivo do FIXER.
2. **0:30–1:30:** abrir o app, acessar o cadastro e mostrar a escolha entre Cliente e Prestador.
3. **1:30–2:30:** selecionar Prestador, preencher categoria e descrição, concluir e mostrar o perfil salvo.
4. **2:30–3:30:** voltar ao login e entrar com o e-mail e a senha cadastrados.
5. **3:30–4:30:** mostrar a validação de campos e o aviso de e-mail repetido; fechar e abrir o aplicativo para demonstrar a persistência.
6. **4:30–5:00:** mostrar o repositório, o APK e explicar que os dados são locais nesta versão.

Use dados fictícios na apresentação. Cada aparelho começa com sua própria base de contas.

## Relação com o material da disciplina

O material enviado apresenta o projeto UP Campus+ (agenda acadêmica), enquanto este repositório usa o tema FIXER (serviços). A entrega do FIXER reúne projeto completo, APK, prints e README com integrantes.

| Item funcional do roteiro | Situação no FIXER atual |
| --- | --- |
| Tela inicial, navegação e formulário com validação | Implementados para contas de Cliente e Prestador |
| Persistência após fechar o app | Implementada localmente para as contas |
| Visualização dos dados cadastrados | Implementada na confirmação e na tela da conta |
| Agenda e cadastro de eventos | Fora do tema desta versão |
| Lista de registros e edição/exclusão (CRUD completo) | Ainda não implementados |
| Favoritos persistentes | Ainda não implementados |
| Tela Sobre | Ainda não implementada |
| Interface XML/Views e evolução para Room | Esta versão utiliza Jetpack Compose e SharedPreferences |

A troca de tema e de tecnologia precisa estar de acordo com a orientação do professor. A organização dos arquivos para entrega não significa que todos os recursos do roteiro já foram desenvolvidos.
