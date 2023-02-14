# Mongoose AI
Kafka-Flink-Cassandra Data Pipeline Implementation for Mongoose AI.

## Full Documentation
* Production Details (mongoose-prod): https://docs.google.com/document/d/1FeQUUAP0NwxkRoNrd_TKdKlIQN2l_dvRfsZKIYPwlu4/edit?usp=sharing

## Table of Contents
- [(1) Overview](#1)
  * [(1.1) Prerequisites](#1-1)
  * [(1.2) Architecture](#1-2)
  * [(1.3) Usage](#1-3)
- [(2) Directory Structure](#2)
- [(3) Configurations](#3)
  * [(3.1) Ports](#3-1)
  * [(3.2) Logging](#3-2)
- [(4) Getting Started](#4)
  * [(4.1) Setup](#4-1)
  * [(4.2) Run Files](#4-2)
  * [(4.3) Edit Config Files](#4-3)
<!-- -------------------------------------------------------------------------------------------------------------------------------------------------- -->

## (1) Overview<a id="1"></a>

### (1.1) Prerequisites<a id="1-1"></a>
| Framework         | Version |
| ----------------- | ------------- |
| Apache Kafka      | 3.3.1  |
| Apache Cassandra  | 4.0.7  |
| DataStax Apache Kafka Connector | 1.4.0 |
| DataStax Driver for Apache Cassandra | 3.25 |
| Confluent Kafka Python | 2.0.2 |

* Development Environment: WSL2 Ubuntu 20.04.5 LTS on Windows 11

### (1.2) Architecture<a id="1-2"></a>
![Alt text](/architecture.jpg?raw=true "Data Pipeline Architecture")

### (1.3) Usage<a id="1-3"></a>
| Implemented? | Function | Pipeline | Description |
| -------- | -------- | -------- | ----------- |
| [x] | Store Raw Data | (Producer 1) → (Kafka Connect 1) | Store raw data immediately into DB without processing |
| [ ] | Raw Data Pre-Processing | (Producer 1) → (Flink) | Use Flink to preprocess raw data before storing |
| [ ] | Data Analysis | (Producer 1) → (Consumer 1) → (Producer 2) → (Kafka Connect 2) | Forward data to AI/ML engine, AI/ML engine sends back analysis through Kafka to be stored in DB |

<!-- -------------------------------------------------------------------------------------------------------------------------------------------------- -->

## (2) Directory Structure<a id="2"></a>

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

<!-- -------------------------------------------------------------------------------------------------------------------------------------------------- -->

## (3) Configurations<a id="3"></a>

### (3.1) Ports<a id="3-1"></a>
| Application | Port | Edit File |
| ----------- | ---- | --------- |
| Cassandra | 9042 | apache-cassandra-4.0.7/conf/cassandra.yaml |
| Zookeeper | 2181 | kafka_2.13-3.3.1/config/server.properties|
| Kafka | 9092 | kafka_2.13-3.3.1/config/server.properties |
| Test Data Acquisition | 4444 | mongoose_python/kafka/data_acquisition/data_acquisition.py |
| UI for Apache Kafka | 8080 | monitoring/kafka-ui/docker-compose-kafka-ui.yaml |

### (3.2) Logging<a id="3-2"></a>
| Framework | Log File | Location |
| ----------- | ---- | --------- |
| Cassandra | <ul><li>debug.log</li><li>system.log</li></ul> | apache-cassandra-4.0.7/logs/ |
| Kafka, Zookeeper Brokers | <ul><li>server.log</li><li>state-change.log</li></ul> | kafka_2.13-3.3.1/logs/ |
| Kafka Connect | <ul><li>connect.log</li></ul> | kafka_2.13-3.3.1/logs/ |
| (Java) Kafka Producer | <ul><li>logging.log</li></ul> | mongoose_java/ |
| (Python) Cassandra App | <ul><li>cassandra_setup.log</li></ul> | mongoose_python/cassandra/logs/ |
| (Python) Data Acquisition Service | <ul><li>data_acquisition.log</li></ul> | mongoose_python/kafka/data_acquisition/logs/ |
| (Python) AI Agent | <ul><li>consumer.log</li></ul> | monogoose_python/kafka/ai-agent/logs/ |

<!-- -------------------------------------------------------------------------------------------------------------------------------------------------- -->

## (4) Getting Started<a id="4"></a>

### (4-1) Setup<a id="4-1"></a>
* Install Kafka
> Starting Directory: mongooseAI-prod/
```
tar -xzf ./downloads/kafka_2.13-3.3.1.tgz
```

* Install Cassandra
> Starting Directory: mongooseAI-prod/
```
tar xzvf ./downloads/apache-cassandra-4.0.7-bin.tar.gz
```

##### Kafka Connector Setup
* Install DataStax Kafka Connector
> Starting Directory: mongooseAI-prod/
```
mkdir kafka-connect
cd ./kafka-connect/
tar xzf ../downloads/kafka-connect-cassandra-sink-1.4.0.tar.gz
```

* Configure connect-standalone.properties
> Starting Directory: mongooseAI-prod/kafka-connect/
```
cd ../kafka_2.13-3.3.1/config/

# Configure path to Kafka Connect JAR file
echo plugin.path=~/mongooseAI-prod/kafka-connect/kafka-connect-cassandra-sink-1.4.0/kafka-connect-cassandra-sink-1.4.0.jar > connect-standalone.properties
```

* Edit connect-standalone.properties
```
key.converter=org.apache.kafka.connect.storage.StringConverter
key.converter.schemas.enable=false
value.converter.schemas.enable=false
```

* Move and configure cassandra-sink-standalone.properties
> Starting Directory: mongooseAI-prod/kafka-conncet/
```
mkdir ./kafka-connect-cassandra-sink-1.4.0/conf-prod
cd kafka-connect-cassandra-sink-1.4.0/conf-prod
cp ../conf/cassandra-sink-standalone.properties.sample cassandra-sink-standalone.properties
```

* Edit cassandra-sink-standalone.properties according to Cassandra DB structure
> Details in mongooseAI-prod/kafka-connect/README.md

* Configure Kafka Connect logging
> kafka_2.13-3.3.1/config/connect-log4j.properties

##### Python Virtual Environment Setup
* Install Virtual Environment
> Starting Directory: mongooseAI-prod/
```
pip3 install virtualenv
python3 -m venv python-env

source python-env/bin/activate
```

* Install DataStax Cassandra Python Driver
```
pip3 install cassandra-driver
```

* Install Confluent Kafka Python Client
```
pip3 install confluent-kafka
```

### (4-2) Run Files<a id="4-2"></a>
* Start Servers
> Starting Directory: mongooseAI-prod/
| Shell File | Description |
| ---------- | ----------- |
| launch_wsl.sh | For development in WSL2 Environment <br><ul><li>Set bash PATH environment variable</li><li>Split panes</li></ul> |
| bin/run_cassandra.sh | <ul><li>Set Cassandra server</li></ul> |
| bin/run_zookeeper.sh | <ul><li>Set Zookeeper server</li></ul> |
| bin/run_kafka.sh | <ul><li>Delete all files in /tmp/</li><li>Start Kafka server</li></ul> |
| bin/run_connect.sh | <ul><li>Start Kafka Connect server</li></ul> |
| bin/run_venv.sh | <ul><li>Start Python virtual environment</li></ul> |

* Run Kafka Python Tests
> Starting Directory: mongooseAI-prod/mongoose_python/cassandra
| Directory | Shell File | Description |
| --------- | ---------- | ----------- |
| mongoose_python | run_acquisition.sh | <ul><li>Start data acquisition producer</li></ul> |
| mongoose_python | run_test.sh | <ul><li>Start test data generator</li><li>Send data via TCP socket to producer</li></ul> |
| mongoose_python | run_acquisition.sh | <ul><li>Start consumer for applications (eg. AI application) </li></ul> |

* Run Kafka Java Tests
> Starting Directory: mongooseAI-prod/mongoose_python/cassandra
| Directory | Shell File | Description |
| --------- | ---------- | ----------- |
| mongoose_java | run_producer.sh | <ul><li>Start data acquisition producer</li></ul> |
| mongoose_python | run_test.sh | <ul><li>Start test data generator</li><li>Send data via TCP socket to producer</li></ul> |
| mongoose_java | run_consumer.sh | <ul><li>Start consumer for applications (eg. AI application) </li></ul> |

* Monitoring Solutions
> Starting Directory: mongooseAI-prod/monitoring/
| Shell File | Description |
| ---------- | ----------- |
| kafka-ui/run_kafka_ui.sh | Start UI for Apache Kafka application |

### (4-3) Edited Config Files<a id="4-3"></a>
* Kafka Config Files: __kafka_2.13-3.3.1/config__
| Edited Files | Description |
| ------------ | ----------- |
| connect-log4j.properties | Add Kafka Connect log properties, Kafka log settings |
| server.properties | Configure 'listeners' for Kafka broker, Zookeeper broker |
| producer.properties | Configure list of brokers used for bootstrapping knowledge about cluster |
| consumer.properties | Configure list of brokers used for bootstrapping knowledge about cluster |
| connect-standalone.properties | Configure converters and schemas needed for Kafka Connect |
| kafka-server-start.sh | Check allocation of memory for Kafka server |

* Kafka Connect Config Files: __kafka-connect/kafka-connect-cassandra-sink-1.4.0/conf-prod__
| Edited Files | Description |
| ------------ | ----------- |
| cassandra-sink-standalone.properties | All configurations for standalone mode of Kafka Connect |

* Cassandra Config Files: __apache-cassandra-4.0.7/conf__
| Edited Files | Description |
| ------------ | ----------- |
| cassandra-env.sh | Check allocation of memory for Cassandra |
| cassandra.yaml | All Cassandra configurations |

<!-- -------------------------------------------------------------------------------------------------------------------------------------------------- -->



**Note***: must edit .bashrc file and include path to `/kafka_2.13-3.3.1/bin/` to use shell commands without having to input full path*

* Automate in WSL2 Using Shell Script
```
cd mongooseAI/
bash launch.sh
```

## Contact
Jae Sung Park.
16jpark@kaist.ac.kr
