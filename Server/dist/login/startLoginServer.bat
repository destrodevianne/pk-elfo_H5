@echo off
title Pkelfo: Login Console
color 02
:start
echo Iniciando Pkelfo Login Server.
echo.

java -version:1.7 -server -Dfile.encoding=UTF-8 -Xms256m -Xmx256m -cp ./../libs/*;LoginServer_PkElfo.jar pk.elfo.loginserver.L2LoginServer

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end

:restart
echo.
echo Admin Restarted Login Server.
echo.
goto start

:error
echo.
echo O Login Server terminou anormalmente!
echo.

:end
echo.
echo Login Server Terminado.
echo.
pause