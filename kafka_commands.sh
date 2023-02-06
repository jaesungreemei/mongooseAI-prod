<KafkaBrokerHost>:<KafkaBrokerPort>

# List / Describe all Topics
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe

# Topics
bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --create
bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --create --partitions 3 --replication-factor 2
bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --delete

# Producers
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic first_topic
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic first_topic --producer-property acks=all
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic first_topic --property parse.key=true --property key.separator=:

# Consumers
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --from-beginning
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --from-beginning

# Consumer Groups
    # Create Consumer Groups
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --group my-first-application
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --group my-first-application

    # Monitor Consumer Groups
    bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
    bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my-first-application

    # Delete Consumer Groups
    bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-second-application --delete

# Resetting Offset
    # Reset offset for all topics in consumer group
    bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --execute --all-topics
    # Reset offset for particular topic in consumer group
    bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --execute --topic first_topic

    # Shift offset by certain number
    bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --shift-by -2 --execute --topic first_topic


# Partition Size, Topic Size
bin/kafka-log-dirs.sh --bootstrap-server localhost:9092 --describe --topic-list <YourTopic>