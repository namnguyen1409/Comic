package top.telecomic.mediaservice.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuditorContext {
    static ThreadLocal<String> context = new ThreadLocal<>();

    public static void set(String auditor) {
        context.set(auditor);
    }

    public static Optional<String> get() {
        return Optional.ofNullable(context.get());
    }

    public static void clear() {
        context.remove();
    }
}
