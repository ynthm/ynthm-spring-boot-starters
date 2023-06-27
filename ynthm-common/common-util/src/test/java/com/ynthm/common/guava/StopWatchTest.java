package com.ynthm.common.guava;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

  @Test
  public void test() throws Exception {
    // 创建stopwatch并开始计时
    Stopwatch stopwatch = Stopwatch.createStarted();
    Thread.sleep(1980);

    // 以秒打印从计时开始至现在的所用时间，向下取整
    System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));

    // 停止计时
    stopwatch.stop();
    Thread.sleep(1000);
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    System.out.println(stopwatch.toString());

    // 再次计时
    stopwatch.start();
    Thread.sleep(100);
    System.out.println(stopwatch.elapsed(TimeUnit.SECONDS)); // 2

    // 重置并开始
    stopwatch.reset().start();
    Thread.sleep(1030);

    // 检查是否运行
    System.out.println(stopwatch.isRunning()); // true
    long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS); // 1030
    System.out.println(millis);
    Thread.sleep(100);
    System.out.println(stopwatch.toString());
  }
}
