package com.example.oauthsample.security;

import com.example.oauthsample.config.properties.ApplicationProperties;
import com.example.oauthsample.db.domain.Token;
import com.example.oauthsample.db.domain.User;
import com.example.oauthsample.service.TokenService;
import com.example.oauthsample.service.UserService;
import com.example.oauthsample.util.DateUtil;
import com.example.oauthsample.util.JsonUtil;
import com.example.oauthsample.util.security.EncryptedPayload;
import com.example.oauthsample.util.security.PlainTextPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TokenProvider {

  private ApplicationProperties applicationProperties;

  private UserService userService;
  private TokenService tokenService;

  @Autowired
  public TokenProvider(ApplicationProperties applicationProperties, @Lazy UserService userService, @Lazy TokenService tokenService) {
    this.applicationProperties = applicationProperties;
    this.userService = userService;
    this.tokenService = tokenService;
  }

  public String getNewAuthToken(User user) {
    Token token = newToken(applicationProperties.getSecurityProperties().getAuthTokenExpiryInMS());
    token.setUserid(user.getId());
    token.setType(Token.Type.AUTH);

    return encrypt(token);

  }

  public boolean verify(String token) {
    Token authToken = retrieve(token);

    Long nowInMillis = DateUtil.nowInMillis();

    if (authToken == null || authToken.getExpiry() < nowInMillis) {
      return Boolean.FALSE;
    }

    // Verify Against DB
    Token dbToken = tokenService.findOneBy("uuid", authToken.getUuid());
    if (dbToken == null || !dbToken.equals(authToken)) {
      return Boolean.FALSE;
    }

    // Check activeness
    if (dbToken.isUsed() || dbToken.getDeleted()) {
      return Boolean.FALSE;
    }

    // Verify against expiry
    if (dbToken.getExpiry() < nowInMillis) {
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  public Token retrieve(String token) {
    final EncryptedPayload payload =
      new EncryptedPayload(token, applicationProperties.getSecurityProperties().getEncryptionSecret());
    Token authToken = JsonUtil.fromJson(payload.getPlainText(), Token.class);
    authToken.setUser(userService.findById(authToken.getUserid()));
    return authToken;
  }

  private Token newToken(long expiry) {
    Token token = new Token();
    token.setExpiry(DateUtil.nowInMillis() + expiry);
    token.setCreatedDate(DateUtil.nowInMillis());
    return token;
  }

  private String encrypt(Token token) {
    final PlainTextPayload payload =
      new PlainTextPayload(JsonUtil.toJson(tokenService.save(token)), applicationProperties.getSecurityProperties().getEncryptionSecret());
    return payload.getCipherText();
  }

}