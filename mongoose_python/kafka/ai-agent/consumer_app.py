import pathlib
import logging

import sys
sys.path.insert(0, "/home/greemei/mongooseAI-prod/mongoose_python/kafka")

from ConsumerApp import ConsumerApp

######################################################
# Helper Functions, Helper Variables

curr_dir = str(pathlib.Path(__file__).parent.resolve())

######################################################
# Create logger for consumer (logs will be emitted when poll() is called)

logging.basicConfig(
    filename=curr_dir + '/logs/consumer.log',
    level=logging.INFO,
    format= '[%(asctime)s] {%(pathname)s:%(lineno)d} %(levelname)s - %(message)s',
    datefmt='%H:%M:%S'
)

logger = logging.getLogger('consumer')
handler = logging.StreamHandler()
handler.setLevel(logging.DEBUG)
handler.setFormatter(logging.Formatter('%(asctime)-15s %(levelname)-8s %(message)s'))
logger.addHandler(handler)

######################################################

def main(consumer_app):
    
    # Kafka Variables
    topics = ["plc_topic"]
    consumer_app.start_listener(topics)

######################################################

if __name__=='__main__':
    config_file = curr_dir + "/../getting_started.ini"

    consumer_app = ConsumerApp(logger, config_file)
    main(consumer_app)