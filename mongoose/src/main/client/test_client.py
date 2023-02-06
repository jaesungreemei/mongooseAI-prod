import socket
import json
import time

from datetime import datetime
from generate_data import generate_data


# Experimentation Parameters
num_machines = 3            # How many machines do we want to test?
num_processes_range = 3     # What is the maximum number of processes each machine can have?
d1 = datetime.strptime('1/1/2022 1:30 PM', '%m/%d/%Y %I:%M %p')
d2 = datetime.strptime('1/1/2022 2:30 PM', '%m/%d/%Y %I:%M %p')

########################################################################

def main(ip, port):
    _, plc_data, _ = generate_data()

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((ip, port))

        for data in plc_data:

            # Extract Important Features from JSON Data
            json_data = json.loads(data)
            
            metric_category = json_data['metric_category']
            dt = json_data['dt']
            machine_code = json_data['machine_code']
            process_code = json_data['process_code']

            # Send Data to Connection
            s.sendall(bytes(data + '\n', encoding="utf-8"))
            print('Sent Data: [{}] {} - {} - {}'.format(metric_category, dt, machine_code, process_code))

            # Add delay
            time.sleep(2)

        s.close()

########################################################################

if __name__=='__main__':
    ip = "127.0.0.1"
    port = 4444

    main(ip, port)