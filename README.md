# Mongoose AI
Kafka-Flink-Cassandra Data Pipeline Implementation for Mongoose AI.

## Full Documentation
* Production Details (mongoose-prod): https://docs.google.com/document/d/1FeQUUAP0NwxkRoNrd_TKdKlIQN2l_dvRfsZKIYPwlu4/edit?usp=sharing

## Overview

### Prerequisites
| Framework         | Version |
| ----------------- | ------------- |
| Apache Kafka      | 3.3.1  |
| Apache Cassandra  | 4.0.7  |
| DataStax Apache Kafka Connector | 1.4.0 |
| DataStax Driver for Apache Cassandra | 3.25 |
| Confluent Kafka Python | 2.0.2 |

* Development Environment: WSL2 Ubuntu 20.04.5 LTS on Windows 11

### Architecture
![Alt text](/architecture.jpg?raw=true "Data Pipeline Architecture")

### Usage
| Function | Pipeline | Description |
| -------- | -------- | ----------- |
| Store Raw Data | (Producer 1) → (Kafka Connect 1) | Store raw data immediately into DB without processing |
| Raw Data Pre-Processing | (Producer 1) → (Flink) | Use Flink to preprocess raw data before storing |
| Data Analysis | (Producer 1) → (Consumer 1) → (Producer 2) → (Kafka Connect 2) | Forward data to AI/ML engine, AI/ML engine sends back analysis through Kafka to be stored in DB |

## Directory Structure

```
mongooseAI-prod
    .
    ├── bin                     # shell files to run servers
    ├── downloads               # download tar files
    .
    ├── apache-cassandra-4.0.7      # Apache Cassandra files installed from apache-cassandra-4.0.7-bin.tar.gz
    ├── kafka_2.13-3.3.1            # Apache Kafka files installed from kafka_2.13-3.3.1.tgz
    .
    ├── kafka-connect               # DataStax Kafka Connector files installed from kafka-connect-cassandra-sink-1.4.0.tar.gz
    ├──├── kafka-connect-cassandra-sink-1.4.0
    ├──├──├── conf
    ├──├──├── conf-prod             # custom configurations for Kafka Connect
    .
    ├── mongoose_java
    ├──├── src
    ├──├──├── main
    ├──├──├──├── java
    ├──├──├──├──├── kafka           # Java implementation of Kafka applications
    ├──├──├──├──├── resources
    .
    ├── mongoose_python
    ├──├── cassandra                # Cassandra applications, CQL code
    ├──├──├── cql
    ..
    ├──├── kafka                    # Python implementation of Kafka applications
    ├──├──├── ai-agent
    ├──├──├── data_acquisition
    ├──├──├── test_data
    .
    ├── monitoring                  # Monitoring applications for Kafka, Cassandra.
    ├──├── kafka-ui
    .
    ├── python-env                  # Python virtual environment files.
```

##### Apache Kafka
```
/kafka_2.13-3.3.1
    .
    ├── LICENSE
    ├── NOTICE
    ├── bin                   
    ├── config                  # config files containing customized .properties files
    ├── libs                    
    ├── licenses
    ├── logs
    └── site-docs
```

##### Apache Cassandra
```
/apache-cassandra-4.0.7
    .
    ├── LICENSE
    ├── NOTICE
    ├── CHANGES
    ├── NEWS
    ├── bin                   
    ├── conf                    # config files containing customized .properties, logback files
    ├── libs                    
    ├── data                    # keyspaces, tables, and associated data
    ├── doc
    ├── logs
    └── pylib
    └── tools
```
> The `conf` and `data` files contain customized configurations and data for keyspaces, tables.

##### DataStax Apache Kafka Connector
```
/kafka-connect/kafka-connect-cassandra-sink-1.4.0/
    .
    ├── LICENSE
    ├── README.md
    ├── THIRD-PARTY
    ├── conf
    ├── conf-prod
    └── kafka-connect-cassandra-sink-1.4.0.jar
```

##### Ports
| Application | Port | Edit File |
| ----------- | ---- | --------- |
| Cassandra | 9042 | apache-cassandra-4.0.7/conf/cassandra.yaml |
| Zookeeper | 2181 | kafka_2.13-3.3.1/config/server.properties|
| Kafka | 9092 | kafka_2.13-3.3.1/config/server.properties |
| Test Data Acquisition | 4444 | mongoose_python/kafka/data_acquisition/data_acquisition.py |
| UI for Apache Kafka | 8080 | monitoring/kafka-ui/docker-compose-kafka-ui.yaml |

##### Logging
| Framework | Log File | Location |
| ----------- | ---- | --------- |
| Cassandra | debug.log, system.log | apache-cassandra-4.0.7/logs/ |
| Kafka, Zookeeper Brokers | server.log, state-change.log | kafka_2.13-3.3.1/logs/ |
| Kafka Connect | connect.log | kafka_2.13-3.3.1/logs/ |
| (Java) Kafka Producer | logging.log | mongoose_java/ |
| (Python) Cassandra App | cassandra_setup.log | mongoose_python/cassandra/logs/ |
| (Python) Data Acquisition Service | data_acquisition.log | mongoose_python/kafka/data_acquisition/logs/ |
| (Python) AI Agent | consumer.log | monogoose_python/kafka/ai-agent/logs/ |

* Application
```
zookeeper-server-start.sh ~/mongooseAI/kafka_2.13-3.3.1/config/zookeeper.properties
```

* Start Kafka
```
cd /tmp/kafka-logs
rm -rf *                # Must clear logs first

kafka-server-start.sh ~/mongooseAI/kafka_2.13-3.3.1/config/server.properties
```

* Start Cassandra
```
bin/cassandra
```

* Start CQL
```
bin/cqlsh
```

* Start Kafka Connector
```
bin/connect-standalone.sh config/cassandra_sink/connect-standalone.properties config/cassandra_sink/cassandra-sink-standalone.properties
```
(Use *connectdistributed.sh* depending on situation)

**Note***: must edit .bashrc file and include path to `/kafka_2.13-3.3.1/bin/` to use shell commands without having to input full path*

* Automate in WSL2 Using Shell Script
```
cd mongooseAI/
bash launch.sh
```-

## Contact
Jae Sung Park.
16jpark@kaist.ac.kr
