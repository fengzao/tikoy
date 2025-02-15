import com.alibaba.fastjson2.JSON;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.kafka.JobProps;
import com.cokebook.tools.tikoy.kafka.KafkaJob;
import com.cokebook.tools.tikoy.mapping.Op;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2025/2/3
 */
public class KafkaJobTest {

    public static void main(String[] args) throws Exception {
        JobProps props = JobProps.builder()
                .servers("localhost:9092")
                .id("test")
                .topics("test.demo")
                .valueDeserializer(RecordDeserializer.class.getName())
                .build();

        LogDispatcher dispatcher = (group, record) -> {
            System.out.println("text = " + JSON.toJSONString(record.data()));
        };
        KafkaJob job = new KafkaJob("test", props, dispatcher);
        job.init();
        job.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> job.stop()));
    }

    public static class RecordDeserializer implements Deserializer<Log> {
        @Override
        public Log deserialize(String topic, byte[] data) {
            String str = new String(data);
            return new Log() {
                @Override
                public String database() {
                    return null;
                }

                @Override
                public String table() {
                    return null;
                }

                @Override
                public Op type() {
                    return null;
                }

                @Override
                public Map<String, Object> data() {
                    Map<String, Object> x = new HashMap<>();
                    x.put("text", str);
                    return x;
                }

                @Override
                public Map<String, Object> old() {
                    return null;
                }
            };
        }
    }
}
