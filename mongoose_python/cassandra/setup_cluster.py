#!/usr/bin/env python

from connection_manager import cassandraConnect
from cassandra import ConsistencyLevel
from cassandra.query import SimpleStatement

import logging
import os
import pathlib


######################################################

# Current Directory Path
curr_dir = str(pathlib.Path(__file__).parent.resolve())

# Connection Variables
CONTACT_POINTS = ["127.0.0.1"]
PORT = 9042

# DB Variables
KEYSPACE = "mongoose_keyspace"

######################################################
# Create logger for consumer (logs will be emitted when poll() is called)

logging.basicConfig(
    filename= curr_dir + '/logs/cassandra_setup.log',
    level=logging.INFO,
    format= '[%(asctime)s] {%(pathname)s:%(lineno)d} %(levelname)s - %(message)s',
    datefmt='%H:%M:%S'
)

logger = logging.getLogger('cassandra_setup')
handler = logging.StreamHandler()
handler.setLevel(logging.DEBUG)
handler.setFormatter(logging.Formatter('%(asctime)-15s %(levelname)-8s %(message)s'))
logger.addHandler(handler)

######################################################
# HELPER FUNCTIONS

def get_tables(path):
    """
    Given a path to directory with CQL queries for tables, return:
     - list of table names
     - list of corresponding cql files paths
    """
    all_files = [f for f in os.listdir(path) if os.path.isfile(os.path.join(path, f))]

    table_names = [x[:-4] for x in all_files]
    table_files = [path + y for y in all_files]

    return table_names, table_files


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
                logger.info("Executing query: " + file)
                cc.session.execute(query)
    return

########################################################################

def setup(cc, keyspace_path, tables_path):
    """
    Given paths to directories with CQL queries for keyspace, tables:
     - check if keyspace, tables exist in Cassandra
     - if not, create them.
    """
    
    # Query Definition
    keyspace_query = "SELECT keyspace_name FROM system_schema.keyspaces;"
    table_query = "SELECT table_name FROM system_schema.tables WHERE keyspace_name='{}';".format(KEYSPACE)

    keyspace_rows = cc.session.execute(keyspace_query)
    table_rows = cc.session.execute(table_query)

    # Create 'mongoose_keyspace' (if not exists)
    if not (KEYSPACE in [row[0] for row in keyspace_rows]):
        execute_cql_file(cc, keyspace_path)
    
    # Set keyspace
    cc.session.set_keyspace(KEYSPACE)

    # Create tables if they do not exist
    table_names, table_files = get_tables(tables_path)

    for i, table in enumerate(table_names):
        if not (table in [row[0] for row in table_rows]):
            execute_cql_file(cc, table_files[i])

    return

########################################################################

def main():

    cc = cassandraConnect()
    print("------------------------------------------------------")
    print("Connected to Cassandra Successfully: {} {}".format(CONTACT_POINTS, PORT))
    print("------------------------------------------------------")

    # Variables
    keyspace_path = curr_dir + "/cql/mongoose_keyspace.cql"
    tables_path = curr_dir + "/cql/tables/"

    # Create keyspace, tables if needed.
    setup(cc, keyspace_path, tables_path)

    # Disconnect 
    cc.disconnect()

    print("------------------------------------------------------")

########################################################################

if __name__ == '__main__':
    main()