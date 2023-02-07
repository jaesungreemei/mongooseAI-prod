#!/usr/bin/env python

from connection_manager import cassandraConnect
from cassandra import ConsistencyLevel
from cassandra.util import datetime_from_uuid1
from cassandra.query import SimpleStatement

import sys
import os
import pathlib

import uuid

######################################################

# Current Directory Path
curr_dir = str(pathlib.Path(__file__).parent.resolve())

# Connection Variables
CONTACT_POINTS = ["127.0.0.1"]
PORT = 9042

# DB Variables
KEYSPACE = "mongoose_keyspace"

######################################################
# HELPER FUNCTIONS

def execute_cql_file(cc, file):
    """
    Execute all queries located in cql file, delimited by ';'
    """
    
    with open(file, mode="r") as f:
        txt = f.read()
        queries = txt.split(r";")

        for q in queries:
            query = q.strip()

            if(query != ""):
                cc.session.execute(query)
    return

########################################################################

def setup(cc, tables_path):
    """
    Given paths to directories with CQL queries for keyspace, tables:
     - check if keyspace, tables exist in Cassandra
     - if not, create them.
    """
    
    # Query Definition
    table_query = "SELECT table_name FROM system_schema.tables WHERE keyspace_name='{}';".format(KEYSPACE)
    table_rows = cc.session.execute(table_query)
    
    # Set keyspace
    cc.session.set_keyspace(KEYSPACE)

    if not ("example" in [row[0] for row in table_rows]):
        execute_cql_file(cc, tables_path)

    return

########################################################################

def main():

    cc = cassandraConnect()
    print("------------------------------------------------------")
    print("Connected to Cassandra Successfully: {} {}".format(CONTACT_POINTS, PORT))
    print("------------------------------------------------------")

    # Variables
    tables_path = curr_dir + "/example/example.cql"

    # Create keyspace, tables if needed.
    setup(cc, tables_path)

    # put in 5 sensor reads right now

    key = uuid.uuid4()
    for i in range(5):
        ts = uuid.uuid1()
        ts_datetime = datetime_from_uuid1(ts)

        print("ts: {}, ts_datetime: {}".format(ts, ts_datetime))

        query = """INSERT INTO mongoose_keyspace.example 
                    (sensor_id, created_at, io)
                    VALUES (%(sensor_id)s, %(created_at)s, %(io)s)"""
        values = {'sensor_id':key,
                'created_at': ts_datetime,
                'io': "random sample {}".format(i)}
        cc.session.execute(query, values)

    # Disconnect 
    cc.disconnect()

    print("------------------------------------------------------")

########################################################################

if __name__ == '__main__':
    main()