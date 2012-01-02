package se.acrend.sj2cal.model;


public class SwebusTicket extends EventBase {

  public static final String TICKET_TYPE = "Ticket2Cal.Swebusbiljett";
  private int train;
  private String message;

  @Override
  public String toString() {
    return message + "\n\n" + TICKET_TYPE;
  }

  public int getTrain() {
    return train;
  }

  public void setTrain(final int train) {
    this.train = train;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
