import socket
import logging
import pathlib

from preprocess import preprocess_plc_by_sensor, preprocess_data_by_metric
from mongoose_python.kafka.ProducerApp import ProducerApp

######################################################
# Helper Functions, Helper Variables

# Current Directory Path
curr_dir = str(pathlib.Path(__file__).parent.resolve())

# https://stackoverflow.com/questions/2838244/get-open-tcp-port-in-python
def find_free_port():
    """
    Find an open TCP port on the local computer
    """
    with socket.socket() as s:
        s.bind(('', 0))            # Bind to a free port provided by the host.
        return s.getsockname()[1]  # Return the port number assigned.

######################################################
# Create logger for consumer (logs will be emitted when poll() is called)

logging.basicConfig(
    filename= curr_dir + '/logs/data_acquisition.log',
    level=logging.INFO,
    format= '[%(asctime)s] {%(pathname)s:%(lineno)d} %(levelname)s - %(message)s',
    datefmt='%H:%M:%S'
)

logger = logging.getLogger('data_acquisition')
handler = logging.StreamHandler()
handler.setLevel(logging.DEBUG)
handler.setFormatter(logging.Formatter('%(asctime)-15s %(levelname)-8s %(message)s'))
logger.addHandler(handler)

######################################################

def main(plc_producer, ip, port):
    
    # Connection to TCP socket input
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((ip, port))
        s.listen()

        print("Waiting for connection to {}:{}...".format(ip, port))
        conn, addr = s.accept()
        print("Connection to {}:{} established by {}".format(ip, port, addr))
        logging.info("Connection to {}:{} established by {}".format(ip, port, addr))

        while(True):
            data = conn.recv(2048)

            # Table: mongoose_keyspace.plc_by_sensor
            topic_plc_by_sensor = "plc_topic"
            msg_key, plc_data = preprocess_plc_by_sensor(data.decode())
            plc_producer.send_msg(topic_plc_by_sensor, msg_key, plc_data)

            # Table: mongoose_keyspace.data_by_metric
            topic_data_by_metric = "metric_topic"
            msg_key, metric_data = preprocess_data_by_metric(data.decode())
            plc_producer.send_msg(topic_data_by_metric, msg_key, metric_data)

        conn.close()

######################################################

if __name__=='__main__':
    ip = "127.0.0.1"
    # port = int(find_free_port())
    port = 4444
    config_file = curr_dir + "/../getting_started.ini"

    plc_producer = ProducerApp(logger, config_file)
    main(plc_producer, ip, port)