package top.telecomic.mediaservice.message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.telecomic.mediaservice.config.RabbitConfig;
import top.telecomic.mediaservice.dto.message.EndpointMessage;

import java.util.List;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EndpointSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendEndpoint(List<EndpointMessage> endpointMessages) {
        rabbitTemplate.convertAndSend(RabbitConfig.ENDPOINT_QUEUE, endpointMessages);
        log.info("Sent {} endpoint messages to queue {}", endpointMessages.size(), RabbitConfig.ENDPOINT_QUEUE);
    }

}
