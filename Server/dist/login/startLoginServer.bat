@echo off
title KingServer: Login Console
color 02
:start
echo Iniciando KingServer Login Server.
echo.

java -Xms128m -Xmx128m -cp ./../libs/*;l2jlogin.jar king.server.loginserver.L2LoginServer

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
echo Login Server terminated abnormally!
echo.

:end
echo.
echo Login Server Terminated.
echo.
pause