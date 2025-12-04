@echo off
echo ==========================================
echo      INICIANDO A APLICACAO...
echo   (Isso pode demorar na primeira vez)
echo ==========================================

:: Verifica se o Docker está rodando
docker info >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo O Docker nao esta rodando ou nao esta instalado.
    echo Por favor, abra o Docker Desktop e tente novamente.
    pause
    exit
)

:: Roda o projeto
docker-compose up

pause