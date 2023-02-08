import json
from datetime import datetime

"""
Functions used to preprocess various types of data received in data_acquisition.py:
 - PLC
"""

######################################################

def preprocess_plc_by_sensor(data):
    """
    Keyspace: mongoose_keyspace
    Table: plc_by_sensor

    Input:
     - 'metric_category': 'plc',
     - 'collected_at': collected_at,
     - 'machine_id': machine_id,
     - 'process_id': process_id,
     - 'sensor_id': sensor_id,
     - 'machine_status': machine_status,
     - 'process_state': process_state,
     - 'process_count': process_count,
     - 'io': io
    
    Output:
     - 'sensor_id'
     - 'hour'
     - 'collected_at'
     - 'machine_id'
     - 'process_id'
     - 'machine_status'
     - 'process_state'
     - 'process_count'
     - 'io'
    """

    json_data = json.loads(data)
    
    # collected_at = json_data['collected_at']
    collected_at = datetime.now().isoformat()
    company_id = json_data['company_id']
    machine_id = json_data['machine_id']
    process_id = json_data['process_id']

    # Convert datetime to hour
    hour = datetime.fromisoformat(collected_at).replace(second=0, microsecond=0, minute=0).isoformat()

    # PLC data
    data = {
        'sensor_id': json_data['sensor_id'],
        'hour': hour,
        'collected_at': collected_at,
        'company_id': company_id,
        'machine_id': machine_id,
        'process_id': process_id,
        'machine_status': json_data['machine_status'],
        'process_state': json_data['process_state'],
        'process_count': json_data['process_count'],
        'io': json_data['io'],
    }

    return machine_id, json.dumps(data)

######################################################

def preprocess_data_by_metric(data):
    """
    Keyspace: mongoose_keyspace
    Table: data_by_metric

    Input (PLC Data):
     - 'metric_category': 'plc',
     - 'collected_at': collected_at,
     - 'machine_id': machine_id,
     - 'process_id': process_id,
     - 'sensor_id': sensor_id,
     - 'machine_status': machine_status,
     - 'process_state': process_state,
     - 'process_count': process_count,
     - 'io': io
    
    Output:
     - 'metric_category'
     - 'hour'
     - 'hour'
     - 'sensor_id'
     - 'machine_id'
     - 'process_id'
     - 'datetime'
     - 'metric'
     - 'value'
    """

    json_data = json.loads(data)
    
    # Extract necessary variables
    metric_category = json_data['metric_category']
    company_id = json_data['company_id']
    sensor_id = json_data['sensor_id']
    machine_id = json_data['machine_id']
    process_id = json_data['process_id']
    # collected_at = json_data['collected_at']
    collected_at = datetime.now().isoformat()

    del json_data['metric_category']
    del json_data['collected_at']
    del json_data['machine_id']
    del json_data['process_id']
    del json_data['sensor_id']

    # Convert datetime to hour
    hour = datetime.fromisoformat(collected_at).replace(second=0, microsecond=0, minute=0).isoformat()

    # Split each metric into its own data row entry
    all_data = list()

    for k,v in json_data.items():
        data = {
            'metric_category': metric_category,
            'hour': hour,
            'company_id': company_id,
            'sensor_id': sensor_id,
            'machine_id': machine_id,
            'process_id': process_id,
            'collected_at': collected_at
        }
        data['metric'] = k
        data['value'] = str(v)

        all_data.append(json.dumps(data))

    return machine_id, all_data

######################################################