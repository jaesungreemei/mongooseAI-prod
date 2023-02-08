CREATE TABLE IF NOT EXISTS mongoose_keyspace.analysis_by_metric_hour (
    sensor_id text,
    metric_category text,
    metric text,
    date timestamp,
    created_at timeuuid,
    max decimal,
    mean decimal,
    median decimal,
    min decimal,
    stdev decimal,
    PRIMARY KEY ((sensor_id, metric_category), metric, date)
) WITH ID = 644c1be0-a75d-11ed-b90d-e315afdd89a7
    AND CLUSTERING ORDER BY (metric ASC, date DESC)
    AND additional_write_policy = '99p'
    AND bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND cdc = false
    AND comment = 'Q7. Analysis Results'
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
