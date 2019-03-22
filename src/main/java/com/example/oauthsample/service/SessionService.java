package com.example.oauthsample.service;

import com.example.oauthsample.db.domain.User;

public interface SessionService {

  User getLoggedInUser();

}
