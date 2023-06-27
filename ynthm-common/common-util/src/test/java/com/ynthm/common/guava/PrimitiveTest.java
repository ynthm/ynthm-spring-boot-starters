package com.ynthm.common.guava;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * https://www.tutorialspoint.com/guava/guava_ints.htm https://github.com/google/guava/wiki
 * https://github.com/google/guava/tree/master/guava-tests
 */
public class PrimitiveTest {

  @Test
  void testBytes() {
    byte[] byteArray = {1, 2, 3, 4, 5, 5, 7, 9, 9};

    // convert array of primitives to array of objects
    List<Byte> objectArray = Bytes.asList(byteArray);
    System.out.println(objectArray.toString());

    // convert array of objects to array of primitives
    byteArray = Bytes.toArray(objectArray);
    System.out.print("[ ");

    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteArray[i] + " ");
    }

    System.out.println("]");
    byte data = 5;

    // check if element is present in the list of primitives or not
    System.out.println("5 is in list? " + Bytes.contains(byteArray, data));

    // Returns the index
    System.out.println("Index of 5: " + Bytes.indexOf(byteArray, data));

    // Returns the last index maximum
    System.out.println("Last index of 5: " + Bytes.lastIndexOf(byteArray, data));
  }

  @Test
  void testInts() {
    int[] intArray = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    // convert array of primitives to array of objects
    List<Integer> integerList = Ints.asList(intArray);
    System.out.println(integerList.toString());

    // convert array of objects to array of primitives
    intArray = Ints.toArray(integerList);
    System.out.print("[ ");

    for (int i = 0; i < intArray.length; i++) {
      System.out.print(intArray[i] + " ");
    }

    System.out.println("]");

    // check if element is present in the list of primitives or not
    System.out.println("5 is in list? " + Ints.contains(intArray, 5));

    // Returns the minimum
    System.out.println("Min: " + Ints.min(intArray));

    // Returns the maximum
    System.out.println("Max: " + Ints.max(intArray));

    // get the byte array from an integer
    byte[] byteArray = Ints.toByteArray(20000);

    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteArray[i] + " ");
    }
  }
}
