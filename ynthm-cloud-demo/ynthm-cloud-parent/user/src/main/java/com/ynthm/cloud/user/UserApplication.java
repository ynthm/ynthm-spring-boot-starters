package com.ynthm.cloud.user;

import com.ynthm.cloud.user.client.HttpBinClient;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class UserApplication {

  @Autowired HttpBinClient httpBinClient;

  public static void main(String[] args) {
    SpringApplication.run(UserApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> {
      Map<String,String> map = httpBinClient.userAgent();
      System.out.println(map);
    };
  }
}
