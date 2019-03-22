package com.example.oauthsample.service.impl;

import com.example.oauthsample.db.domain.Token;
import com.example.oauthsample.db.repository.TokenRepository;
import com.example.oauthsample.exception.AccessDeniedException;
import com.example.oauthsample.security.TokenProvider;
import com.example.oauthsample.service.TokenService;
import com.example.oauthsample.rest.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl extends BaseService<Token, TokenRepository> implements TokenService {

  private static final Logger LOGGER = LogManager.getLogger(TokenServiceImpl.class);
  private TokenProvider tokenProvider;

  @Autowired
  public TokenServiceImpl(TokenRepository repository, @Lazy TokenProvider tokenProvider) {
    super(repository);
    this.tokenProvider = tokenProvider;
  }

  @Override
  public boolean validateToken(Token.Type type, String tokenStr) {
    if (!tokenProvider.verify(tokenStr)) {
      return Boolean.FALSE;
    }

    Token token = tokenProvider.retrieve(tokenStr);
    if (type != token.getType()) {
      return Boolean.FALSE;
    }

    // Token is verified now return success
    return Boolean.TRUE;
  }

  @Override
  public void act(Token.Type type, String tokenStr, JsonNode jsonNode) {
    if (!validateToken(type, tokenStr)) {
      throw new AccessDeniedException("Invalid Token", ResponseCode.UNAUTHORIZED);
    }

    Token token = tokenProvider.retrieve(tokenStr);
    boolean actionPerformed = Boolean.FALSE;
    switch (token.getType()) {
      case AUTH:
        actionPerformed = Boolean.TRUE;
        break;
      default:
        actionPerformed = Boolean.FALSE;
        break;
    }

    if (actionPerformed) {
      // Invalidate token
      token.setExpiry(0);
      token.setUsed(Boolean.TRUE);
      token.setDeleted(Boolean.TRUE);
      repository.update(token);
    }

  }
}
