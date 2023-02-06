CREATE TABLE IF NOT EXISTS mongoose_keyspace.data_by_metric (
  group_id text,
  metric_category text,
  metric text,
  dt timestamp,
  sensor_id text,
  value decimal,
  PRIMARY KEY ((group_id, metric_category), metric, dt, sensor_id)
)
WITH comment = 'Q6. Query Data By Metric'
AND CLUSTERING ORDER BY (metric ASC, dt DESC, sensor_id ASC) ;