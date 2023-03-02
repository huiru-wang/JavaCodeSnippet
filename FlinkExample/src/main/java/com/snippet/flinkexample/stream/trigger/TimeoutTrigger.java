public class TimeoutTrigger extends Trigger<Object, TimeWindow> {

    // 超时关窗时间
    private final Duration timeout;

    public TimeoutTrigger(Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public TriggerResult onElement(Object element, long timeStamp, TimeWindow timeWindow, TriggerContext ctx) {

        if (timeWindow.maxTimestamp() <= ctx.getCurrentWatermark()) {
            return TriggerResult.FIRE;
        } else {
            ctx.registerEventTimeTimer(timeWindow.maxTimestamp());
            // 定时器事件必须 大于 数据乱序时间
            ctx.registerProcessingTimeTimer(timeWindow.maxTimestamp() + timeout.toMillis());
            return TriggerResult.CONTINUE;
        }
    }

    @Override
    public TriggerResult onProcessingTime(long timeStamp, TimeWindow timeWindow, TriggerContext ctx) throws Exception {
        // 在处理时间机制下 删除事件时间定时器
        ctx.deleteEventTimeTimer(timeWindow.maxTimestamp());
        return TriggerResult.FIRE;
    }

    @Override
    public TriggerResult onEventTime(long timeStamp, TimeWindow timeWindow, TriggerContext ctx) throws Exception {
        // 数据事件时间达到窗口最大值 触发计算
        if (timeStamp == timeWindow.maxTimestamp()) {
            // 触发计算 删除处理时间定时器
            ctx.deleteProcessingTimeTimer(timeWindow.maxTimestamp() + 10000);
            return TriggerResult.FIRE;
        } else {
            return TriggerResult.CONTINUE;
        }
    }

    @Override
    public void clear(TimeWindow timeWindow, TriggerContext ctx) throws Exception {
        // 清楚定时器
        ctx.deleteProcessingTimeTimer(timeWindow.maxTimestamp() + 10000);
        ctx.deleteEventTimeTimer(timeWindow.maxTimestamp());
    }
}
