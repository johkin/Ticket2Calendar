package se.acrend.sj2cal;

import java.util.ArrayList;
import java.util.List;

import se.acrend.sj2cal.calendar.CalendarHelper;
import se.acrend.sj2cal.model.EventBase;
import se.acrend.sj2cal.parser.ConfirmationParser;
import se.acrend.sj2cal.parser.MessageParser;
import se.acrend.sj2cal.parser.SmsTicketParser;
import se.acrend.sj2cal.util.PrefsHelper;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

  private final List<MessageParser> parsers;

  public SmsReceiver() {
    parsers = new ArrayList<MessageParser>();
    parsers.add(new ConfirmationParser());
    parsers.add(new SmsTicketParser());
  }

  @Override
  public void onReceive(final Context context, final Intent intent) {
    if (!PrefsHelper.isProcessIncommingMessages(context)) {
      return;
    }
    Bundle bundle = intent.getExtras();

    boolean successfulParsing = false;
    boolean successfulAddEvent = false;

    Object messages[] = (Object[]) bundle.get("pdus");
    String msgBody = "";
    for (int n = 0; n < messages.length; n++) {
      SmsMessage message = SmsMessage.createFromPdu((byte[]) messages[n]);
      msgBody += message.getDisplayMessageBody();
    }
    MessageParser parser = getMessageParser(msgBody);
    if (parser == null) {
      return;
    }

    Notification notification = new Notification();
    notification.icon = R.drawable.sj2cal_bw;
    notification.when = System.currentTimeMillis();
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    notification.tickerText = "Nya biljetter mottagna.";
    Intent notificationIntent = new Intent("se.acrend.sj2cal.OpenReceivedTickets");
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

    try {
      EventBase ticket = parser.parse(msgBody);
      successfulParsing = true;
      successfulAddEvent = CalendarHelper.addEvent(ticket, context);
      notification.setLatestEventInfo(context, "Lagt till biljett.",
          "Har lagt till nya biljetter i kalendern. Kontrollera informationen.", contentIntent);
    } catch (IllegalArgumentException e) {
      Log.e("Text Parse", "Fel vid parsning", e);
      successfulParsing = false;
      notification.setLatestEventInfo(context, "Fel vid mottagning av biljett.",
          "Kunde inte hantera biljett, kontrollera meddelande i inkorgen.", contentIntent);
    }

    try {
      NotificationManager notificationManager = (NotificationManager) context
          .getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(1, notification);
    } catch (Exception e) {
      Log.e("Notification", "Kunde inte lägga till notifiering", e);
    }

    if (PrefsHelper.isDeleteProcessedMessages(context) && successfulParsing && successfulAddEvent) {
      abortBroadcast();
    }
  }

  private MessageParser getMessageParser(final String message) {
    for (MessageParser parser : parsers) {
      if (parser.supports(message)) {
        return parser;
      }
    }
    return null;
  }
}
