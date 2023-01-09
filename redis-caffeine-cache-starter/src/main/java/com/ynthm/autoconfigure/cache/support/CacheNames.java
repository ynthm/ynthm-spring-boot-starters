package com.ynthm.autoconfigure.cache.support;

/**
 * @author Ethan Wang
 */
public interface CacheNames {
  /**
   * 15分钟缓存组
   */
  String CACHE_15_MIN = "cache:15m";
  /**
   * 30分钟缓存组
   */
  String CACHE_30MIN = "cache:30m";
  /**
   * 60分钟缓存组
   */
  String CACHE_60MIN = "cache:60m";
  /**
   * 2小时缓存组
   */
  String CACHE_2HOUR = "cache:2h";
  /**
   * 12Hours分钟缓存组
   */
  String CACHE_12HOUR = "cache:12h";
}