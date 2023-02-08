from confluent_kafka import Producer
from configparser import ConfigParser

from datetime import datetime
import logging

######################################################

class ProducerApp:
    producer = None

    def __init__(self, logger, config_file):
        config_parser = ConfigParser()
        config_parser.read_file(open(config_file))

        config = dict(config_parser['default'])
        config.update(config_parser['producer'])

        self.producer = Producer(config, logger=logger)

    def delivery_report(self, err, msg):
        """
        Called once for each message produced to indicate delivery result.
        Triggered by poll() or flush().
        """

        if(err is not None):
            err_msg = 'ERROR: Message delivery failed: {}'.format(err)
            logging.error(err_msg)
            print(err_msg)

        else:
            msg_timestamp = datetime.fromtimestamp(msg.timestamp()[-1] / 1000)

            success_msg = 'Produced Message ({}): [Topic: {}] [Partition: {}] [Key: {}] Value: {}'.format( msg_timestamp, msg.topic(), msg.partition(), msg.key(), msg.value() )
            logging.info(success_msg)
            print(success_msg)

    def send_msg(self, topic, use_key, msg):
        print("Sending message...")

        self.producer.produce(
            topic,
            key=use_key,
            value=msg,
            callback = lambda err, original_msg= msg: self.delivery_report(err, original_msg),
        )
        self.producer.flush()

######################################################