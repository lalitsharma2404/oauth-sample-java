package com.example.oauthsample.exception;

public class InitializationException extends BaseException {

  public InitializationException(String message) {
    super(message);
  }

  public InitializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public InitializationException(Throwable cause) {
    super(cause);
  }

  public InitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
