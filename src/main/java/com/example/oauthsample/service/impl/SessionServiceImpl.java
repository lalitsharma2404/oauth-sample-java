package com.example.oauthsample.service.impl;

import com.example.oauthsample.db.domain.User;
import com.example.oauthsample.service.SessionService;
import com.example.oauthsample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionServiceImpl implements SessionService {

  private UserService userService;
  private String userEmail;

  @Autowired
  public SessionServiceImpl(UserService userService) {
    this.userService = userService;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    userEmail = (String) authentication.getPrincipal();
  }


  @Override
  public User getLoggedInUser() {
    return userService.findOneBy("email", userEmail);
  }
}
