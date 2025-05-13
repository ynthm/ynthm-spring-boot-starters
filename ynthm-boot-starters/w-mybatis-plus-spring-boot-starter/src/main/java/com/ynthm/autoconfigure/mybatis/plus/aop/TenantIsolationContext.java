package com.ynthm.autoconfigure.mybatis.plus.aop;

import java.util.function.Supplier;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class TenantIsolationContext {
  private static final ThreadLocal<Boolean> BOOLEAN_THREAD_LOCAL = new ThreadLocal<>();

  private TenantIsolationContext() {}

  public static Boolean get() {
    return BOOLEAN_THREAD_LOCAL.get();
  }

  public static void set(boolean ignore) {
    BOOLEAN_THREAD_LOCAL.set(ignore);
  }

  public static void clear() {
    BOOLEAN_THREAD_LOCAL.remove();
  }

  public static void notIsolated(Runnable runnable) {
    try {
      set(true);
      runnable.run();
    } finally {
      clear();
    }
  }

  public static <T> T notIsolated(Supplier<T> supplier) {
    try {
      set(true);
      return supplier.get();
    } finally {
      clear();
    }
  }
}
