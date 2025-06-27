package top.telecomic.authservice.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import top.telecomic.authservice.entity.Key;
import top.telecomic.authservice.enums.KeyGenName;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyUtils {
    public static String encodePublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static String encodePrivateKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public static RSAKey toRsaJwk(Key key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new RSAKey.Builder(getRsaPublicKeyFromBase64(key.getPublicKey()))
                .privateKey(getRsaPrivateKeyFromBase64(key.getPrivateKey()))
                .keyID(key.getKeyId().toString())
                .algorithm(key.getAlgorithm().getName())
                .build();
    }

    public static RSAPublicKey getRsaPublicKeyFromBase64(String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var keyBytes = Base64URL.from(publicKeyBase64).decode();
        var spec = new X509EncodedKeySpec(keyBytes);
        var keyFactory = KeyFactory.getInstance(KeyGenName.RSA.name());
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    public static RSAPrivateKey getRsaPrivateKeyFromBase64(String privateKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var keyBytes = Base64URL.from(privateKeyBase64).decode();
        var spec = new PKCS8EncodedKeySpec(keyBytes);
        var keyFactory = KeyFactory.getInstance(KeyGenName.RSA.name());
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    public static ECKey toEcJwk(Key key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new ECKey.Builder(key.getAlgorithm().getCurve(), getEcPublicKeyFromBase64(key.getPublicKey()))
                .privateKey(getEcPrivateKeyFromBase64(key.getPrivateKey()))
                .keyID(key.getKeyId().toString())
                .algorithm(key.getAlgorithm().getName())
                .build();
    }

    public static ECPublicKey getEcPublicKeyFromBase64(String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var keyBytes = Base64URL.from(publicKeyBase64).decode();
        var spec = new X509EncodedKeySpec(keyBytes);
        var keyFactory = KeyFactory.getInstance(KeyGenName.EC.name());
        return (ECPublicKey) keyFactory.generatePublic(spec);
    }

    public static ECPrivateKey getEcPrivateKeyFromBase64(String privateKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var keyBytes = Base64URL.from(privateKeyBase64).decode();
        var spec = new PKCS8EncodedKeySpec(keyBytes);
        var keyFactory = KeyFactory.getInstance(KeyGenName.EC.name());
        return (ECPrivateKey) keyFactory.generatePrivate(spec);
    }

    public static PrivateKey getPrivateKey(Key key) {
        try {
            return switch (key.getAlgorithm().getKeyGenName()) {
                case RSA -> KeyUtils.getRsaPrivateKeyFromBase64(key.getPrivateKey());
                case EC -> KeyUtils.getEcPrivateKeyFromBase64(key.getPrivateKey());
            };
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.TOKEN_CREATION_FAILED, "Failed to get private key: " + e.getMessage());
        }
    }

    public static JWSSigner buildSigner(Key key, PrivateKey privateKey) {
        try {
            return switch (key.getAlgorithm().getKeyGenName()) {
                case RSA -> new RSASSASigner(privateKey);
                case EC -> new ECDSASigner((ECPrivateKey) privateKey);
            };
        } catch (JOSEException e) {
            throw new GlobalException(ErrorCode.TOKEN_CREATION_FAILED, "Signer creation failed: " + e.getMessage());
        }
    }

}
