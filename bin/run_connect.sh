#! usr/bin/bash

# https://docs.datastax.com/en/kafka/doc/kafka/operations/kafkaStartStop.html

cd ../

kafka_2.13-3.3.1/bin/connect-standalone.sh ./kafka_2.13-3.3.1/config/connect-standalone.properties ./kafka-connect/kafka-connect-cassandra-sink-1.4.0/conf-prod/cassandra-sink-standalone.properties