package com.ynthm.common.util;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Random 线程安全 CAS 保证线程安全，线程竞争激烈的时候效率非常低
 *
 * <p>多线程竞争激烈的场景请使用 ThreadLocalRandom 不支持手动设置种子 启动参数 -Djava.util.secureRandomSeed=true
 * 保证种子与不同的启动时间相关
 *
 * <p>SecureRandom 加密强随机数生成器 收集一些随机事件，如鼠标点击。键盘点击等作为种子
 */
class RandomUtilTest {

  @Test
  void testRandom00() throws InterruptedException {
    for (int i = 0; i < 2; i++) {
      new Thread(
              () -> {
                Random random = new Random(100L);
                for (int j = 0; j < 5; j++) {
                  System.out.println(Thread.currentThread().getName() + ": " + random.nextInt());
                  try {
                    TimeUnit.MICROSECONDS.sleep(10L);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }
              })
          .start();
    }
    // JUnit 不支持多线程
    TimeUnit.SECONDS.sleep(1L);
  }

  @Test
  void testRandom01() {
    Random rnd = new Random();
    for (int i = 0; i < 5; i++) {
      System.out.println(rnd.nextInt());
    }
    for (int i = 0; i < 10; i++) {
      // 0 到 5 不含5
      System.out.println(rnd.nextInt(5));
    }
  }

  @Test
  void testRandom003() {
    new Random().ints(6, 3, 10).forEach(System.out::println);
  }

  @Test
  void testThreadLocalRandom01() {
    System.out.println(ThreadLocalRandom.current().nextInt(3, 10));
    ThreadLocalRandom.current().ints(10, 5, 15).forEach(System.out::println);
  }

  @Test
  void testSecureRandom() {
    // SHA1PRNG NativePRNG  -Djava.security 参数可以修改算法 或者选择 getInstance(algorithm)
    SecureRandom secureRandom = new SecureRandom();
    System.out.println(secureRandom.nextInt());
  }

  @Test
  void testSecureRandom02() throws NoSuchProviderException, NoSuchAlgorithmException {
    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");

    // Get 128 random bytes  可用做生产算法的 salt
    byte[] randomBytes = new byte[16];
    secureRandom.nextBytes(randomBytes);
    System.out.println(HexUtil.bytes2hex(randomBytes));

    // Get random integer
    int r = secureRandom.nextInt();
    System.out.println(r);

    // Get random integer in range
    int randInRange = secureRandom.nextInt(999999);
    System.out.println(randInRange);
  }

  @Test
  void testRandomPassword() {
    System.out.println(RandomUtil.randomPassword());
  }

  @Test
  void test003() {
    for (int i = 0; i < 10; i++) {
      System.out.println((int) (Math.random() * 100));
    }
  }
}
