import sys
import logging

from confluent_kafka import Consumer, KafkaError, KafkaException
from configparser import ConfigParser

######################################################

class ConsumerApp:
    consumer = None

    def __init__(self, logger, config_file):
        config_parser = ConfigParser()
        config_parser.read_file(open(config_file))

        config = dict(config_parser['default'])
        config.update(config_parser['consumer'])

        self.consumer = Consumer(config, logger=logger)

    def start_listener(self, topics):
        
        self.consumer.subscribe(topics)

        try:
            while(True):
                print("Listening...")

                msg = self.consumer.poll(timeout=1.0)

                if(msg is None):
                    continue

                elif(msg.error()):
                    logging.error(msg.error())
                    raise KafkaException(msg.error())
                    
                else:
                    # Proper message
                    msg_info = '{} [{}] at offset {} with key {}: {}'.format(msg.topic(), msg.partition(), msg.offset(), msg.key(), msg.value())
                    logging.info(msg_info)
                    print(msg_info)

                    # Store the offset associated with msg to a local cache.
                    # Stored offsets are committed to Kafka by a background thread every 'auto.commit.interval.ms'.
                    # Explicitly storing offsets after processing gives at-least once semantics.
                    # self.consumer.store_offsets(msg)

        except KeyboardInterrupt:
            logging.error('Aborted by user\n')
            sys.stderr.write('Aborted by user\n')

        finally:
            # Close down consumer to commit final offsets.
            self.consumer.close()

######################################################          