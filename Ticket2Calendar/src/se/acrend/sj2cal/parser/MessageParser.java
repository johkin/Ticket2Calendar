package se.acrend.sj2cal.parser;

import se.acrend.sj2cal.model.EventBase;

public interface MessageParser {

  boolean supports(final String message);

  EventBase parse(final String message);

}