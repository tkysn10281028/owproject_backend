package com.oysterworld.portfolio.owproject_backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.oysterworld.portfolio.owproject_backend.common.RedisUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 * ログインが成功した後の処理を実装.
 * jwtトークンを生成してjwtIDと共にDBに登録する.
 * </pre>
 * 
 * @author entakuya
 *
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.oauth.success-url}")
    private String oauthSuccessUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication auth) throws IOException, ServletException {
        var token = JwtUtils.generateAccessToken(((OidcUser) auth.getPrincipal()).getEmail());
        RedisUtil.set("jwt", token);
        response.sendRedirect(oauthSuccessUrl);
    }
}