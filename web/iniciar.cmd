@echo off
setlocal
cd /d "%~dp0"
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "PATH=%JAVA_HOME%\bin;%PATH%"
where java >nul 2>&1
if errorlevel 1 (
  echo Java 17 nao encontrado. Instale o JDK 17 e abra este arquivo novamente.
  pause
  exit /b 1
)
echo.
echo Iniciando o CEPA. Na primeira vez, Maven e Tomcat serao baixados automaticamente.
echo Quando aparecer a mensagem de inicializacao, abra http://localhost:8081/cepa/
echo Para encerrar o servidor, pressione Ctrl+C nesta janela.
echo.
call "%~dp0mvnw.cmd" package cargo:run
if errorlevel 1 (
  echo.
  echo A inicializacao falhou. Consulte a mensagem acima.
  pause
  exit /b 1
)
