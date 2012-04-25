package se.acrend.sj2cal.parser;

import se.acrend.sj2cal.model.EventBase;

public interface MessageParser {

  boolean supports(String sender, String message);

  EventBase parse(String message);

}