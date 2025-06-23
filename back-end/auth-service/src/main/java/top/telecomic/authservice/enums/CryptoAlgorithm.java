package top.telecomic.authservice.enums;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CryptoAlgorithm {
    RS256(JWSAlgorithm.RS256, KeyGenName.RSA, 2048, null),
    RS384(JWSAlgorithm.RS384, KeyGenName.RSA, 3072, null),
    RS512(JWSAlgorithm.RS512, KeyGenName.RSA, 4096, null),

    ES256(JWSAlgorithm.ES256, KeyGenName.EC, 256, Curve.P_256),
    ES384(JWSAlgorithm.ES384, KeyGenName.EC, 384, Curve.P_384),
    ES512(JWSAlgorithm.ES512, KeyGenName.EC, 521, Curve.P_521);
    JWSAlgorithm name;
    KeyGenName keyGenName;
    int defaultKeySize;
    Curve curve;
}
