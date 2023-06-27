package com.ynthm.common.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * Guava Cache与ConcurrentMap很相似，但也不完全一样。 最基本的区别是ConcurrentMap会一直保存所有添加的元素，直到显式地移除。 相对地，Guava
 * Cache为了限制内存占用，通常都设定为自动回收元素。 在某些场景下，尽管LoadingCache 不回收元素，它也是很有用的，因为它会自动加载缓存。
 * http://ifeve.com/google-guava-cachesexplained/
 */
public class CacheTest {

  @Test
  void testCache() throws InterruptedException {
    // 创建 cache ，过期时间 2 s
    Cache<String, String> cache =
        CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).build();
    // 向缓存中添加 数据 K V 形式
    cache.put("hello", "where are you");
    // 获取 key = hello 的 值
    System.out.println(cache.getIfPresent("hello"));
    // 延迟3 秒
    TimeUnit.SECONDS.sleep(3l);
    // return null if not present
    System.out.println(cache.getIfPresent("hello"));
  }

  @Test
  void testLoadingCache() {
    LoadingCache<String, String> graphs =
        CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build(
                new CacheLoader<String, String>() {
                  @Override
                  public String load(String key) {
                    System.out.println("load key ：" + key);
                    return key + " ：value";
                  }
                });
    // 由于CacheLoader可能抛出异常，LoadingCache.get(K)也声明为抛出ExecutionException异常。如果你定义的CacheLoader没有声明任何检查型异常，则可以通过getUnchecked(K)查找缓存；但必须注意，一旦CacheLoader声明了检查型异常，就不可以调用getUnchecked(K)。
    System.out.println(graphs.getUnchecked("hello"));
    System.out.println(graphs.getUnchecked("hello"));

    // 基于容量的回收（size-based eviction）CacheBuilder.maximumSize(long)
  }
}
