#! usr/bin/bash

###########################################################################

# Kafka, Zookeeper Launch:
tmp_files = "/tmp/kafka-logs"

if [ -d "$tmp_files" ] 
then
    cd /tmp/kafka-logs
    rm -rf *                # Must clear logs first
    echo "Logs Cleared."
else
    echo "$path directory doesn't exist."
fi

###########################################################################

cd kafka_2.13-3.3.1
bin/kafka-server-start.sh config/server.properties
