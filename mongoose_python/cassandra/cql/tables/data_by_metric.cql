CREATE TABLE IF NOT EXISTS mongoose_keyspace.data_by_metric (
  metric_category text,
  hour timestamp,
  sensor_id text,
  machine_id text,
  process_id text,
  collected_at timestamp,
  created_at timeuuid,
  metric text,
  value text,
  PRIMARY KEY ((metric_category, hour), sensor_id, machine_id, process_id, collected_at)
)
WITH comment = 'Q6. Query Data By Metric'
AND default_time_to_live = 7890000
AND CLUSTERING ORDER BY (sensor_id ASC, machine_id ASC, process_id ASC, collected_at DESC) ;