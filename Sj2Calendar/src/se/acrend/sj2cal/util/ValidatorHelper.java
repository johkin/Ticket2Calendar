package se.acrend.sj2cal.util;

import java.util.Calendar;

public class ValidatorHelper {

  public static void notEmpty(final String value, final String fieldName) {
    if (value == null || value.length() == 0) {
      throw new IllegalArgumentException("Fältet " + fieldName + " får inte vara tomt.");
    }
  }

  public static void notEmpty(final Calendar value, final String fieldName) {
    if (value == null || value.getTimeInMillis() == 0) {
      throw new IllegalArgumentException("Fältet " + fieldName + " får inte vara tomt.");
    }
  }

  public static void inRange(final int value, final int min, final int max, final String fieldName) {
    if (value <= min || value >= max) {
      throw new IllegalArgumentException("Fältet " + fieldName + " får inte vara tomt.");
    }
  }

}
