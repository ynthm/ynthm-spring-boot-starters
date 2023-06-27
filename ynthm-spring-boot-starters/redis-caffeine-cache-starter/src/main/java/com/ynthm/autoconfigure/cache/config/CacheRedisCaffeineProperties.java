package com.ynthm.autoconfigure.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ethan Wang
 */
@ConfigurationProperties(prefix = "cache.multi")
@Data
public class CacheRedisCaffeineProperties {

  /**
   * 是否存储空值，默认 true，防止缓存穿透
   */
  private boolean cacheNullValues = true;

  /**
   * 是否动态根据 cacheName 创建 Cache 的实现，默认 true
   */
  private boolean dynamic = true;

  /**
   * 缓存 key 的前缀
   */
  private String cachePrefix;

  private Redis redis = new Redis();

  private CacheDefault cacheDefault = new CacheDefault();
  private Cache15m cache15m = new Cache15m();
  private Cache30m cache30m = new Cache30m();
  private Cache60m cache60m = new Cache60m();
  private Cache2h cache2h = new Cache2h();
  private Cache12h cache12h = new Cache12h();

  @Data
  public class Redis {

    /**
     * 全局过期时间，单位秒，默认不过期
     */
    private Duration defaultExpiration = Duration.ZERO;

    /**
     * 每个cacheName的过期时间，单位秒，优先级比defaultExpiration高
     */
    private Map<String, Duration> expires = new HashMap<>();

    /**
     * 缓存更新时通知其他节点的topic名称
     */
    private String topic = "cache:redis:caffeine:topic";

  }

  @Data
  public class CacheDefault {
    /**
     * 访问后过期时间，单位秒
     */
    protected Duration expireAfterAccess = Duration.ZERO;
    /**
     * 写入后过期时间，单位秒
     */
    protected Duration expireAfterWrite = Duration.ofHours(1);
    /**
     * 写入后刷新时间，单位秒
     */
    protected Duration refreshAfterWrite = Duration.ZERO;
    /**
     * 初始化大小,默认50
     */
    protected int initialCapacity = 50;
    /**
     * 最大缓存对象个数 超过此数量时会使用Window TinyLfu策略来淘汰缓存
     */
    protected long maximumSize = 10000;

    /** 由于权重需要缓存对象来提供，对于使用spring cache这种场景不是很适合，所以暂不支持配置*/
//		private long maximumWeight;
  }

  public class Cache15m extends CacheDefault {
  }

  public class Cache30m extends CacheDefault {
  }

  public class Cache60m extends CacheDefault {
  }

  public class Cache2h extends CacheDefault {
  }

  public class Cache12h extends CacheDefault {
  }
}
