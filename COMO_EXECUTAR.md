# Executar o FIXER 1.1

## Android Studio

1. Clone o repositório ou baixe e extraia o ZIP.
2. Use **File > Open** e abra a pasta que contém `settings.gradle.kts`, `gradlew` e a pasta `app`.
3. Configure o SDK Android 37 e o JDK 25; aguarde a sincronização do Gradle.
4. Selecione um emulador ou celular com Android 8.0 ou superior.
5. Execute o módulo **app**, pelo botão de executar ou com **Shift+F10**.

Abrir somente a subpasta `app` pode causar o erro `Task 'prepareKotlinBuildScriptModel' not found in project ':app'`.

## Gerar e instalar o APK

Na raiz do projeto, com o Java configurado:

```powershell
.\gradlew.bat :app:assembleDebug
```

O APK estará em `app/build/outputs/apk/debug/app-debug.apk`. A cópia para entrega fica em `apk/FIXER.apk`.

Com `adb` no PATH e o emulador iniciado:

```powershell
adb -e install -r .\apk\FIXER.apk
adb -e shell am start -n com.fixer.servicosapp/.MainActivity
```

## Apresentar o fluxo completo

1. Toque em Começar e em Cadastre-se.
2. Escolha Cliente ou Prestador. A senha deve ter pelo menos seis caracteres.
3. Para Prestador, informe categoria, descrição e telefone com DDD. Use um número que você possa utilizar na apresentação.
4. Finalize o cadastro e toque em Ir para login.
5. Entre com o e-mail e a senha cadastrados. O app abrirá o catálogo.
6. Pesquise por nome ou categoria, aplique os filtros e abra um profissional.
7. Nos detalhes de um prestador com telefone, teste os botões WhatsApp e Ligar. A ligação apenas abre o discador.
8. Acesse Minha conta para consultar os dados e atualizar o telefone de um prestador.
9. Use Sair para encerrar a sessão. Com Lembrar de mim marcado no login, a sessão continua nas próximas aberturas do app.

Os cinco perfis com selo Exemplo e suas notas são fictícios e não possuem telefone. Os botões de contato desses exemplos ficam desabilitados. Cadastre seu próprio prestador no mesmo aparelho para testar os contatos.

## Dados locais

As contas permanecem disponíveis após fechar o app. Prestadores locais aparecem no catálogo do mesmo aparelho, sem avaliações. Cadastros de versões anteriores continuam válidos; prestadores sem telefone podem adicioná-lo em Minha conta.

Não há sincronização entre celulares. Desinstalar o aplicativo ou limpar seus dados apaga as contas locais. A recuperação de senha por e-mail ainda está em desenvolvimento.

## Firebase

As dependências estão declaradas, mas o cadastro e o login desta versão usam armazenamento local. O arquivo `app/google-services.json` é opcional para compilar a demonstração e não é versionado. A autenticação online ainda precisa ser implementada.
