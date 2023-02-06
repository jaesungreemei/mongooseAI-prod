CREATE TABLE IF NOT EXISTS mongoose_keyspace.metrics (
  metric_category text,
  metric text,
  unit text,
  PRIMARY KEY ((metric_category), metric)
)
WITH comment = 'Q3. Types of Data Metrics Measured'
AND CLUSTERING ORDER BY (metric ASC) ;