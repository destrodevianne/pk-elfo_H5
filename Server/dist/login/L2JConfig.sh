#!/bin/sh
java -Djava.util.logging.config.file=console.cfg -cp ./../libs/*:GameServer_PkElfo.jar:LoginServer_PkElfo.jar pk.elfo.tools.configurator.ConfigUserInterface
