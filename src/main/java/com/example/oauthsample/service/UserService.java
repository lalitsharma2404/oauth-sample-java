package com.example.oauthsample.service;

import com.example.oauthsample.db.domain.User;
import com.example.oauthsample.db.repository.UserRepository;

public interface UserService extends Service<User, UserRepository> {

  User lookupByEmail(String email);

}
