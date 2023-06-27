package com.ynthm.common.util.id;

/**
 * @author Ethan Wang
 */
import com.ynthm.common.constant.StringPool;
import com.ynthm.common.exception.BaseException;
import com.ynthm.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 分布式高效有序 ID 生产黑科技(sequence)
 *
 * <p>优化开源项目：https://gitee.com/yu120/sequence
 *
 * @author ethan wang
 */
@Slf4j
public class Sequence {
  /** 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动） */
  private static final long TW_EPOCH = 1288834974657L;

  public static final int MAX_OFFSET = 5;
  /** 机器标识位数 */
  private final long workerIdBits = 5L;

  private final long datacenterIdBits = 5L;
  private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
  private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
  /** 毫秒内自增位 */
  private final long sequenceBits = 12L;

  private final long workerIdShift = sequenceBits;
  private final long datacenterIdShift = sequenceBits + workerIdBits;
  /** 时间戳左移动位 */
  private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

  private final long sequenceMask = -1L ^ (-1L << sequenceBits);

  private final long workerId;

  /** 数据标识 ID 部分 */
  private final long datacenterId;
  /** 并发控制 */
  private long sequenceNum = 0L;
  /** 上次生产 ID 时间戳 */
  private long lastTimestamp = -1L;
  /** IP 地址 */
  private InetAddress inetAddress;

  public Sequence(InetAddress inetAddress) {
    this.inetAddress = inetAddress;
    this.datacenterId = getDatacenterId(maxDatacenterId);
    this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
  }

  /**
   * 有参构造器
   *
   * @param workerId 工作机器 ID
   * @param datacenterId 序列号
   */
  public Sequence(long workerId, long datacenterId) {
    if (workerId > maxWorkerId || workerId < 0) {
      throw new IllegalArgumentException(
          String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
    }
    if (datacenterId > maxDatacenterId || datacenterId < 0) {
      throw new IllegalArgumentException(
          String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
    }
    this.workerId = workerId;
    this.datacenterId = datacenterId;
  }

  /** 获取 maxWorkerId */
  protected long getMaxWorkerId(long datacenterId, long maxWorkerId) {
    StringBuilder mpid = new StringBuilder();
    mpid.append(datacenterId);
    String name = ManagementFactory.getRuntimeMXBean().getName();
    if (StringUtil.isNotBlank(name)) {
      /*
       * GET jvmPid
       */
      mpid.append(name.split(StringPool.AT)[0]);
    }
    /*
     * MAC + PID 的 hashcode 获取16个低位
     */
    return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
  }

  /** 数据标识id部分 */
  protected long getDatacenterId(long maxDatacenterId) {
    long id = 0L;
    try {
      if (null == this.inetAddress) {
        this.inetAddress = InetAddress.getLocalHost();
      }
      NetworkInterface network = NetworkInterface.getByInetAddress(this.inetAddress);
      if (null == network) {
        id = 1L;
      } else {
        byte[] mac = network.getHardwareAddress();
        if (null != mac) {
          id =
              ((0x000000FF & (long) mac[mac.length - 2])
                      | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8)))
                  >> 6;
          id = id % (maxDatacenterId + 1);
        }
      }
    } catch (Exception e) {
      log.warn(" getDatacenterId: " + e.getMessage());
    }
    return id;
  }

  /**
   * 获取下一个 ID
   *
   * @return 下一个 ID
   */
  public synchronized long nextId() {
    long timestamp = timeGen();
    // 闰秒
    if (timestamp < lastTimestamp) {
      long offset = lastTimestamp - timestamp;
      if (offset <= MAX_OFFSET) {
        try {
          wait(offset << 1);
          timestamp = timeGen();
          if (timestamp < lastTimestamp) {
            throw new BaseException(
                String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new BaseException(e);
        }
      } else {
        throw new BaseException(
            String.format(
                "Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
      }
    }

    if (lastTimestamp == timestamp) {
      // 相同毫秒内，序列号自增
      sequenceNum = (sequenceNum + 1) & sequenceMask;
      if (sequenceNum == 0) {
        // 同一毫秒的序列数已经达到最大
        timestamp = tilNextMillis(lastTimestamp);
      }
    } else {
      // 不同毫秒内，序列号置为 1 - 2 随机数
      sequenceNum = ThreadLocalRandom.current().nextLong(1, 3);
    }

    lastTimestamp = timestamp;

    // 时间戳部分 | 数据中心部分 | 机器标识部分 | 序列号部分
    return ((timestamp - TW_EPOCH) << timestampLeftShift)
        | (datacenterId << datacenterIdShift)
        | (workerId << workerIdShift)
        | sequenceNum;
  }

  protected long tilNextMillis(long lastTimestamp) {
    long timestamp = timeGen();
    while (timestamp <= lastTimestamp) {
      timestamp = timeGen();
    }
    return timestamp;
  }

  protected long timeGen() {
    return System.currentTimeMillis();
  }

  /** 反解id的时间戳部分 */
  public static long parseIdTimestamp(long id) {
    return (id >> 22) + TW_EPOCH;
  }
}
