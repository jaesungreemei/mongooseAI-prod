from confluent_kafka import Consumer, KafkaError, KafkaException

import sys
import logging

######################################################
# Global Variables

conf = {
    'bootstrap.servers': "localhost:9092",
    'group.id': "plc_consume",
    'auto.offset.reset': "earliest",
    'session.timeout.ms': 6000,
    'enable.auto.offset.store': False
}
running = True
topics = ['plc_topic']

######################################################
# Create logger for consumer (logs will be emitted when poll() is called)

logger = logging.getLogger('consumer')
logger.setLevel(logging.DEBUG)
handler = logging.StreamHandler()
handler.setFormatter(logging.Formatter('%(asctime)-15s %(levelname)-8s %(message)s'))
logger.addHandler(handler)

######################################################

def print_assignment(consumer, partitions):
    print('Assignment:', partitions)

def consume_data(consumer):
    try:
        while(True):
            msg = consumer.poll(timeout=1.0)
            if(msg is None):
                continue

            if(msg.error()):
                raise KafkaException(msg.error())
            else:
                # Proper message
                sys.stderr.write('%% %s [%d] at offset %d with key %s:\n' %
                                 (msg.topic(), msg.partition(), msg.offset(),
                                  str(msg.key())))
                print(msg.value())
                # Store the offset associated with msg to a local cache.
                # Stored offsets are committed to Kafka by a background thread every 'auto.commit.interval.ms'.
                # Explicitly storing offsets after processing gives at-least once semantics.
                consumer.store_offsets(msg)

    except KeyboardInterrupt:
        sys.stderr.write('%% Aborted by user\n')

    finally:
        # Close down consumer to commit final offsets.
        consumer.close()

######################################################

def main():

    c = Consumer(conf, logger=logger)
    c.subscribe(topics, on_assign=print_assignment)
    consume_data(c)

    return

if __name__=='__main__':
    main()