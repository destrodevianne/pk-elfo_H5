@echo off
title L2J - Register Game Server
color 17
java -Djava.util.logging.config.file=console.cfg -cp ./../libs/*;L2J_Pk_Elfo_login.jar pk.elfo.tools.gsregistering.BaseGameServerRegister -c
pause