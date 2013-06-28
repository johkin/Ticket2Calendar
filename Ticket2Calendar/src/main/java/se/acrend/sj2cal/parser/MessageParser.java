package se.acrend.sj2cal.parser;

import se.acrend.sj2cal.model.EventBase;

public interface MessageParser {

  String getParserName();

  boolean supports(final String sender, final String message);

  EventBase parse(final String message);

}