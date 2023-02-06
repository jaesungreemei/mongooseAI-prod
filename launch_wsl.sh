#! usr/bin/bash

###########################################################################

# Edit .bashrc file to edit path for:
#  - Cassandra (.\/mongooseAI\/apache-cassandra-4.0.7\/bin)
#  - Kafka (.\/mongooseAI\/kafka_2.13-3.3.1\/bin)
file=../.bashrc
path="PATH="
# default_path=$(getconf PATH)

if  grep -q "$path" "$file" ; then
    sed -i 's/PATH=.*/PATH=".\/mongooseAI-prod\/kafka_2.13-3.3.1\/bin:$PATH"/' $file
    echo "Path has been set."
else
    sed -i 's/PATH=.\/mongooseAI-prod\/kafka_2.13-3.3.1\/bin:$PATH/' $file
    echo "Path has been set."
fi

###########################################################################

# Tab 1: Cassandra Server & Kafka Connect
# Tab 2: Zookeeper Server & Kafka Server

cmd.exe /c "wt.exe" --window 0 \
  split-pane -V \; \
  new-tab \; \
  split-pane -V \; \

###########################################################################

# Kafka, Zookeeper Launch:
# tmp_files = "/tmp/kafka-logs"

# if [ -d "$tmp_files" ] 
# then
#     cd /tmp/kafka-logs
#     rm -rf *                # Must clear logs first
#     echo "Logs Cleared."
# else
#     echo "$path directory doesn't exist."
# fi

###########################################################################

### NOTE: WSL2 wt.exe and wsl.exe do not currently support running multiple commands in different panes at once.
### https://stackoverflow.com/questions/62468029/windows-terminal-open-multiple-panes-and-execute-specified-command
### https://learn.microsoft.com/en-us/windows/terminal/command-line-arguments?tabs=linux

# Launch Commands:
# zookeeper-server-start.sh ~/mongooseAI/kafka_2.13-3.3.1/config/zookeeper.properties
# kafka-server-start.sh ~/mongooseAI/kafka_2.13-3.3.1/config/server.properties

# cd apache-cassandra-4.0.7/
# bin/cassandra

# cd ./apache-cassandra-4.0.7/bin
# cqlsh