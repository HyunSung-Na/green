package com.green.demo.configure;


import com.green.demo.security.*;
import com.green.demo.service.resource.SecurityResourceService;
import com.green.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

  private final Jwt jwt;

  private final JwtTokenConfigure jwtTokenConfigure;

  private final JwtAccessDeniedHandler accessDeniedHandler;

  private final EntryPointUnauthorizedHandler unauthorizedHandler;

  private final CorsConfig corsConfig;

  private final SecurityResourceService securityResourceService;

  @Bean
  public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
    return new JwtAuthenticationTokenFilter(jwtTokenConfigure.getHeader(), jwt);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/swagger-resources", "/webjars/**", "/static/**", "/templates/**", "/h2/**");
  }

  @Autowired
  public void configureAuthentication(AuthenticationManagerBuilder builder, JwtAuthenticationProvider authenticationProvider) {
    builder.authenticationProvider(authenticationProvider);
  }

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider(Jwt jwt, UserService userService) {
    return new JwtAuthenticationProvider(jwt, userService);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public AccessDecisionManager accessDecisionManager() {
    return new AffirmativeBased(getAccessDecisionVoters());
  }

  @Bean
  public List<AccessDecisionVoter<?>> getAccessDecisionVoters() {

    List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
    accessDecisionVoters.add(roleVoter());

    return accessDecisionVoters;
  }

  @Bean
  public AccessDecisionVoter<? extends Object> roleVoter() {
    return new RoleHierarchyVoter(roleHierarchy());
  }

  @Bean
  public RoleHierarchyImpl roleHierarchy() {
    return new RoleHierarchyImpl();
  }

  @Bean
  public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
    FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
    filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetaDataSource());
    filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
    filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
    return filterSecurityInterceptor;
  }

  @Bean
  public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetaDataSource() throws Exception {
    return new UrlFilterInvocationSecurityMetaDataSource(urlResourcesMapFactoryBean().getObject(), securityResourceService);
  }

  @Bean
  public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
    return new UrlResourcesMapFactoryBean(securityResourceService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf()
        .disable()
      .headers()
        .disable()
      .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(unauthorizedHandler)
        .and()
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .addFilter(corsConfig.corsFilter())
            .formLogin()
        .disable();
    http
      .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);
  }

}