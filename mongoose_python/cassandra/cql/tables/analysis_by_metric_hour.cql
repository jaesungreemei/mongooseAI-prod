CREATE TABLE IF NOT EXISTS mongoose_keyspace.analysis_by_metric_hour (
  sensor_id text,
  metric_category text,
  metric text,
  date timestamp,
  created_at timeuuid,
  min decimal,
  max decimal,
  median decimal,
  mean decimal,
  stdev decimal,
  PRIMARY KEY ((sensor_id, metric_category), metric, date)
)
WITH comment = 'Q7. Analysis Results'
AND CLUSTERING ORDER BY (metric ASC, date DESC) ;