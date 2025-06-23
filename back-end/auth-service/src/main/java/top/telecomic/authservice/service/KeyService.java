package top.telecomic.authservice.service;

import com.nimbusds.jose.jwk.JWK;
import top.telecomic.authservice.entity.Key;

import java.util.List;

public interface KeyService {

    List<JWK> getActiveJwks();

    Key getLatestKey();
}
