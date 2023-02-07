CREATE TABLE IF NOT EXISTS mongoose_keyspace.example (
  sensor_id uuid,
  created_at timestamp,
  io text,
  PRIMARY KEY ((sensor_id, created_at))
)
;