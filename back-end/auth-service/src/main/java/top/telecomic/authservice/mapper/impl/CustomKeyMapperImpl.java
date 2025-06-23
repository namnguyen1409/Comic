package top.telecomic.authservice.mapper.impl;

import com.nimbusds.jose.jwk.JWK;
import org.springframework.stereotype.Component;
import top.telecomic.authservice.entity.Key;
import top.telecomic.authservice.mapper.CustomKeyMapper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static top.telecomic.authservice.utils.KeyUtils.toEcJwk;
import static top.telecomic.authservice.utils.KeyUtils.toRsaJwk;

@Component
public class CustomKeyMapperImpl implements CustomKeyMapper {

    @Override
    public JWK toJwk(Key key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return switch (key.getAlgorithm().getKeyGenName()) {
            case RSA -> toRsaJwk(key);
            case EC -> toEcJwk(key);
        };
    }


}