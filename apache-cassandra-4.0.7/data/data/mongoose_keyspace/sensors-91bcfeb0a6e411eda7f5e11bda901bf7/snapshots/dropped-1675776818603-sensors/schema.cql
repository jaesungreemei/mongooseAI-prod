CREATE TABLE IF NOT EXISTS mongoose_keyspace.sensors (
    sensor_id text,
    machine_id text,
    process_id text,
    location text,
    characteristics map<text, text>,
    PRIMARY KEY (sensor_id, machine_id, process_id)
) WITH ID = 91bcfeb0-a6e4-11ed-a7f5-e11bda901bf7
    AND CLUSTERING ORDER BY (machine_id ASC, process_id ASC)
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
