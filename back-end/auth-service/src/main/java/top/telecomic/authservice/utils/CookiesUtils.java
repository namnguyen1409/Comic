package top.telecomic.authservice.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CookiesUtils {

    HttpServletResponse httpServletResponse;
    HttpServletRequest httpServletRequest;

    @NonFinal
    @Value("${setup.cookies.domain}")
    String cookiesDomain;

    @NonFinal
    @Value("${setup.cookies.path}")
    String cookiesPath;

    public void setCookie(String name, String value, int maxAge) {
        var cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(cookiesPath);
        cookie.setDomain(cookiesDomain);
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge(maxAge);
        httpServletResponse.addCookie(cookie);

    }

    public String getCookie(String name) {
        var cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void deleteCookie(String name) {
        var cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(cookiesPath);
        cookie.setDomain(cookiesDomain);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }

}
