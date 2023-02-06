---------------------------------------------------------------------
-- Master Tables (data generated via Python code)
---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS mongoose_keyspace.machine_group (
  machine_code text,
  characteristics map<text, text>,
  description text STATIC,
  group_id text,
  PRIMARY KEY ((machine_code), (process_code))
  WITH comment = 'Q1. Machine Information'
  AND CLUSTERING ORDER BY (process_code ASC) ;

CREATE TABLE IF NOT EXISTS mongoose_keyspace.process_group (
  process_code text,
  characteristics map<text, text>,
  description text STATIC,
  group_id text,
  PRIMARY KEY ((process_code), (machine_code))
  WITH comment = 'Q2. Processes Information'
  WITH CLUSTERING ORDER BY (machine_code ASC) ;

CREATE TABLE IF NOT EXISTS mongoose_keyspace.sensors_by_group (
  group_id text,
  sensor_id text,
  characteristics map<text, text>,
  location text,
  PRIMARY KEY ((group_id), (sensor_id))
  WITH comment = 'Q4. IoT Sensor Groups'
  WITH CLUSTERING ORDER BY (sensor_id ASC) ;

CREATE TABLE IF NOT EXISTS mongoose_keyspace.metrics (
  metric_category text,
  metric text,
  unit text,
  PRIMARY KEY ((metric_category), (metric))
  WITH comment = 'Q3. Types of Data Metrics Measured'
  WITH CLUSTERING ORDER BY (metric ASC) ;

---------------------------------------------------------------------
-- Data Tables (incoming data from IoT sensors)
---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS mongoose_keyspace.data_by_sensor (
  group_id text,
  sensor_id text,
  dt timestamp,
  metric_category text,
  metric text,
  value decimal,
  PRIMARY KEY ((group_id, sensor_id), (dt, metric_category, metric))
  WITH comment = 'Q5. Query Data By Sensors'
  WITH CLUSTERING ORDER BY (dt DESC, metric_category ASC, metric ASC) ;

CREATE TABLE IF NOT EXISTS mongoose_keyspace.data_by_metric (
  group_id text,
  metric_category text,
  metric text,
  dt timestamp,
  sensor_id text,
  value decimal,
  PRIMARY KEY ((group_id, metric_category), (metric, dt, sensor_id))
  WITH comment = 'Q6. Query Data By Metric'
  WITH CLUSTERING ORDER BY (metric ASC, dt DESC, sensor_id ASC) ;

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
  PRIMARY KEY ((sensor_id, metric_category), (metric, date))
  WITH comment = 'Q7. Analysis Results'
  WITH CLUSTERING ORDER BY (metric ASC, dt DESC, sensor_id ASC) ;

---------------------------------------------------------------------
-- Apache Cassandra TTL for tables (specified in seconds)
---- data tables can grow big quickly.
---- TIME_TO_LIVE (TTL) setting is used to keep them under control.
---------------------------------------------------------------------
-- EXAMPLE: 1296000 seconds = 15 days * 24 hours * 60 minutes * 60 seconds

-- ALTER TABLE mongoose_keyspace.data_by_sensor WITH default_time_to_live = 1296000 ;
-- ALTER TABLE mongoose_keyspace.data_by_metric WITH default_time_to_live = 1296000 ;
