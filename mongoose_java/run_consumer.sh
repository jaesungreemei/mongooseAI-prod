#!/usr/bin/env bash

echo Compile...

# pass <topicName> <numOfRecsToProduce> as args

mvn -q clean compile exec:java \
 -Dexec.mainClass="kafka.App" \
 -Dexec.args="consumer $1"