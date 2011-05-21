package se.acrend.sj2cal.parser;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParserBase {

  private Locale locale = null;

  public MessageParserBase() {
    super();
    locale = new Locale("sv", "SE");
  }

  Locale getLocale() {
    return locale;
  }

  protected String findValue(final String message, final String patternStr, final String fieldName) {

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(message);
    if (!matcher.find() || matcher.groupCount() != 1) {
      throw new IllegalArgumentException("Kunde inte tolka " + fieldName + ". Meddelande: " + message);
    }

    return matcher.group(1);
  }

}