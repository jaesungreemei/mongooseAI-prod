CREATE TABLE IF NOT EXISTS mongoose_keyspace.sensors (
  company_id text,
  sensor_id text,
  machine_id text,
  process_id text,
  created_at timeuuid,
  location text,
  PRIMARY KEY ((company_id), sensor_id, machine_id, process_id)
)
WITH comment = 'Q4. IoT Sensor Groups'
AND CLUSTERING ORDER BY (sensor_id ASC, machine_id ASC, process_id ASC) ;