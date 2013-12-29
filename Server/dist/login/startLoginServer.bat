@echo off
title Pkelfo: Login Console
color 02
:start
echo Iniciando Pkelfo Login Server.
echo.

java -Xms128m -Xmx128m -cp ./../libs/*;Pk_Elfo_login.jar pk.elfo.loginserver.L2LoginServer

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