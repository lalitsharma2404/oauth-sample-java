package com.example.oauthsample.db.repository.impl;

import com.example.oauthsample.db.domain.Token;
import com.example.oauthsample.db.repository.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepositoryImpl extends BaseRepository<Token> implements TokenRepository {
}
