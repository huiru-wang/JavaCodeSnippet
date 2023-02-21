package com.atguigu.chapter05;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

public class SinkToRedisTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Event> stream = env.addSource(new ClickSource());

        // 创建一个到redis连接的配置
        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder()
                .setHost("hadoop102")
                .build();

        stream.addSink(new RedisSink<Event>(conf, new MyRedisMapper()));

        env.execute();
    }
    public static class MyRedisMapper implements RedisMapper<Event> {
        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET, "clicks");
        }

        @Override
        public String getKeyFromData(Event data) {
            return data.user;
        }

        @Override
        public String getValueFromData(Event data) {
            return data.url;
        }
    }

}
