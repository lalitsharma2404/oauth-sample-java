package com.example.oauthsample.db.repository.impl;

import com.example.oauthsample.db.domain.User;
import com.example.oauthsample.db.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends BaseRepository<User> implements UserRepository {

}
