package com.example.oauthsample.service.impl;

import com.example.oauthsample.db.domain.User;
import com.example.oauthsample.db.repository.UserRepository;
import com.example.oauthsample.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Service
public class UserServiceImpl extends BaseService<User, UserRepository> implements UserService {

  private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
    super(repository);
    this.passwordEncoder = passwordEncoder;
  }

  @PostConstruct
  public void init() {
    if (Objects.isNull(this.lookupByEmail("admin@admin.com"))) {
      User user = new User();
      user.setFirstName("Admin");
      user.setLastName("Admin");
      user.setEmail("admin@gmail.com");
      user.setMobileNumber("9876543210");
      user.setActive(Boolean.TRUE);
      user.setAccountLocked(Boolean.FALSE);
      user.setSecret(this.passwordEncoder.encode("123123"));
      this.save(user);
    }
  }

  @Override
  public User lookupByEmail(String email) {
    return this.repository.findOneBy("email", email);
  }
}
