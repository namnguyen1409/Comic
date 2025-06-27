package top.telecomic.mediaservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    public static final String ENDPOINT_QUEUE = "endpoint.queue";

    @Bean
    public Queue endpointQueue() {
        return new Queue(ENDPOINT_QUEUE, true);
    }
}
