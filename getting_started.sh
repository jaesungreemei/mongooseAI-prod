####################################################

# Install Kafka
tar -xzf ./downloads/kafka_2.13-3.3.1.tgz

# Install Cassandra
tar xzvf ./downloads/apache-cassandra-4.0.7-bin.tar.gz

####################################################

# Install Kafka Connect
mkdir kafka-connect
cd ./kafka-connect/
tar xzf ../downloads/kafka-connect-cassandra-sink-1.4.0.tar.gz

# Configure path to Kafka Connect JAR File
cd ../kafka_2.13-3.3.1/config/
echo plugin.path=/home/greemei/mongooseai-prod/kafka-connect/kafka-connect-cassandra-sink-1.4.0/kafka-connect-cassandra-sink-1.4.0.jar >> connect-standalone.properties

####################################################

# Move and configure cassandra-sink-standalone.properties
# Starting Directory: mongooseAI-prod/kafka-connect/
cd ../../kafka-connect/

mkdir ./kafka-connect-cassandra-sink-1.4.0/conf-prod
cd kafka-connect-cassandra-sink-1.4.0/conf-prod
cp ../conf/cassandra-sink-standalone.properties.sample cassandra-sink-standalone.properties

