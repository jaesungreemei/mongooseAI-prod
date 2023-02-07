CREATE TABLE IF NOT EXISTS mongoose_keyspace.machine_group (
  machine_id text,
  created_at timeuuid,
  characteristics map<text, text>,
  description text,
  PRIMARY KEY ((machine_id))
)
WITH comment = 'Q1. Machine Information' ;