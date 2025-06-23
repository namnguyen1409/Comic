package top.telecomic.authservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CurveName {
    P_256("secp256r1"),
    P_384("secp384r1"),
    P_521("secp521r1");
    String specName;
}
