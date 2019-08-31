package br.com.samuel.sambatech.services;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.samuel.sambatech.domain.Users;
import br.com.samuel.sambatech.repositories.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UsersRepository repo;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Users user = repo.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException(email);
    }
    List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
    return new User(user.getEmail(), user.getPassword(), authorities);
  }
}
