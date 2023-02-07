CREATE TABLE IF NOT EXISTS mongoose_keyspace.plc_by_hour (
  sensor_id text,
  month timestamp,
  hour timestamp,
  created_at timeuuid,
  machine_id text,
  process_id text,
  avg_machine_status decimal,
  total_machine_status decimal,
  avg_process_status decimal,
  total_process_status decimal,
  total_process_count decimal,
  all_io text,
  PRIMARY KEY ((sensor_id, month), hour)
)
WITH comment = 'Q8. PLC Data by Hour'
AND CLUSTERING ORDER BY (hour DESC) ;