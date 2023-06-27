package com.ynthm.common.util.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ethan
 */
public class ScheduleUtil {
  private static class SingletonHolder {
    private static final ScheduleUtil INSTANCE = new ScheduleUtil();
  }

  private ScheduleUtil() {}

  public static final ScheduleUtil instance() {
    return SingletonHolder.INSTANCE;
  }

  private final Map<String, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();
  private final ScheduledExecutorService service =
      new ScheduledThreadPoolExecutor(
          2, new ThreadFactoryBuilder().setNameFormat("schedule-pool-").setDaemon(true).build());

  public void start(BaseTask task, long delay, long period) {
    ScheduledFuture<?> scheduledFuture =
        service.scheduleAtFixedRate(task, delay, period, TimeUnit.MINUTES);

    taskMap.put(task.getId(), scheduledFuture);
  }

  /**
   * @param task
   * @param period
   */
  public void startStopWatch(BaseTask task, long period) {
    ScheduledFuture<?> scheduledFuture = service.schedule(task, period, TimeUnit.SECONDS);
    taskMap.put(task.getId(), scheduledFuture);
  }

  public void cancel(BaseTask task) {
    ScheduledFuture<?> scheduledFuture = taskMap.get(task.getId());
    if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
      scheduledFuture.cancel(false);
    }

    taskMap.remove(task.getId());
  }

  public void reset(BaseTask task, long delay, long period) {
    // 先取消任务
    cancel(task);
    start(task, delay, period);
  }
}
