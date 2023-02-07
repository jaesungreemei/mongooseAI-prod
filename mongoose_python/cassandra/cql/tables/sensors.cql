CREATE TABLE IF NOT EXISTS mongoose_keyspace.sensors (
  sensor_id text,
  machine_id text,
  process_id text,
  created_at timeuuid,
  characteristics map<text, text>,
  location text,
  PRIMARY KEY ((sensor_id), machine_id, process_id)
)
WITH comment = 'Q4. IoT Sensor Groups'
AND CLUSTERING ORDER BY (machine_id ASC, process_id ASC) ;