package com.ynthm.cloud.user.client;
/**
 * @author Ethan Wang
 * @version 1.0
 */
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "ms-content-sample", configuration = UploadClient.MultipartSupportConfig.class)
public interface UploadClient {

  @RequestMapping(
      value = "/upload",
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  String handleFileUpload(@RequestPart(value = "file") MultipartFile file);

  class MultipartSupportConfig {
    @Bean
    public Encoder feignFormEncoder() {
      return new SpringFormEncoder();
    }
  }
}
