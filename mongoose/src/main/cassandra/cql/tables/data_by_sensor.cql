CREATE TABLE IF NOT EXISTS mongoose_keyspace.data_by_sensor (
  group_id text,
  sensor_id text,
  dt timestamp,
  metric_category text,
  metric text,
  value decimal,
  PRIMARY KEY ((group_id, sensor_id), dt, metric_category, metric)
)
WITH comment = 'Q5. Query Data By Sensors'
AND CLUSTERING ORDER BY (dt DESC, metric_category ASC, metric ASC) ;