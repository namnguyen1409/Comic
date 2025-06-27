package top.telecomic.mediaservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    HttpServletRequest request;

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

        if (request != null) {
            userId = request.getHeader("X-User-Id");
        }
        if (userId != null && !userId.isEmpty()) {
            return Optional.of(userId);
        }
        // if user id is not present, return "system" as auditor
        return Optional.of("system");
    }

}
