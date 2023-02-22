public class KafkaStreamSourceExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "flink_stream");

        FlinkKafkaConsumer<MessageModel> myConsumer = new FlinkKafkaConsumer<>(
                java.util.regex.Pattern.compile("snippet[0-9]"),    // 自动发现动态创建的kafka分区
                new MessageDeserializationSchema(),
                properties
        );

        // TODO checkpoint?

        DataStream<MessageModel> stream = env.addSource(myConsumer);

        stream.print();

        env.execute();
    }
}
