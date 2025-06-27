package top.telecomic.mediaservice.config;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.telecomic.mediaservice.dto.message.EndpointMessage;
import top.telecomic.mediaservice.message.EndpointSender;

import java.util.Map;
import java.util.Objects;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Configuration
public class EndpointScanner {

    RequestMappingHandlerMapping requestMappingHandlerMapping;
    EndpointSender endpointSender;


    @NonFinal
    @Value("${spring.application.name}")
    private String serviceName;

    @PostConstruct
    public void scanEndpoints() {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        var endpoints = map.keySet().stream()
                .map(
                        info -> {
                            var uri = info.getPatternValues().stream().findFirst().orElse(null);
                            var httpMethod = info.getMethodsCondition().getMethods().stream()
                                    .findFirst()
                                    .map(Enum::name)
                                    .orElse(null);
                            if (uri == null || httpMethod == null) return null;
                            return new EndpointMessage(
                                    serviceName,
                                    uri,
                                    httpMethod
                            );
                        }
                )
                .filter(Objects::nonNull)
                .toList();
        endpointSender.sendEndpoint(endpoints);
    }

}
