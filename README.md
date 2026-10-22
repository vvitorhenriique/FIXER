# FIXER

Aplicativo Android desenvolvido em Kotlin com o objetivo de conectar clientes a prestadores de serviços da região. A proposta do FIXER é permitir que o usuário encontre profissionais por nome ou categoria, consulte seus detalhes e entre em contato por WhatsApp ou ligação telefônica.

**O projeto está em desenvolvimento.** A versão atual implementa cadastro e login locais; a listagem de profissionais e a tela de detalhes ainda serão desenvolvidas.

## Integrantes

- Leonardo Bora Zimmermann.
- Victor Hugo de Miranda Assunção.
- Vitor Henrique de Melo.

## Funcionalidades implementadas

- Tela inicial de apresentação e navegação entre login, cadastro e perfil.
- Cadastro como **Cliente** ou **Prestador**.
- Categoria e descrição do serviço para prestadores.
- Validação dos campos, senha com pelo menos seis caracteres e aviso de e-mail já cadastrado.
- Confirmação do cadastro, login e visualização dos dados da conta.
- Persistência local: os cadastros continuam disponíveis após fechar e abrir o aplicativo.
- Senhas armazenadas com salt e PBKDF2, sem texto simples.

**Esta versão é uma demonstração local.** As contas ficam somente no aparelho e são apagadas ao desinstalar o app ou limpar seus dados. A recuperação de senha por e-mail e a autenticação online ainda não estão implementadas.

## Estrutura de telas e andamento

| Tela prevista | Situação atual |
| --- | --- |
| **Login** (`LoginActivity` na proposta) | Funcional na versão local: e-mail, senha com opção de mostrar/ocultar, botão Entrar e acesso ao cadastro. Após entrar, o usuário visualiza sua conta; o direcionamento para a Home de prestadores ainda está em desenvolvimento. |
| **Cadastro** (`RegisterActivity` na proposta) | Funcional na versão local: nome completo, e-mail, senha, seleção de Cliente ou Prestador, campos extras de categoria e descrição para prestadores e botão Finalizar Cadastro. |
| **Principal / Home** (`HomeActivity` na proposta) | **Em desenvolvimento:** lista de prestadores, barra superior com perfil/sair, pesquisa por nome ou categoria e cards com foto ou ícone, nome, profissão e avaliação média. A tela inicial atual apresenta o FIXER e o botão Começar; ainda não exibe esse catálogo. |
| **Detalhes do Prestador** (`DetailActivity` na proposta) | **Em desenvolvimento:** abertura do profissional selecionado na lista, foto em destaque, nome, categoria, seção Sobre mim, telefone/WhatsApp e botões para abrir a conversa no WhatsApp ou o discador de chamadas. |

O fluxo final previsto é **Login → Home com prestadores → Detalhes → Contato**. A conexão entre essas etapas ainda será concluída.

Os nomes de Activities acima fazem parte da proposta de organização. A implementação atual utiliza **Jetpack Compose**, com as telas organizadas em uma `MainActivity` e navegação pelo Navigation Compose.

## APK

[**Baixar o APK de demonstração**](apk/FIXER.apk?raw=true)

Compatível com **Android 8.0 (API 26)** ou superior. No celular, abra o arquivo e permita a instalação pelo aplicativo usado para baixá-lo, caso o Android solicite.

## Executar o projeto

1. Clone este repositório ou use **Code > Download ZIP** e extraia os arquivos.
2. No Android Studio, abra a pasta raiz que contém `settings.gradle.kts`, `gradlew` e a pasta `app`.
3. Configure o SDK Android **37**, use o **JDK 25** e aguarde a sincronização do Gradle.
4. Selecione um emulador ou celular compatível e execute o módulo **app**.

Ambiente usado: Android Studio 2026.1.3, Gradle 9.5.0, Android Gradle Plugin 9.3.3, Jetpack Compose e Material 3. O Gradle Wrapper está incluído no projeto.

Para gerar o APK no PowerShell, na raiz do projeto:

```powershell
.\gradlew.bat :app:assembleDebug
```

O resultado fica em `app/build/outputs/apk/debug/app-debug.apk`. No macOS/Linux, use `./gradlew :app:assembleDebug`.

## Telas

| Início | Cadastro de prestador |
| --- | --- |
| <img src="docs/screenshots/inicio.png" alt="Tela inicial do FIXER" width="230"> | <img src="docs/screenshots/cadastro-prestador.png" alt="Seleção de Prestador, categoria, descrição e botão para finalizar" width="230"> |

## Testes

O projeto inclui testes de cadastro de Cliente e Prestador, validação de campos, persistência, e-mail duplicado e login. Com um emulador ou celular conectado:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

Os testes criam e removem contas de demonstração. Execute-os em um emulador de teste.

## Arquivos para entrega

O código da versão atual, o APK e os prints estão neste repositório. O projeto compactado pode ser baixado em **Code > Download ZIP**. Veja também as [instruções para executar o aplicativo](COMO_EXECUTAR.md).
