package top.telecomic.authservice.mapper;

import com.nimbusds.jose.jwk.JWK;
import top.telecomic.authservice.entity.Key;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface CustomKeyMapper {
    JWK toJwk(Key key) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
