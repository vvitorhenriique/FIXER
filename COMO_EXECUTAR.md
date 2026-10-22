# Executar o FIXER

## Android Studio

1. Clone o repositório ou baixe e extraia o ZIP.
2. Use **File > Open** e abra a pasta que contém `settings.gradle.kts`, `gradlew` e a pasta `app`.
3. Configure o SDK Android 37 e o JDK 25; aguarde a sincronização do Gradle.
4. Selecione um emulador ou celular com Android 8.0 ou superior.
5. Execute o módulo **app**, pelo botão de executar ou com **Shift+F10**.

Abrir somente a subpasta `app` pode causar o erro `Task 'prepareKotlinBuildScriptModel' not found in project ':app'`.

## PowerShell

Com o Java configurado, execute na raiz:

```powershell
.\gradlew.bat :app:assembleDebug
```

O APK gerado estará em `app/build/outputs/apk/debug/app-debug.apk`. Também há uma cópia para demonstração em `apk/FIXER.apk`.

Com `adb` no PATH e o emulador iniciado:

```powershell
adb -e install -r .\apk\FIXER.apk
adb -e shell am start -n com.fixer.servicosapp/.MainActivity
```

## Cadastro e login locais

Escolha **Cliente** ou **Prestador** e toque em **Finalizar cadastro** após preencher os campos. A senha precisa ter pelo menos seis caracteres. Prestadores informam também categoria e descrição.

Após a confirmação, toque em **Ir para login** e use o mesmo e-mail e senha. Os dados continuam disponíveis após fechar e abrir o aplicativo. Desinstalar o app ou limpar seus dados apaga as contas locais.

## Firebase

As dependências do Firebase estão declaradas, mas o cadastro e o login desta versão usam armazenamento local.

O arquivo `app/google-services.json` é opcional para compilar a demonstração e não é versionado. Adicionar esse arquivo não muda automaticamente o cadastro local para autenticação online; essa integração é um trabalho separado.
