package br.com.samuel.sambatech.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.samuel.sambatech.dto.security.CredenciaisDTO;
import br.com.samuel.sambatech.exceptions.StandardError;
import br.com.samuel.sambatech.utils.MessageUtils;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  private JWTUtil jwtUtil;

  private ObjectMapper mapper;

  private MessageUtils messageUtils;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
      ObjectMapper mapper, MessageUtils messageUtils) {
    setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.mapper = mapper;
    this.messageUtils = messageUtils;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {
    try {
      CredenciaisDTO creds =
          new ObjectMapper().readValue(req.getInputStream(), CredenciaisDTO.class);
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          creds.getEmail(), creds.getPassword(), new ArrayList<>());
      Authentication auth = authenticationManager.authenticate(authToken);
      return auth;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
      FilterChain chain, Authentication auth) throws IOException, ServletException {

    String username = ((User) auth.getPrincipal()).getUsername();
    String token = jwtUtil.generateToken(username);
    res.addHeader("Authorization", "Bearer " + token);
    res.addHeader("access-control-expose-headers", "Authorization");
  }

  private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().append(json(new StandardError(new Date().getTime(),
          HttpStatus.UNAUTHORIZED.value(), messageUtils.getMessage("label.unauthorized"),
          messageUtils.getMessage("msg.authentication.user.password.invalid"),
          messageUtils.getMessage("msg.authentication.user.password.invalid.details"), "/login")));
    }

    private String json(StandardError error) {
      try {
        return mapper.writeValueAsString(error);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
