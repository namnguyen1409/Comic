package top.telecomic.authservice.service.impl;

import com.nimbusds.jose.jwk.JWK;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.telecomic.authservice.entity.Key;
import top.telecomic.authservice.enums.CryptoAlgorithm;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;
import top.telecomic.authservice.mapper.CustomKeyMapper;
import top.telecomic.authservice.repository.KeyRepository;
import top.telecomic.authservice.service.KeyService;
import top.telecomic.authservice.utils.KeyPairGeneratorUtil;
import top.telecomic.authservice.utils.KeyUtils;

import java.security.KeyPair;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyServiceImpl implements KeyService {

    KeyRepository keyRepository;
    CustomKeyMapper customKeyMapper;

    @NonFinal
    @Value("${security.key.algorithm}")
    CryptoAlgorithm algorithm;

    @NonFinal
    @Value("${security.key.size}")
    int keySize;


    @Transactional
    @PostConstruct
    public void init() {
        if (keyRepository.count() == 0) {
            try {
                KeyPair keyPair = KeyPairGeneratorUtil.generateKeyPair(algorithm, keySize);
                Key key = Key.builder()
                        .keyId(UUID.randomUUID())
                        .algorithm(algorithm)
                        .privateKey(KeyUtils.encodePrivateKey(keyPair.getPrivate()))
                        .publicKey(KeyUtils.encodePublicKey(keyPair.getPublic()))
                        .isActive(true)
                        .build();
                keyRepository.save(key);
            } catch (Exception e) {
                log.error("Failed to generate key pair");
                log.error("Error: {}", e.getMessage(), e);
                throw new GlobalException(ErrorCode.KEY_GENERATION_FAILED);
            }
        }
    }

    @Cacheable(cacheNames = "keys", key = "#root.methodName")
    @Override
    public List<JWK> getActiveJwks() {
        return keyRepository.findAllByIsActiveIsTrue()
                .stream()
                .map(key -> {
                    try {
                        return customKeyMapper.toJwk(key);
                    } catch (Exception e) {
                        log.error("Failed to convert key to JWK: {}", key.getKeyId(), e);
                        throw new GlobalException(ErrorCode.KEY_CONVERSION_FAILED);
                    }
                })
                .toList();
    }

    @Cacheable(cacheNames = "activeKey", key = "#root.methodName")
    @Override
    public Key getLatestKey() {
        return keyRepository.findFirstByIsActiveIsTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new GlobalException(ErrorCode.KEY_NOT_FOUND, "No active key found"));
    }

}
