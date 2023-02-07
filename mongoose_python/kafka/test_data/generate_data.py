import time
# from tqdm import tqdm
import random
import json

from datetime import datetime, timedelta
from get_data import get_CMS, get_PLC, get_Power

#############################################################################################

# Experimentation Parameters
num_machines = 3            # How many machines do we want to test?
num_processes_range = 3     # What is the maximum number of processes each machine can have?
d1 = datetime.strptime('1/1/2022 1:30 PM', '%m/%d/%Y %I:%M %p')
d2 = datetime.strptime('1/1/2022 2:30 PM', '%m/%d/%Y %I:%M %p')
company_id = "MONGOOSEAI"

#############################################################################################

# def random_date(start, end):
#     """
#     Return a random datetime between two datetime objects.
#     """

#     delta = end - start
#     int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
#     random_second = random.randrange(int_delta)
#     return start + timedelta(seconds=random_second)

def generate_data(company=company_id, num_machines=num_machines, num_processes=num_processes_range, date_range=(d1, d2)):
    """
    Generate random data for each of "Power", "PLC", "CMS" and combine into JSON format
    """

    ###############################
    # Generate random list of machines
    # Generate random list of processes for each machine
    # Assign sensor for each process for each machine

    machine_list = ["M"+str(i) for i in range(1, num_machines+1)]
    process_list = [list(range(1, random.randint(1, num_processes+1))) for _ in range(num_machines)]
    
    delta = date_range[1] - date_range[0]
    num_seconds = (delta.days * 24 * 60 * 60) + delta.seconds

    ###############################
    
    # Common Data:
    #  - DateTime                  =>  dt
    #  - 설비번호 = Machine Code    =>  machine_id      
    #  - 공정번호 = Process Code    =>  process_id
    
    power_data = list()
    plc_data = list()
    cms_data = list()

    sensor_num = 1

    # Iterate for each machine
    for i, machine_id in enumerate(machine_list):

        # Iterate for each process of each machine
        for process_id in process_list[i]:

            sensor_id = "S" + str(sensor_num)   # Assign a sensor for each process for each machine
            sensor_num += 1

            # For each second within the time range of data collected
            for sec in range(num_seconds):
                dt = d1 + timedelta(seconds=sec)
                # use_dt = dt.strftime("%m/%d/%Y %H:%M:%S")
                use_dt = dt.isoformat()

                power = get_Power(use_dt, company_id, machine_id, process_id, sensor_id)
                plc = get_PLC(use_dt, company_id, machine_id, process_id, sensor_id)
                cms = get_CMS(use_dt, company_id, machine_id, process_id, sensor_id)

                power_data.append(json.dumps(power))
                plc_data.append(json.dumps(plc))
                cms_data.append(json.dumps(cms))

    return power_data, plc_data, cms_data

#############################################################################################

if __name__=='__main__':
    power_data, plc_data, cms_data = generate_data()
    print(plc_data[0])