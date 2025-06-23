package top.telecomic.authservice.utils;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.telecomic.authservice.entity.Key;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static top.telecomic.authservice.utils.KeyUtils.buildSigner;
import static top.telecomic.authservice.utils.KeyUtils.getPrivateKey;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityUtils {

    HttpServletRequest httpServletRequest;

    @NonFinal
    @Value("${spring.security.oauth2.authorizationserver.issuer}")
    String issuer;


    public String getIpAddress() {
        String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpServletRequest.getRemoteAddr();
        }
        return ipAddress;
    }

    public String createJwt(String name, Map<String, Object> customClaims, Duration exp, Key key) {
        try {
            PrivateKey privateKey = getPrivateKey(key);
            JWSHeader header = new JWSHeader(key.getAlgorithm().getName());

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(name)
                    .issuer(issuer)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(exp)));

            if (customClaims != null) {
                customClaims.forEach(claimsBuilder::claim);
            }

            JWTClaimsSet claimsSet = claimsBuilder.build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            JWSSigner signer = buildSigner(key, privateKey);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.TOKEN_CREATION_FAILED, "Failed to create JWT: " + e.getMessage());
        }
    }

    public String hash(String input, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            var hashedBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new GlobalException(ErrorCode.TOKEN_CREATION_FAILED, "Failed to hash input: ");
        }
    }

}
