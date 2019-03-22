package com.example.oauthsample;


public final class Constants {

  public static final String BASE_PACKAGE = "com.example.oauthsample";
  public static final String API_BASE_URL = "/api/v1";
  public static final String API_PUBLIC_BASE_URL = "/public/api/v1";
  public static final String[] API_PUBLIC_BASE_URL_PATTERN =
      new String[] {"/public/**", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
          "/swagger-ui.html", "/webjars/**", "/tw", "/images/**"};

  public static final String OPTIONS_ALLOWED_URL_PATTERN = "/**";
  public static final String API_LOGIN_URL = API_PUBLIC_BASE_URL + "/login";
  public static final String ACCESS_TOKEN_HEADER = "Authorization";

  private Constants() {
  }
}
