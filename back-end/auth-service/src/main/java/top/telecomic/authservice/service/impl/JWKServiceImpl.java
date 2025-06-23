package top.telecomic.authservice.service.impl;

import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;
import top.telecomic.authservice.service.JWKService;

import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JWKServiceImpl implements JWKService {

    JWKSource<SecurityContext> securityContextJWKSource;

    @Override
    public Map<String, Object> getKeys() {
        try {
            var selector = new JWKSelector(new JWKMatcher.Builder().build());
            var jwkSet = new JWKSet(securityContextJWKSource.get(selector, null));
            return jwkSet.toJSONObject();
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


}
