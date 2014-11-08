@echo off
title PkElfo: Game Server Console
color 02
:start
echo Iniciando PkElfo Game Server.
echo.

REM set PATH="C:\Program Files\Java\jre7\bin"
java -server -Dfile.encoding=UTF-8 -Xmx2G -cp ./../libs/*;GameServer_PkElfo.jar pk.elfo.gameserver.GameServer

REM NOTE: If you have a powerful machine, you could modify/add some extra parameters for performance, like:
REM -Xms1536m
REM -Xmx3072m
REM -XX:+AggressiveOpts
REM Use this parameters carefully, some of them could cause abnormal behavior, deadlocks, etc.
REM More info here: http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end

:restart
echo.
echo ADM reiniciou o Game Server.
echo.
goto start

:error
echo.
echo O Game Server Terminou Anormalmente!
echo.

:end
echo.
echo Game Server Terminou.
echo.
pause