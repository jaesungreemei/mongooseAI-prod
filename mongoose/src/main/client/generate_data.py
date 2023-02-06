from datetime import datetime, timedelta
import time
# from tqdm import tqdm
import random
import json

# Data Specification:
# Key: String
# Value: JSON

#############################################################################################
# Power Data
#############################################################################################

def get_Power(dt, machine_code, process_code):
    '''
    Generate random Power data for given:
     - dt
     - machine_code 
     - process_code

    Data:
     - 전력량   = power (float)
     - 적산지침 = method (float)
     - 순간전력 = instant_power (float)
     - 전류량   = current (float)
     - 피크전력 = peak_power (float)
    '''
    power = random.random() * 100
    method = random.random() * 100
    instant_power = random.random() * 100
    current = random.random() * 100
    peak_power = random.random() * 100

    # Return as a Python dictionary
    data = {
        'metric_category': 'power',
        'dt': dt,
        'machine_code': machine_code,
        'process_code': process_code,
        'power': power,
        'method': method,
        'instant_power': instant_power,
        'current': current,
        'peak_power': peak_power
    }

    return data


#############################################################################################
# PLC Data
#############################################################################################

def get_PLC(dt, machine_code, process_code):
    '''
    Generate random PLC data for given:
     - dt
     - machine_code 
     - process_code

    Data:
     - 설비운전상태 = machine_status (bit)
     - 단위공정상태 = process_state (bit)
     - 단위공정횟수 = process_count (int)
     - I/O 데이터 = io (JSON)
    '''
    machine_status = random.randint(0, 1)
    process_state = random.randint(0, 1)
    process_count = int(random.random() * 100)
    io = json.dumps({'address': random.random() * 10000})

    # Return as a Python dictionary
    data = {
        'metric_category': 'plc',
        'dt': dt,
        'machine_code': machine_code,
        'process_code': process_code,
        'machine_status': machine_status,
        'process_state': process_state,
        'process_count': process_count,
        'io': io
    }

    return data


#############################################################################################
# CMS Data
#############################################################################################

def get_CMS(dt, machine_code, process_code):
    '''
    Generate random CMS data for given:
     - dt
     - machine_code 
     - process_code

    Data:
     - PeakToPeak = p2p (float)
     - Arms = arms (float)
     - Vrms = vrms (float)
     - CrestFactor = crestfactor (float)
     - Fault = fault (int)
    '''
    p2p = random.random() * 100
    arms = random.random() * 100
    vrms = random.random() * 100
    crestfactor = random.random() * 100
    fault = int(random.random() * 10)

    # Return as a Python dictionary
    data = {
        'metric_category': 'cms',
        'dt': dt,
        'machine_code': machine_code,
        'process_code': process_code,
        'p2p': p2p,
        'arms': arms,
        'vrms': vrms,
        'crestfactor': crestfactor,
        'fault': fault
    }

    return data

#############################################################################################
# Main Function
#############################################################################################

# Experimentation Parameters

num_machines = 3            # How many machines do we want to test?
num_processes_range = 3     # What is the maximum number of processes each machine can have?
d1 = datetime.strptime('1/1/2022 1:30 PM', '%m/%d/%Y %I:%M %p')
d2 = datetime.strptime('1/1/2022 2:30 PM', '%m/%d/%Y %I:%M %p')

# def random_date(start, end):
#     """
#     Return a random datetime between two datetime objects.
#     """

#     delta = end - start
#     int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
#     random_second = random.randrange(int_delta)
#     return start + timedelta(seconds=random_second)


def generate_data(num_machines=num_machines, num_processes=num_processes_range, date_range=(d1, d2)):
    """
    Generate random data for each of "Power", "PLC", "CMS" and combine into JSON format
    """

    ###############################
    # Generate random list of machines
    # Generate random list of processes for each machine

    machine_list = ["M"+str(i) for i in range(1, num_machines+1)]
    process_list = [list(range(1, random.randint(2, num_processes+1))) for _ in range(num_machines)]
    
    delta = date_range[1] - date_range[0]
    num_seconds = (delta.days * 24 * 60 * 60) + delta.seconds

    ###############################
    
    # Common Data:
    #  - DateTime                  =>  dt
    #  - 설비번호 = Machine Code    =>  machine_code      
    #  - 공정번호 = Process Code    =>  process_code
    
    power_data = list()
    plc_data = list()
    cms_data = list()

    # Iterate for each machine
    for i in range(num_machines):
        machine_code = machine_list[i]

        # Iterate for each process of each machine
        for process_code in process_list[i]:

            # For each second within the time range of data collected
            for sec in range(num_seconds):
                dt = d1 + timedelta(seconds=sec)
                # use_dt = dt.strftime("%m/%d/%Y %H:%M:%S")
                use_dt = dt.isoformat()

                power = get_Power(use_dt, machine_code, process_code)
                plc = get_PLC(use_dt, machine_code, process_code)
                cms = get_CMS(use_dt, machine_code, process_code)

                power_data.append(json.dumps(power))
                plc_data.append(json.dumps(plc))
                cms_data.append(json.dumps(cms))

    return power_data, plc_data, cms_data

#############################################################################################

if __name__=='__main__':
    power_data, plc_data, cms_data = generate_data()
    print(plc_data[0])