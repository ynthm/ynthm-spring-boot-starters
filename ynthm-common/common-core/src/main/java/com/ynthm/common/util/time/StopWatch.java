package com.ynthm.common.util.time;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Slf4j
public class StopWatch {
  private final String id;
  private final List<TaskInfo> taskList;
  private boolean keepTaskList;
  private long startTimeNanos;

  private String currentTaskName;

  private TaskInfo lastTaskInfo;
  private int taskCount;
  private long totalTimeNanos;

  public StopWatch() {
    this("");
  }

  public StopWatch(String id) {
    this.keepTaskList = true;
    this.taskList = new ArrayList<>(1);
    this.id = id;
  }

  private static long nanosToMillis(long duration) {
    return TimeUnit.NANOSECONDS.toMillis(duration);
  }

  private static double nanosToSeconds(long duration) {
    return duration / 1.0E9;
  }

  public String getId() {
    return this.id;
  }

  public void setKeepTaskList(boolean keepTaskList) {
    this.keepTaskList = keepTaskList;
  }

  /**
   * Start an unnamed task.
   *
   * <p>The results are undefined if {@link #stop()} or timing methods are called without invoking
   * this method first.
   *
   * @see #start(String)
   * @see #stop()
   */
  public void start() throws IllegalStateException {
    start("");
  }

  /**
   * Start a named task.
   *
   * <p>The results are undefined if {@link #stop()} or timing methods are called without invoking
   * this method first.
   *
   * @param taskName the name of the task to start
   * @see #start()
   * @see #stop()
   */
  public void start(String taskName) throws IllegalStateException {
    if (this.currentTaskName != null) {
      throw new IllegalStateException("Can't start StopWatch: it's already running");
    }
    this.currentTaskName = taskName;
    this.startTimeNanos = System.nanoTime();
  }

  /**
   * Stop the current task.
   *
   * <p>The results are undefined if timing methods are called without invoking at least one pair of
   * {@code start()} / {@code stop()} methods.
   *
   * @see #start()
   * @see #start(String)
   */
  public void stop() throws IllegalStateException {
    if (this.currentTaskName == null) {
      throw new IllegalStateException("Can't stop StopWatch: it's not running");
    }
    long lastTime = System.nanoTime() - this.startTimeNanos;
    this.totalTimeNanos += lastTime;
    this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
    if (this.keepTaskList) {
      this.taskList.add(this.lastTaskInfo);
    }
    ++this.taskCount;
    this.currentTaskName = null;
  }

  public void stopAndPrint() throws IllegalStateException {
    stop();
    printCost4LastTask();
  }

  public boolean isRunning() {
    return this.currentTaskName != null;
  }

  public String currentTaskName() {
    return this.currentTaskName;
  }

  public long getLastTaskTimeNanos() throws IllegalStateException {
    if (this.lastTaskInfo == null) {
      throw new IllegalStateException("No tasks run: can't get last task interval");
    } else {
      return this.lastTaskInfo.getTimeNanos();
    }
  }

  public void printCost4LastTask() {
    if (this.lastTaskInfo == null) {
      throw new IllegalStateException("No tasks run: can't get last task interval");
    } else {
      log.info(
          "耗时:{} --- {}",
          TimeUtil.formatTimeCost(lastTaskInfo.getTimeMillis()),
          lastTaskInfo.getTaskName());
    }
  }

  public void printCostStat() {
    // 日志框架打印换行
    StringBuilder sb =
        new StringBuilder(
            "\n" + this.getId() + " 总耗时: " + TimeUtil.formatTimeCost(this.getTotalTimeMillis()));
    sb.append('\n');
    if (!this.keepTaskList) {
      sb.append("No task info kept");
    } else {
      sb.append("---------------------------------------------\n");
      sb.append("耗时         %     任务名\n");
      sb.append("---------------------------------------------\n");

      NumberFormat pf = NumberFormat.getPercentInstance();
      pf.setMinimumIntegerDigits(3);
      pf.setGroupingUsed(false);
      for (TaskInfo task : getTaskInfo()) {
        sb.append(TimeUtil.formatTimeCost(task.getTimeMillis())).append("  ");
        sb.append(pf.format((double) task.getTimeNanos() / getTotalTimeNanos())).append("  ");
        sb.append(task.getTaskName()).append('\n');
      }
    }
    log.info(sb.toString());
  }

