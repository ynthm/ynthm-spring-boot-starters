package com.ynthm.common.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 对于ipv6因为需要用两个long的大小存储所以转成了BigInteger,数据库中ipv6存成binary类型
 *
 * @author Ynthm Wang
 * @version 1.0
 */
public class IPAddressUtil {

  public static String long2Ipv4(long longIp) {
    final StringBuilder sb = StrUtil.builder();
    // 直接右移24位
    sb.append(longIp >> 24 & 0xFF);
    sb.append(CharUtil.DOT);
    // 将高8位置0，然后右移16位
    sb.append(longIp >> 16 & 0xFF);
    sb.append(CharUtil.DOT);
    sb.append(longIp >> 8 & 0xFF);
    sb.append(CharUtil.DOT);
    sb.append(longIp & 0xFF);
    return sb.toString();
  }

  public static String longToIpv4(long longIp) {
    int octet3 = (int) ((longIp >> 24) % 256);
    int octet2 = (int) ((longIp >> 16) % 256);
    int octet1 = (int) ((longIp >> 8) % 256);
    int octet0 = (int) (longIp % 256);
    return octet3 + "." + octet2 + '.' + octet1 + '.' + octet0;
  }

  public static long ipv42Long(String ip) {
    String[] octets = StringUtil.split(ip, '.');
    return (Long.parseLong(octets[0]) << 24)
        + (Long.parseLong(octets[1]) << 16)
        + (Long.parseLong(octets[2]) << 8)
        + Long.parseLong(octets[3]);
  }

  /**
   * 根据ip地址(xxx.xxx.xxx.xxx)计算出long型的数据
   *
   * @param strIP IP V4 地址
   * @return long值
   */
  public static long ipv4ToLong(String strIP) {
    // Validator.validateIpv4(strIP, "Invalid IPv4 address!");
    final long[] ip = Convert.convert(long[].class, StrUtil.split(strIP, CharUtil.DOT));
    return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
  }

