package com.ynthm.autoconfigure.minio.domain;

import com.ynthm.common.constant.CharPool;
import com.ynthm.common.util.FileUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PreSignedFormDataReq extends BaseObject {

  @Nonnull private ZonedDateTime expiration = ZonedDateTime.now().plusDays(7);

  private String contentTypeStartsWith;

  /** PostPolicy condition that 'content-length-range' is between 4kiB to 10MiB. */
  private long lowerLimit = 4 * 1024L;

  private long upperLimit = 10 * 1024 * 1024L;

  public String getContentTypeStartsWith() {
    String mimeType = FileUtil.getMimeType(getObject());
    if (mimeType.indexOf(CharPool.SLASH) >= 0) {
      mimeType = mimeType.substring(0, mimeType.indexOf(CharPool.SLASH));
    }

    return mimeType;
  }
}
