package top.telecomic.mediaservice.message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.telecomic.mediaservice.config.AuditorContext;
import top.telecomic.mediaservice.dto.request.MediaFileUpdateRequest;
import top.telecomic.mediaservice.service.MediaFileService;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MediaReviewConsumer {

    private final MediaFileService mediaFileService;

    @KafkaListener(topics = "ai.to.media", groupId = "${spring.application.name}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(MediaFileUpdateRequest message) {
        AuditorContext.set("ai-service");
        try {
            mediaFileService.updateMediaFile(message);
        } catch (Exception e) {
            log.error("Failed to update media file: {}", message.id(), e);
        } finally {
            AuditorContext.clear();
        }
    }


}
