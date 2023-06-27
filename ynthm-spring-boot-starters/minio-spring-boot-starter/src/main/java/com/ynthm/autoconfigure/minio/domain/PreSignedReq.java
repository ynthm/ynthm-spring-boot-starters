package com.ynthm.autoconfigure.minio.domain;

import io.minio.http.Method;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PreSignedReq extends BaseObject {

  @NotNull private Method method;

  private int duration = 1;

  private TimeUnit unit = TimeUnit.DAYS;

  private Map<String, String> reqParams;
}
