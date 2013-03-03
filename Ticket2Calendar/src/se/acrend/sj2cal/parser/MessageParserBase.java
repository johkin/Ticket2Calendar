package se.acrend.sj2cal.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.acrend.sj2cal.util.TimeSource;

public class MessageParserBase {

  protected TimeSource timeSource;
	
  public MessageParserBase() {
    super();
    timeSource = new TimeSource();
  }

  protected String findValue(final String message, final String patternStr, final String fieldName) {

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(message);
    if (!matcher.find() || matcher.groupCount() != 1) {
      throw new IllegalArgumentException("Kunde inte tolka " + fieldName + ". Meddelande: " + message);
    }

    return matcher.group(1);
  }

  public void setTimeSource(final TimeSource timeSource) {
    this.timeSource = timeSource;
  }

  public TimeSource getTimeSource() {
    return timeSource;
  }
}