package br.com.samuel.sambatech.security;

import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.samuel.sambatech.exceptions.StandardError;
import br.com.samuel.sambatech.utils.MessageUtils;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private JWTUtil jwtUtil;

  private MessageUtils messageUtils;

  private UserDetailsService userDetailsService;

  private ObjectMapper mapper;

  public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
      UserDetailsService userDetailsService, MessageUtils messageUtils, ObjectMapper mapper) {
    super(authenticationManager);
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
    this.messageUtils = messageUtils;
    this.mapper = mapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader("Authorization");
    boolean isError = true;
    if (!ObjectUtils.isEmpty(header) && header.startsWith("Bearer ")) {
      UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
      if (!ObjectUtils.isEmpty(auth)) {
        SecurityContextHolder.getContext().setAuthentication(auth);
        isError = false;
      }
    }
    if (isError) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter()
          .append(json(new StandardError(new Date().getTime(), HttpStatus.UNAUTHORIZED.value(),
              messageUtils.getMessage("authentication.exception.title"),
              messageUtils.getMessage("authentication.exception"),
              messageUtils.getMessage("authentication.exception.details"),
              request.getRequestURL().toString())));
      return;
    }
    chain.doFilter(request, response);
  }


  private String json(StandardError error) {
    try {
      return mapper.writeValueAsString(error);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private UsernamePasswordAuthenticationToken getAuthentication(String token) {
    if (jwtUtil.tokenValido(token)) {
      UserDetails user = userDetailsService.loadUserByUsername(jwtUtil.getUsername(token));
      return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
    return null;
  }
}
