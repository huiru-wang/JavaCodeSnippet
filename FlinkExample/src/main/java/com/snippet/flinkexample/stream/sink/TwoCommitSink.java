package com.snippet.flinkexample.stream.sink;

import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;

/**
 * 两阶段提交：提交事务 + checkpoint
 * <p>
 * create by whr on 2023/3/2
 */
public class TwoCommitSink extends TwoPhaseCommitSinkFunction {

    public TwoCommitSink(TypeSerializer transactionSerializer, TypeSerializer contextSerializer) {
        super(transactionSerializer, contextSerializer);
    }

    @Override
    protected void invoke(Object transaction, Object value, Context context) throws Exception {

    }

    @Override
    protected Object beginTransaction() throws Exception {
        return null;
    }

    @Override
    protected void preCommit(Object transaction) throws Exception {

    }

    @Override
    protected void commit(Object transaction) {

    }

    @Override
    protected void abort(Object transaction) {

    }
}