  public long getLastTaskTimeMillis() throws IllegalStateException {
    if (this.lastTaskInfo == null) {
      throw new IllegalStateException("No tasks run: can't get last task interval");
    } else {
      return this.lastTaskInfo.getTimeMillis();
    }
  }

  public String getLastTaskName() throws IllegalStateException {
    if (this.lastTaskInfo == null) {
      throw new IllegalStateException("No tasks run: can't get last task name");
    } else {
      return this.lastTaskInfo.getTaskName();
    }
  }

  public TaskInfo getLastTaskInfo() throws IllegalStateException {
    if (this.lastTaskInfo == null) {
      throw new IllegalStateException("No tasks run: can't get last task info");
    } else {
      return this.lastTaskInfo;
    }
  }

  public long getTotalTimeNanos() {
    return this.totalTimeNanos;
  }

  public long getTotalTimeMillis() {
    return nanosToMillis(this.totalTimeNanos);
  }

  public double getTotalTimeSeconds() {
    return nanosToSeconds(this.totalTimeNanos);
  }

  public int getTaskCount() {
    return this.taskCount;
  }

  /** Get an array of the data for tasks performed. */
  public TaskInfo[] getTaskInfo() {
    if (!this.keepTaskList) {
      throw new UnsupportedOperationException("Task info is not being kept!");
    }
    return this.taskList.toArray(new TaskInfo[0]);
  }

  public String shortSummary() {
    return "StopWatch '" + this.getId() + "': running time = " + this.getTotalTimeNanos() + " ns";
  }

  /**
   * Generate a string with a table describing all tasks performed.
   *
   * <p>For custom reporting, call {@link #getTaskInfo()} and use the task info directly.
   */
  public String prettyPrint() {
    // 日志框架打印换行
    StringBuilder sb =
        new StringBuilder(
            "StopWatch '" + this.getId() + "'\nrunning time = " + this.getTotalTimeNanos() + " ns");
    sb.append('\n');
    if (!this.keepTaskList) {
      sb.append("No task info kept");
    } else {
      sb.append("---------------------------------------------\n");
      sb.append("ns         %     Task name\n");
      sb.append("---------------------------------------------\n");
      NumberFormat nf = NumberFormat.getNumberInstance();
      nf.setMinimumIntegerDigits(9);
      nf.setGroupingUsed(false);
      NumberFormat pf = NumberFormat.getPercentInstance();
      pf.setMinimumIntegerDigits(3);
      pf.setGroupingUsed(false);
      for (TaskInfo task : getTaskInfo()) {
        sb.append(nf.format(task.getTimeNanos())).append("  ");
        sb.append(pf.format((double) task.getTimeNanos() / getTotalTimeNanos())).append("  ");
        sb.append(task.getTaskName()).append('\n');
      }
    }
    return sb.toString();
  }

  /**
   * Generate an informative string describing all tasks performed
   *
   * <p>For custom reporting, call {@link #getTaskInfo()} and use the task info directly.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(shortSummary());
    if (this.keepTaskList) {
      for (TaskInfo task : getTaskInfo()) {
        sb.append("; [")
            .append(task.getTaskName())
            .append("] took ")
            .append(task.getTimeNanos())
            .append(" ns");
        long percent = Math.round(100.0 * task.getTimeNanos() / getTotalTimeNanos());
        sb.append(" = ").append(percent).append('%');
      }
    } else {
      sb.append("; no task info kept");
    }
    return sb.toString();
  }

  public static final class TaskInfo {
    private final String taskName;
    private final long timeNanos;

    TaskInfo(String taskName, long timeNanos) {
      this.taskName = taskName;
      this.timeNanos = timeNanos;
    }

    public String getTaskName() {
      return this.taskName;
    }

    public long getTimeNanos() {
      return this.timeNanos;
    }

    public long getTimeMillis() {
      return nanosToMillis(this.timeNanos);
    }

    public double getTimeSeconds() {
      return nanosToSeconds(this.timeNanos);
    }
  }
}