  public static BigInteger ipv6ToNumber(String addr) {
    try {
      InetAddress ia = InetAddress.getByName(addr);
      byte[] byteArr = ia.getAddress();
      return new BigInteger(1, byteArr);
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }

  public static String numberToIPv6(BigInteger ipNumber) {
    StringBuilder ipString = new StringBuilder();
    BigInteger a = new BigInteger("FFFF", 16);

    for (int i = 0; i < 8; i++) {
      ipString.append(ipNumber.and(a).toString(16)).append(":");

      ipNumber = ipNumber.shiftRight(16);
    }

    return ipString.substring(0, ipString.length() - 1);
  }

  public static String getLocalIPv6Address() throws IOException {

    InetAddress inetAddress = null;

    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

    outer:
    while (networkInterfaces.hasMoreElements()) {

      Enumeration<InetAddress> inetAds = networkInterfaces.nextElement().getInetAddresses();

      while (inetAds.hasMoreElements()) {

        inetAddress = inetAds.nextElement();

        // Check if it's ipv6 address and reserved address

        if (inetAddress instanceof Inet6Address && !isReservedAddr(inetAddress)) {

          break outer;
        }
      }
    }

    String ipAddr = inetAddress.getHostAddress();

    // Filter network card No

    int index = ipAddr.indexOf('%');

    if (index > 0) {

      ipAddr = ipAddr.substring(0, index);
    }

    return ipAddr;
  }

  /**
   * Check if it's "local address" or "link local address" or "loopbackaddress"
   *
   * @param inetAddr address
   * @return result
   */
  private static boolean isReservedAddr(InetAddress inetAddr) {

    if (inetAddr.isAnyLocalAddress()
        || inetAddr.isLinkLocalAddress()
        || inetAddr.isLoopbackAddress()) {

      return true;
    }

    return false;
  }

  public static String canonize(String ipv6Address) throws IllegalArgumentException {
    if (ipv6Address == null) {
      return null;
    } else if (!mayBeIPv6Address(ipv6Address)) {
      return ipv6Address;
    } else {
      int ipv6AddressLength = ipv6Address.length();
      if (ipv6Address.contains(".")) {
        int lastColonPos = ipv6Address.lastIndexOf(58);
        int lastColonsPos = ipv6Address.lastIndexOf("::");
        if (lastColonsPos >= 0 && lastColonPos == lastColonsPos + 1) {
          ipv6AddressLength = lastColonPos + 1;
        } else {
          ipv6AddressLength = lastColonPos;
        }
      } else if (ipv6Address.contains("%")) {
        ipv6AddressLength = ipv6Address.lastIndexOf(37);
      }

      StringBuilder result = new StringBuilder();
      char[][] groups = new char[8][4];
      int groupCounter = 0;
      int charInGroupCounter = 0;
      int zeroGroupIndex = -1;
      int zeroGroupLength = 0;
      int maxZeroGroupIndex = -1;
      int maxZeroGroupLength = 0;
      boolean isZero = true;
      boolean groupStart = true;
      StringBuilder expanded = new StringBuilder(ipv6Address);
      int colonsPos = ipv6Address.indexOf("::");
      int length = ipv6AddressLength;
      int change = 0;
      int charCounter;
      int j;
      if (colonsPos >= 0 && colonsPos < ipv6AddressLength - 1) {
        charCounter = 0;

        for (j = 0; j < ipv6AddressLength; ++j) {
          if (ipv6Address.charAt(j) == ':') {
            ++charCounter;
          }
        }

        if (colonsPos == 0) {
          expanded.insert(0, "0");
          ++change;
        }

        for (j = 0; j < 8 - charCounter; ++j) {
          expanded.insert(colonsPos + 1, "0:");
          change += 2;
        }

        if (colonsPos == ipv6AddressLength - 2) {
          expanded.setCharAt(colonsPos + change + 1, '0');
        } else {
          expanded.deleteCharAt(colonsPos + change + 1);
          --change;
        }

        length = ipv6AddressLength + change;
      }

      for (charCounter = 0; charCounter < length; ++charCounter) {
        char c = expanded.charAt(charCounter);
        if (c >= 'A' && c <= 'F') {
          c = (char) (c + 32);
        }

        if (c != ':') {
          groups[groupCounter][charInGroupCounter] = c;
          if (!groupStart || c != '0') {
            ++charInGroupCounter;
            groupStart = false;
          }

          if (c != '0') {
            isZero = false;
          }
        }

        if (c == ':' || charCounter == length - 1) {
          if (isZero) {
            ++zeroGroupLength;
            if (zeroGroupIndex == -1) {
              zeroGroupIndex = groupCounter;
            }
          }

          if (!isZero || charCounter == length - 1) {
            if (zeroGroupLength > maxZeroGroupLength) {
              maxZeroGroupLength = zeroGroupLength;
              maxZeroGroupIndex = zeroGroupIndex;
            }

            zeroGroupLength = 0;
            zeroGroupIndex = -1;
          }

          ++groupCounter;
          charInGroupCounter = 0;
          isZero = true;
          groupStart = true;
        }
      }

      charCounter = groupCounter;

      for (groupCounter = 0; groupCounter < charCounter; ++groupCounter) {
        if (maxZeroGroupLength > 1
            && groupCounter >= maxZeroGroupIndex
            && groupCounter < maxZeroGroupIndex + maxZeroGroupLength) {
          if (groupCounter == maxZeroGroupIndex) {
            result.append("::");
          }
        } else {
          for (j = 0; j < 4; ++j) {
            if (groups[groupCounter][j] != 0) {
              result.append(groups[groupCounter][j]);
            }
          }

          if (groupCounter < charCounter - 1
              && (groupCounter != maxZeroGroupIndex - 1 || maxZeroGroupLength <= 1)) {
            result.append(':');
          }
        }
      }

      j = result.length();
      if (result.charAt(j - 1) == ':'
          && ipv6AddressLength < ipv6Address.length()
          && ipv6Address.charAt(ipv6AddressLength) == ':') {
        result.delete(j - 1, j);
      }

      for (int i = ipv6AddressLength; i < ipv6Address.length(); ++i) {
        result.append(ipv6Address.charAt(i));
      }

      return result.toString();
    }
  }

  public static boolean mayBeIPv6Address(String input) {
    if (input == null) {
      return false;
    } else {
      int colonsCounter = 0;
      int length = input.length();

      for (int i = 0; i < length; ++i) {
        char c = input.charAt(i);
        if (c == '.' || c == '%') {
          break;
        }

        if ((c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F') && c != ':') {
          return false;
        }

        if (c == ':') {
          ++colonsCounter;
        }
      }

      return colonsCounter >= 2;
    }
  }

  public static void main(String[] args) throws IOException {
    IPAddressUtil.ipv42Long("255.255.255.255");

    System.out.println(
        sun.net.util.IPAddressUtil.isIPv6LiteralAddress("2408:8256:3083:8c79:402e:80ba:797a:919c"));
    InetAddress inetAddr = InetAddress.getByName("2408::797a:919c");
    String hostAddress = inetAddr.getHostAddress();

    System.out.println(canonize("2408:0000:0000:797a:919c"));

    String localIPv6Address = getLocalIPv6Address();

    BigInteger bigInteger = ipv6ToNumber("2408:8256:3083:8c79:402e:80ba:797a:919c");
    System.out.println(bigInteger.bitLength());
    String s = numberToIPv6(bigInteger);

    System.out.println(hostAddress);
  }
}
