import random
import json
from datetime import datetime

# Data Specification:
# Key: String
# Value: JSON

#############################################################################################
# Power Data
#############################################################################################

def get_Power(dt, company_id, machine_id, process_id, sensor_id):
    '''
    Generate random Power data for given:
     - dt
     - machine_id 
     - process_id

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
        'company_id': company_id,
        'collected_at': dt,
        'machine_id': machine_id,
        'process_id': process_id,
        'sensor_id': sensor_id,
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

def get_PLC(dt, company_id, machine_id, process_id, sensor_id):
    '''
    Generate random PLC data for given:
     - dt
     - machine_id 
     - process_id

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
        'company_id': company_id, 
        'collected_at': dt,
        'machine_id': machine_id,
        'process_id': process_id,
        'sensor_id': sensor_id,
        'machine_status': machine_status,
        'process_state': process_state,
        'process_count': process_count,
        'io': io
    }

    return data


#############################################################################################
# CMS Data
#############################################################################################

def get_CMS(dt, company_id, machine_id, process_id, sensor_id):
    '''
    Generate random CMS data for given:
     - dt
     - machine_id 
     - process_id

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
        'company_id': company_id,
        'collected_at': dt,
        'machine_id': machine_id,
        'process_id': process_id,
        'sensor_id': sensor_id,
        'p2p': p2p,
        'arms': arms,
        'vrms': vrms,
        'crestfactor': crestfactor,
        'fault': fault
    }

    return data

#############################################################################################