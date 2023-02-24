package com.snippet.flinkexample.source;

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

/**
 * create by whr on 2023/2/24
 */
public class EventRichParallelSource extends RichParallelSourceFunction {
    private Boolean running = true;

    @Override
    public void run(SourceContext ctx) throws Exception {
        while (running) {

        }
    }

    @Override
    public void cancel() {
        this.running = false;
    }
}
