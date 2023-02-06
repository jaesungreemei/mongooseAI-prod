CREATE TABLE IF NOT EXISTS mongoose_keyspace.process_group (
  process_code text,
  characteristics map<text, text>,
  description text STATIC,
  group_id text,
  PRIMARY KEY ((process_code), group_id)
)
WITH comment = 'Q2. Processes Information'
AND CLUSTERING ORDER BY (group_id ASC) ;