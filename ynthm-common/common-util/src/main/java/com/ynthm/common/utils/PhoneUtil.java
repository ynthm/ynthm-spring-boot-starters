package com.ynthm.common.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;

import java.util.Locale;

/**
 * @author : Ethan Wang
 */
public class PhoneUtil {

  private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
  private static String LANGUAGE = "CN";

  private PhoneUtil() {}

  /**
   * 根据国家代码和手机号 判断手机号是否有效
   *
   * @param mobileNumber 18588888888
   * @param lang CN
   * @return 手机号是否正确
   */
  public static boolean checkPhoneNumber(String mobileNumber, String lang)
      throws NumberParseException {

    // 美国打瑞士号码
    Phonenumber.PhoneNumber swissNumberProto = phoneNumberUtil.parse("044 668 18 00", "CH");
    System.out.println(phoneNumberUtil.formatOutOfCountryCallingNumber(swissNumberProto, "US"));

    Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(mobileNumber, lang);

    return phoneNumberUtil.isValidNumber(phoneNumber);
  }

  /**
   * 根据国家代码和手机号 判断手机号是否有效
   *
   * @param phoneNumber 18588888888
   * @param countryCode 国家码 86
   * @return 手机号是否正确
   */
  public static boolean checkPhoneNumber(Long phoneNumber, int countryCode) {
    Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
    pn.setCountryCode(countryCode);
    pn.setNationalNumber(phoneNumber);
    return phoneNumberUtil.isValidNumber(pn);
  }

  public static void main(String[] args) throws Exception {
    System.out.println(PhoneUtil.checkPhoneNumber(18588888888L, 86));
    System.out.println(PhoneUtil.checkPhoneNumber("18588888888", LANGUAGE));

    //        Locale[] availableLocales = Locale.getAvailableLocales();
    //        for (Locale availableLocale : availableLocales) {
    //            System.out.println(availableLocale.getCountry() + "---" +
    // availableLocale.getLanguage());
    //        }
    // CountryCode countryCode = CountryCode.getByLocale(Locale.CHINA);

    CountryCode[] values = CountryCode.values();
    for (CountryCode countryCode : values) {
      System.out.println(
          countryCode.getName()
              + "---"
              + countryCode.getAlpha2()
              + "---"
              + countryCode.getAlpha3()
              + "---"
              + countryCode.getCurrency()
              + "---"
              + countryCode.getNumeric());
    }

    LanguageCode langCode = LanguageCode.getByLocale(Locale.CHINA);
    System.out.println(langCode.getName());
    System.out.println(langCode.getAlpha3());
    System.out.println(langCode.name());
  }
}
