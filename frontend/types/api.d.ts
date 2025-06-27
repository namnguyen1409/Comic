export interface DecodedJwtToken {
    iss: string;
    sub: string;
    exp: number;
    iat: number;
    jti: string;
    provider: string;
    scope: string;
    [key: string]: any
}
