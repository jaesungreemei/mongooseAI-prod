CREATE TABLE IF NOT EXISTS mongoose_keyspace.process_group (
  company_id text,
  process_id text,
  created_at timeuuid,
  description text,
  PRIMARY KEY ((company_id), process_id)
)
WITH comment = 'Q2. Processes Information'
AND CLUSTERING ORDER BY (process_id ASC) ;