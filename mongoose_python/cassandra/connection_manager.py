#!/usr/bin/env python

from cassandra.cluster import Cluster
from cassandra.auth    import PlainTextAuthProvider
from cassandra.query   import SimpleStatement
from cassandra.policies import DCAwareRoundRobinPolicy

########################################################################

### connection variables for on-prem Cassandra or DSE
CONTACT_POINTS = ["127.0.0.1"]
PORT = 9042
USERNAME = "cassandra"
PASSWORD = "cassandra"

# DB Settings
KEYSPACE = "mongoose_keyspace"

########################################################################

class cassandraConnect:

    def __init__(self):
        """
        Initialize Cassandra Cluster and begin session by connecting
        """

        # AUTH_PROVIDER = PlainTextAuthProvider(username=USERNAME, password=PASSWORD)
        self.cluster = Cluster(
            contact_points = CONTACT_POINTS,
            port = PORT,
            load_balancing_policy = DCAwareRoundRobinPolicy(local_dc='datacenter1'),
            protocol_version = 3
            # auth_provider = AUTH_PROVIDER
        )
        # self.session = self.cluster.connect(KEYSPACE)
        self.session = self.cluster.connect()

    def disconnect(self):
        """
        Disconnect from Cassandra
        """

        self.cluster.shutdown()
        self.session.shutdown()
        return (0)

########################################################################