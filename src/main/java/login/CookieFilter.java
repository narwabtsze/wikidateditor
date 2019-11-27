package main.java.login;

import main.java.login.RandomString;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Cookie setter filter called");
        HttpServletRequest req = (HttpServletRequest) request;
        for(Cookie c : req.getCookies()){
            if(c.getName().equals("Wikidatacookie")){
                log.info("Caller already has a cookie");
                chain.doFilter(request, response);
                return;
            }
        }
        HttpServletResponse res = (HttpServletResponse) response;
        RandomString rnd = new RandomString(48);
        Cookie newcookie = new Cookie("Wikidatacookie", rnd.nextString());
        newcookie.setMaxAge(3600);
//        newcookie.setSecure(true);
        newcookie.setHttpOnly(true);

        res.addCookie(newcookie);
        log.info("New cookie added: " + newcookie.getName() + "=" + newcookie.getValue());
        chain.doFilter(request, response);
    }
}