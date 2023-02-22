public class FaultEventSource implements SourceFunction<FaultEvent> {

    private Boolean running = true;

    String[] serviceList = new String[] {"AlertService", "ServiceInsight", "BusinessService", "WiseCloudTrade"};

    String[] microServiceList = new String[] {"ms2", "ms3", "ms4", "ms5", "ms6"};

    String[] categoryList = new String[] {"OS", "CPU", "Memory", "Service", "Other"};

    String[] opTypeList = new String[] {"resolved", "firing"};

    String[] levelList = new String[] {"notice", "warning", "minor", "major", "critical"};

    String[] hostIpList =
            new String[] {"10.33.41.7", "10.33.41.8", "10.33.41.6", "10.33.41.5", "10.33.41.4", "10.33.41.3",};

    @Override
    public void run(SourceContext<FaultEvent> sourceContext) throws Exception {
        Random random = new Random();
        while (running) {
            sourceContext.collect(new FaultEvent(
                    serviceList[random.nextInt(serviceList.length)],
                    microServiceList[random.nextInt(microServiceList.length)],
                    categoryList[random.nextInt(categoryList.length)],
                    IdUtil.getSnowflakeNextId(),
                    opTypeList[random.nextInt(opTypeList.length)],
                    levelList[random.nextInt(levelList.length)],
                    hostIpList[random.nextInt(hostIpList.length)],
                    System.currentTimeMillis())
            );
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
