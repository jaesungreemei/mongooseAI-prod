CREATE TABLE IF NOT EXISTS mongoose_keyspace.sensors (
    company_id text,
    sensor_id text,
    machine_id text,
    process_id text,
    created_at timeuuid,
    location text,
    PRIMARY KEY (company_id, sensor_id, machine_id, process_id)
) WITH ID = 6497a3d0-a75d-11ed-b90d-e315afdd89a7
    AND CLUSTERING ORDER BY (sensor_id ASC, machine_id ASC, process_id ASC)
    AND additional_write_policy = '99p'
    AND bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND cdc = false
    AND comment = 'Q4. IoT Sensor Groups'
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
    AND crc_check_chance = 1.0
    AND default_time_to_live = 0
    AND extensions = {}
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair = 'BLOCKING'
    AND speculative_retry = '99p';
