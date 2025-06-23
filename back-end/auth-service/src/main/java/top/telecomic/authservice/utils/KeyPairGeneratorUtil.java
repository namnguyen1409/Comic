package top.telecomic.authservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.telecomic.authservice.enums.CryptoAlgorithm;
import top.telecomic.authservice.enums.KeyGenName;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyPairGeneratorUtil {

    private static final Set<Integer> ALLOWED_RSA_KEY_SIZES = Set.of(2048, 3072, 4096);

    public static KeyPair generateKeyPair(CryptoAlgorithm algorithm, Integer keySize)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm.getKeyGenName().name());

        switch (algorithm.getKeyGenName()) {
            case RSA -> {
                int validKeySize = getValidKeySize(algorithm, keySize);
                keyPairGenerator.initialize(validKeySize);
            }
            case EC -> {
                ECGenParameterSpec ecSpec = new ECGenParameterSpec(algorithm.getCurve().getStdName());
                keyPairGenerator.initialize(ecSpec);
            }
            default -> throw new IllegalArgumentException("Unsupported algorithm: " + algorithm.getKeyGenName());
        }
        return keyPairGenerator.generateKeyPair();
    }

    private static int getValidKeySize(CryptoAlgorithm algorithm, Integer keySize) {
        if (algorithm.getKeyGenName() != KeyGenName.RSA) {
            throw new IllegalArgumentException("Key size validation only supported for RSA");
        }

        if (keySize == null || !ALLOWED_RSA_KEY_SIZES.contains(keySize)) {
            log.warn("Invalid RSA key size: {}. Using default size: {}", keySize, algorithm.getDefaultKeySize());
            return algorithm.getDefaultKeySize();
        }

        return keySize;
    }
}
