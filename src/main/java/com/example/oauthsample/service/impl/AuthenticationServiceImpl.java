package com.example.oauthsample.service.impl;

import com.example.oauthsample.db.domain.User;
import com.example.oauthsample.service.AuthenticationService;
import com.example.oauthsample.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private UserService userService;

  @Autowired
  public AuthenticationServiceImpl(@Lazy UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userService.lookupByEmail(email);
    if (user != null) {
      return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getSecret(), Lists.newArrayList());
    }
    throw new UsernameNotFoundException("Invalid Credentials");
  }
}
