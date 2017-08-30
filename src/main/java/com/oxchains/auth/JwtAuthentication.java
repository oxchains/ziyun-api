package com.oxchains.auth;

import com.oxchains.bean.model.ziyun.TabUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * @author aiet
 */
public class JwtAuthentication implements Authentication {

  private String token;
  private TabUser user;
  private Map<String, Object> details;

  JwtAuthentication(TabUser user, String token, Map<String, Object> details) {
    this.user = user;
    this.token = token;
    this.details = details;
  }

  public Optional<TabUser> user() {
    return Optional.ofNullable(user);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return emptyList();
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getDetails() {
    return details;
  }

  @Override
  public Object getPrincipal() {
    return user;
  }

  @Override
  public boolean isAuthenticated() {
    return user != null && user.getUsername() != null ;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    if (!isAuthenticated) user = null;
  }

  @Override
  public String getName() {
    return user.getUsername();
  }

  @Override
  public String toString() {
    return token;
  }

}
