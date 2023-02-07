# Mongoose AI

Kafka-Flink-Cassandra Data Pipeline Implementation for Mongoose AI.

## Documentation
* Mongoose AI Trello: https://trello.com/b/4K66AAUE/mongooseai
*Trello Workspace contains all links to all documnetation.*

## Prerequisites
| Framework         | Version |
| ----------------- | ------------- |
| Apache Kafka      | 3.3.1  |
| Apache Cassandra  | 4.0.7  |
| DataStax Apache Kafka Connector | 1.4.0 |
| DataStax Cassandra Python Driver | 3.25 |

* Development Environment: WSL2 Ubuntu 20.04.5 LTS on Windows 11

## Directory Structure

##### Apache Kafka
```
/kafka_2.13-3.3.1
    .
    ├── LICENSE
    ├── NOTICE
    ├── bin                   
    ├── config                  # config files containing customized .properties files
    ├── libs                    
    ├── connectors              # new location of extracted connector files
    ├── licenses
    ├── logs
    └── site-docs
```
> The only differences from the original Apache Kafka directory structure is in the `config` and `connectors` directories. 

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
/kafka_2.13-3.3.1/connectors/kafka-connect-cassandra
    .
    ├── LICENSE
    ├── README.md
    ├── THIRD-PARTY
    ├── conf
    └── kafka-connect-cassandra-sink-1.4.0.jar
```
> DataStax Apache Kafka Connector must be located within the Kafka directory structure, in the "connectors" directory.

##### Downloads
* Apache Cassandra: https://cassandra.apache.org/_/download.html
* Apache Kafka: https://kafka.apache.org/downloads
* Kafka Connect: https://docs.datastax.com/en/kafka/doc/kafka/install/kafkaInstall.html
> *downloads*: Directory with tar files used in this repository.


## Shell Commands
* Start Zookeeper
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
(Use *connect-distributed.sh* depending on situation)

**Note***: must edit .bashrc file and include path to `/kafka_2.13-3.3.1/bin/` to use shell commands without having to input full path*

* Automate in WSL2 Using Shell Script
```
cd mongooseAI/
bash launch.sh
```

## Contact
Jae Sung Park.
16jpark@kaist.ac.kr
