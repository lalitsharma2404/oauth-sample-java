package com.example.oauthsample.rest.filter;

import static com.example.oauthsample.Constants.API_LOGIN_URL;

import com.example.oauthsample.db.domain.User;
import com.example.oauthsample.service.UserService;
import com.example.oauthsample.rest.request.LoginRequest;
import com.example.oauthsample.rest.response.AuthResponse;
import com.example.oauthsample.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

  private AuthenticationManager authenticationManager;
  private TokenProvider tokenProvider;
  private UserService userService;

  public TokenAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
      UserService userService) {
    super(new AntPathRequestMatcher(API_LOGIN_URL));
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.userService = userService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
    try {
      LoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSecret(), new ArrayList<>()));
    } catch (IOException e) {
      LOGGER.info("Error while attempting authentication", e);
      throw new BadCredentialsException("credentials are invalid");
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException {
    final String userEmail = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();

    final User user = userService.lookupByEmail(userEmail);
    final String token = tokenProvider.getNewAuthToken(user);
    final AuthResponse authResponse = new AuthResponse();
    authResponse.setUser(user);
    authResponse.setToken(token);

    final ObjectMapper objectMapper = new ObjectMapper();
    res.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    res.getWriter().write(objectMapper.writeValueAsString(authResponse));
    res.getWriter().flush();
  }
}

