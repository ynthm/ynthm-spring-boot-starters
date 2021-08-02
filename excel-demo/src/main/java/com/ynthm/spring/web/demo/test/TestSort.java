package com.ynthm.spring.web.demo.test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class TestSort {

  public static void main(String[] args) {
    int[] arr = new int[] {1, 2, 3, 4, 6};
    System.out.println(balancedSum(Arrays.asList(1, 2, 3, 4, 6)));
  }

  public static int balancedSum(List<Integer> nums) {

    int total = nums.stream().mapToInt(Integer::intValue).sum();
    int sum = 0;
    for (int i = 0; i < nums.size(); ++i) {
      if (2 * sum + nums.get(i) == total) {
        return i;
      }
      sum += nums.get(i);
    }
    return -1;

    return -1;
  }
}
