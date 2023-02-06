CREATE TABLE IF NOT EXISTS mongoose_keyspace.sensors_by_group (
  group_id text,
  sensor_id text,
  characteristics map<text, text>,
  location text,
  PRIMARY KEY ((group_id), sensor_id)
)
WITH comment = 'Q4. IoT Sensor Groups'
AND CLUSTERING ORDER BY (sensor_id ASC) ;