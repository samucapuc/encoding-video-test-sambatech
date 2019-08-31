package br.com.samuel.sambatech.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.samuel.sambatech.utils.MessageUtils;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private MessageUtils messageUtils;

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private ObjectMapper mapper;

  private static final String[] PUBLIC_MATCHERS_GET = {"/login/**"};

  private static final String[] PUBLIC_MATCHERS_SWAGGER = {"/v2/api-docs", "/configuration/ui",
      "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**"};

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(PUBLIC_MATCHERS_SWAGGER);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.headers().frameOptions().disable();
    // habilita o cors para varias origens
    // desativa csrf pois não utiliza sessão para guardar dados do usuário
    http.cors().and().csrf().disable();
    http.authorizeRequests().antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
        .anyRequest().authenticated();
    http.addFilter(
        new JWTAuthenticationFilter(authenticationManager(), jwtUtil, mapper, messageUtils));
    http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService,
        messageUtils, mapper));
    // assegurar que a aplicação não guarde dados de usuário na sessão para não haver sequestro de
    // sessão
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
    configuration.setAllowedMethods(Arrays.asList(HttpMethod.POST.toString(),
        HttpMethod.GET.toString(), HttpMethod.PUT.toString(), HttpMethod.DELETE.toString(),
        HttpMethod.OPTIONS.toString(), HttpMethod.PATCH.toString()));
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("Content-Type", "api_key", "Authorization"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
