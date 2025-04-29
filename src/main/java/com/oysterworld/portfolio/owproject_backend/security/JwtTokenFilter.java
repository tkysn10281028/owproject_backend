package com.oysterworld.portfolio.owproject_backend.security;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oysterworld.portfolio.owproject_backend.common.RedisUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("!test")
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    UserDetailsService detailsService;

    /**
     * spring securityの内部フィルタ実行. jwtトークンの検証を行い、適当なフィルタリング処理を行う.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 事前にGoogleログインAPIであればセッションを新規作成する
        if (request.getRequestURI().startsWith("/oauth2/authorization/")) {
            request.getSession().invalidate();
            request.getSession(true);
        }
        String jwt = (String) RedisUtil.get("jwt");
        if (jwt != null && JwtUtils.validate(jwt)) {
            log.debug("Valid JWT Token:　{}", jwt);
            doAuthenticate(request, response, filterChain, jwt);
            return;
        }
        jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(jwt)) {
            log.debug("Invalid JWT Token(Empty):　{}", jwt);
            filterChain.doFilter(request, response);
            return;
        }
        if (!JwtUtils.validate(jwt)) {
            log.debug("Invalid JWT Token(Validation):　{}", jwt);
            filterChain.doFilter(request, response);
            return;
        }
        doAuthenticate(request, response, filterChain, jwt);
    }

    /**
     * spring securityの認証処理を実行する.
     * 
     * @param request
     * @param response
     * @param filterChain
     * @param token
     * @throws ServletException
     * @throws IOException
     */
    private void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
            String token) throws ServletException, IOException {
        UserDetails userDetails = detailsService.loadUserByUsername(JwtUtils.getUserEmailAddress(token));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}