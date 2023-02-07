CREATE TABLE IF NOT EXISTS mongoose_keyspace.machine_group (
  company_id text,
  machine_id text,
  created_at timeuuid,
  description text,
  PRIMARY KEY ((company_id), machine_id)
)
WITH comment = 'Q1. Machine Information'
AND CLUSTERING ORDER BY (machine_id ASC) ;