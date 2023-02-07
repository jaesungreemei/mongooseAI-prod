CREATE TABLE IF NOT EXISTS mongoose_keyspace.plc_by_hour (
    sensor_id text,
    month timestamp,
    hour timestamp,
    all_io text,
    avg_machine_status decimal,
    avg_process_status decimal,
    machine_id text,
    process_id text,
    total_machine_status decimal,
    total_process_count decimal,
    total_process_status decimal,
    PRIMARY KEY ((sensor_id, month), hour)
) WITH ID = 7af7d510-a6e4-11ed-a7f5-e11bda901bf7
    AND CLUSTERING ORDER BY (hour DESC)
    AND additional_write_policy = '99p'
    AND bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND cdc = false
    AND comment = 'Q8. PLC Data by Hour'
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
