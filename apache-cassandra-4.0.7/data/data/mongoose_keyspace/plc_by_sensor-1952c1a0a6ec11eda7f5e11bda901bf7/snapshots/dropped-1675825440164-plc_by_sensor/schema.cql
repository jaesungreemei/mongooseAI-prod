CREATE TABLE IF NOT EXISTS mongoose_keyspace.plc_by_sensor (
    sensor_id text,
    hour timestamp,
    collected_at timestamp,
    machine_id text,
    process_id text,
    created_at timeuuid,
    io text,
    machine_status int,
    process_count int,
    process_status int,
    PRIMARY KEY ((sensor_id, hour), collected_at, machine_id, process_id)
) WITH ID = 1952c1a0-a6ec-11ed-a7f5-e11bda901bf7
    AND CLUSTERING ORDER BY (collected_at DESC, machine_id ASC, process_id ASC)
    AND additional_write_policy = '99p'
    AND bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND cdc = false
    AND comment = 'Q5. Query PLC Data By Sensors'
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
    AND crc_check_chance = 1.0
    AND default_time_to_live = 7890000
    AND extensions = {}
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair = 'BLOCKING'
    AND speculative_retry = '99p';
