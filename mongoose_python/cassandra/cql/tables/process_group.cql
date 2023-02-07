CREATE TABLE IF NOT EXISTS mongoose_keyspace.process_group (
  process_id text,
  created_at timeuuid,
  characteristics map<text, text>,
  description text,
  PRIMARY KEY ((process_id))
)
WITH comment = 'Q2. Processes Information' ;