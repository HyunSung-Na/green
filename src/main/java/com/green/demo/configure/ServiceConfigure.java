package com.green.demo.configure;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.green.demo.security.Jwt;
import com.green.demo.util.MessageUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ServiceConfigure {

  @Bean
  public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
    MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
    MessageUtils.setMessageSourceAccessor(messageSourceAccessor);
    return messageSourceAccessor;
  }

  @Bean
  public Jwt jwt(JwtTokenConfigure jwtTokenConfigure) {
    return new Jwt(jwtTokenConfigure.getIssuer(), jwtTokenConfigure.getClientSecret(), jwtTokenConfigure.getExpirySeconds());
  }

  @Bean
  public Jackson2ObjectMapperBuilder configureObjectMapper() {
    // Java time module
    JavaTimeModule jtm = new JavaTimeModule();
    jtm.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
      @Override
      public void configure(ObjectMapper objectMapper) {
        super.configure(objectMapper);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
      }
    };
    builder.serializationInclusion(JsonInclude.Include.NON_NULL);
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    builder.modulesToInstall(jtm);
    return builder;
  }
}
