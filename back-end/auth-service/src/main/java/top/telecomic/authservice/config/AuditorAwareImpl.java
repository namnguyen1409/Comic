package top.telecomic.authservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    ObjectFactory<HttpServletRequest> requestFactory;

    @NonFinal
    @Value("${spring.application.name}")
    String applicationName;

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        // check if auditor is set in the context
        Optional<String> auditorFromContext = AuditorContext.get();
        if (auditorFromContext.isPresent()) {
            return auditorFromContext;
        }
        // get user id from header of request that provided by gateway
        String userId = "anonymous"; // default to anonymous user

        try {
            var request = requestFactory.getObject();
            userId = request.getHeader("X-User-Id");
        } catch (Exception e) {
            log.info("Not in a web context, using default auditor");
        }
        if (userId != null && !userId.isEmpty()) {
            return Optional.of(userId);
        }
        // if user id is not present, return "system" as auditor
        return Optional.of(applicationName + "-system");
    }

}
