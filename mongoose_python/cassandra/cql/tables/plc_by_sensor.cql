CREATE TABLE IF NOT EXISTS mongoose_keyspace.plc_by_sensor (
  sensor_id text,
  hour timestamp,
  collected_at timestamp,
  machine_id text,
  process_id text,
  created_at timeuuid,
  machine_status int,
  process_status int,
  process_count int,
  io text,
  PRIMARY KEY ((sensor_id, hour), collected_at, machine_id, process_id)
)
WITH comment = 'Q5. Query PLC Data By Sensors'
AND default_time_to_live = 7890000
AND CLUSTERING ORDER BY (collected_at DESC, machine_id ASC, process_id ASC) ;