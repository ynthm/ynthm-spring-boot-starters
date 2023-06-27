package com.ynthm.cloud.gateway.config;

import jakarta.annotation.Resource;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.web.access.server.BearerTokenServerAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Resource private CommonProperties commonProperties;

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    // @formatter:off
    http.authorizeExchange(
            exchanges -> {
              Optional.ofNullable(commonProperties.getWhitelistUrls())
                  .ifPresent(
                      urls -> {
                        if (!urls.isEmpty()) {
                          exchanges.pathMatchers(urls.toArray(new String[0])).permitAll();
                        }
                      });
              exchanges
                  .anyExchange()
                  .authenticated();
            })
        .httpBasic(Customizer.withDefaults())
        .oauth2ResourceServer(
            oAuth2ResourceServerSpec -> {
              oAuth2ResourceServerSpec.jwt(jwtSpec -> {});
            })
        .exceptionHandling(
            exceptionHandlingSpec ->
                exceptionHandlingSpec
                    .authenticationEntryPoint(new BearerTokenServerAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenServerAccessDeniedHandler()));

    // @formatter:on
    return http.build();
  }

  @Bean
  ReactiveJwtDecoder reactiveJwtDecoder() {
    return NimbusReactiveJwtDecoder.withPublicKey(this.commonProperties.getJwt().getPublicKey())
        .build();
  }

//  @Bean
//  JwtEncoder jwtEncoder() {
//    CommonProperties.JwtProperties jwtProperties = this.commonProperties.getJwt();
//    JWK jwk =
//        new RSAKey.Builder(jwtProperties.getPublicKey())
//            .privateKey(jwtProperties.getPrivateKey())
//            .build();
//    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
//    return new NimbusJwtEncoder(jwks);
//  }

  //  @Bean
  //  SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
  //    return http.httpBasic()
  //        .and()
  //        .csrf()
  //        .disable()
  //        .authorizeExchange()
  //        .pathMatchers("/anything/**")
  //        .authenticated()
  //        .anyExchange()
  //        .permitAll()
  //        .and()
  //        .build();
  //  }

  @Bean
  public MapReactiveUserDetailsService reactiveUserDetailsService() {
    UserDetails user =
        User.withUsername("user")
            // password
            .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
            .roles("USER")
            .build();

    UserDetails admin =
        User.withUsername("admin").password("{noop}admin").roles("USER", "ADMIN").build();
    return new MapReactiveUserDetailsService(user, admin);
  }

  //  ServerAuthenticationEntryPoint
  //  ServerAccessDeniedHandler 用户无权限处理
  // JwtGrantedAuthoritiesConverter 默认权限转换器
}
