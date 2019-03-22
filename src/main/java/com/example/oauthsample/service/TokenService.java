package com.example.oauthsample.service;

import com.example.oauthsample.db.domain.Token;
import com.example.oauthsample.db.repository.TokenRepository;
import com.fasterxml.jackson.databind.JsonNode;

public interface TokenService extends Service<Token, TokenRepository> {

  boolean validateToken(Token.Type type, String tokenStr);

  void act(Token.Type type, String tokenStr, JsonNode jsonNode);

}
