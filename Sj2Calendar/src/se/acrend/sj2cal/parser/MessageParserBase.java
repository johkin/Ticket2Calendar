package se.acrend.sj2cal.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParserBase {

  public MessageParserBase() {
    super();
  }

  protected String findValue(final String message, final String patternStr, final String fieldName) {
  
    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(message);
    if (!matcher.find() || matcher.groupCount() != 1) {
      throw new IllegalArgumentException("Kunde inte tolka " + fieldName);
    }
  
    return matcher.group(1);
  }

}