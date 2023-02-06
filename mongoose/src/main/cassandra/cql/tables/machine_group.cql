CREATE TABLE IF NOT EXISTS mongoose_keyspace.machine_group (
  machine_code text,
  characteristics map<text, text>,
  description text STATIC,
  group_id text,
  PRIMARY KEY ((machine_code), group_id)
)
WITH comment = 'Q1. Machine Information'
AND CLUSTERING ORDER BY (group_id ASC) ;