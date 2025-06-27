package top.telecomic.authservice.message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.telecomic.authservice.config.RabbitConfig;
import top.telecomic.authservice.dto.message.EndpointMessage;
import top.telecomic.authservice.service.EndpointService;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EndpointReceiver {

    private final EndpointService endpointService;

    @RabbitListener(queues = RabbitConfig.ENDPOINT_QUEUE)
    public void receiveMessage(List<EndpointMessage> messages) {
        endpointService.addEndpoints(messages);
    }


}
