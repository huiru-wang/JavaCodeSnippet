public class CountTrigger extends Trigger<Object, TimeWindow> {

    private static final long serialVersionUID = 1L;

    private final long maxCount;

    private final ReducingStateDescriptor<Long> stateDesc =
        new ReducingStateDescriptor<>("pubAlertCount", new PubAlertCount(), LongSerializer.INSTANCE);

    public CountTrigger(long maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public TriggerResult onElement(Object element, long timestamp, TimeWindow window, TriggerContext ctx)
        throws Exception {
        ctx.registerProcessingTimeTimer(window.maxTimestamp());

        ReducingState<Long> count = ctx.getPartitionedState(stateDesc);
        count.add(1L);
        // 超出最大数量，触发
        if (count.get() >= maxCount) {
            count.clear();
            return TriggerResult.FIRE;
        }
        return TriggerResult.CONTINUE;

    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) {
        return TriggerResult.FIRE;
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) {
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) {
        ctx.getPartitionedState(stateDesc).clear();
        ctx.deleteProcessingTimeTimer(window.maxTimestamp());
    }

    @Override
    public void onMerge(TimeWindow window, OnMergeContext ctx) {
        ctx.mergePartitionedState(stateDesc);
        long windowMaxTimestamp = window.maxTimestamp();
        if (windowMaxTimestamp > ctx.getCurrentProcessingTime()) {
            ctx.registerProcessingTimeTimer(windowMaxTimestamp);
        }
    }

    private static class PubAlertCount implements ReduceFunction<Long> {
        private static final long serialVersionUID = 1L;

        @Override
        public Long reduce(Long value1, Long value2) {
            return value1 + value2;
        }
    }
}
