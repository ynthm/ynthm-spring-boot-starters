package com.ynthm.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class GuavaTest {

  @Test
  void testRange() {
    Map<Range<Double>, String> range2Type = Maps.newHashMap();
    range2Type.put(Range.closed(0.d, 1d), "小于1年");
    range2Type.put(Range.openClosed(1d, 2d), "1年-2年");
    range2Type.put(Range.openClosed(2d, 3d), "2年-3年");
    range2Type.put(Range.openClosed(3d, 4d), "3年-4年");
    range2Type.put(Range.openClosed(4d, 5d), "4年-5年");
    range2Type.put(Range.openClosed(5d, 100d), "5年以上");
    List<Double> sampleVoList = Lists.newArrayList();
    Map<String, Long> age2Count =
        sampleVoList.stream()
            .collect(
                Collectors.groupingBy(
                    age ->
                        range2Type.entrySet().stream()
                            .filter(r -> r.getKey().contains(age))
                            .findFirst()
                            .map(Map.Entry::getValue)
                            .get(),
                    Collectors.counting()));
  }
}
