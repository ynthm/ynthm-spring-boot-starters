package com.ynthm.common.util;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class IpAddressUtilTest {

  @Test
  void long2Ipv4() throws UnknownHostException {

    System.out.println(IpAddressUtil.ipv42Long("255.255.255.255"));

    System.out.println(
        sun.net.util.IPAddressUtil.isIPv6LiteralAddress("2408:8256:3083:8c79:402e:80ba:797a:919c"));
    InetAddress inetAddr = InetAddress.getByName("2408::797a:919c");
    String hostAddress = inetAddr.getHostAddress();

    System.out.println(IpAddressUtil.canonize("2408:0000:0000:797a:919c"));

    String localIPv6Address = IpAddressUtil.getLocalIpv6Address();

    BigInteger bigInteger = IpAddressUtil.ipv6ToNumber("2408:8256:3083:8c79:402e:80ba:797a:919c");
    System.out.println(bigInteger.bitLength());
    String s = IpAddressUtil.numberToIpv6(bigInteger);

    System.out.println(hostAddress);
  }

  @Test
  void longToIpv4() {}

  @Test
  void ipv42Long() {}

  @Test
  void ipv6ToNumber() {}
}
