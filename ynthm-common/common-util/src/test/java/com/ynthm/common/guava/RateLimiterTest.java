package com.ynthm.common.guava;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RateLimiterTest {
  // 每秒10个令牌
  private static RateLimiter rl = RateLimiter.create(20);
  private static BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);

  public static void main(String[] args) throws InterruptedException {

    new Thread(
            () -> {
              try {
                while (true) {
                  queue.put(System.currentTimeMillis() / 1000 + "");
                  TimeUnit.MILLISECONDS.sleep(25l);
                }

              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();

    new Thread(
            () -> {
              try {
                while (true) {
                  rl.acquire();
                  System.out.println(queue.take());
                  // TimeUnit.SECONDS.sleep(2l);
                }

              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();

    while (true) {
      TimeUnit.SECONDS.sleep(1l);
      System.out.println("#############");
    }

    //        while (true) {
    //            rl.acquire(1);
    //            System.out.println(System.currentTimeMillis() / 1000);
    //        }
  }
}
