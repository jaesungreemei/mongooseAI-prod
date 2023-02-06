CREATE TABLE IF NOT EXISTS mongoose_keyspace.analysis_by_metric_hour (
  sensor_id text,
  metric_category text,
  metric text,
  date date,
  min decimal,
  max decimal,
  median decimal,
  mean decimal,
  stdev decimal,
  PRIMARY KEY ((sensor_id, metric_category), date, metric)
)
WITH comment = 'Q7. Analysis Results'
AND CLUSTERING ORDER BY (date DESC, metric ASC) ;