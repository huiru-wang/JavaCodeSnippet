package com.snippet.flinkexample.udf;

import com.snippet.flinkexample.model.AlertAggModel;
import com.snippet.flinkexample.model.MockEvent;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.HashSet;

/**
 * create by whr on 2023/2/22
 */
public class MockEventAggregate implements AggregateFunction<MockEvent, Tuple2<HashSet<String>, AlertAggModel>, AlertAggModel> {

    @Override
    public Tuple2<HashSet<String>, AlertAggModel> createAccumulator() {
        return Tuple2.of(new HashSet<>(), new AlertAggModel());
    }

    @Override
    public Tuple2<HashSet<String>, AlertAggModel> add(MockEvent value, Tuple2<HashSet<String>, AlertAggModel> accumulator) {
        return null;
    }

    @Override
    public AlertAggModel getResult(Tuple2<HashSet<String>, AlertAggModel> accumulator) {
        return null;
    }

    @Override
    public Tuple2<HashSet<String>, AlertAggModel> merge(Tuple2<HashSet<String>, AlertAggModel> a, Tuple2<HashSet<String>, AlertAggModel> b) {
        return null;
    }
}
