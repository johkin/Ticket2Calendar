package se.acrend.sj2cal.receiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.acrend.sj2cal.R;
import se.acrend.sj2cal.calendar.CalendarHelper;
import se.acrend.sj2cal.model.EventBase;
import se.acrend.sj2cal.parser.ConfirmationParser;
import se.acrend.sj2cal.parser.MessageParser;
import se.acrend.sj2cal.parser.SmsTicketParser;
import se.acrend.sj2cal.preference.PrefsHelper;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

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

    List<Long> eventIds = Collections.emptyList();

    EventBase ticket = parser.parse(msgBody);

    eventIds = CalendarHelper.findEvents(ticket.getCode(), context);

    successfulAddEvent = CalendarHelper.addEvent(ticket, context);

    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);

    Notification notification = new Notification();
    notification.icon = R.drawable.sj2cal_bw;
    notification.when = System.currentTimeMillis();
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    notification.tickerText = "Nya biljetter mottagna.";
    Intent notificationIntent = new Intent("se.acrend.sj2cal.OpenReceivedTickets");
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

    notification.setLatestEventInfo(context, "Lagt till biljett.",
        "Har lagt till nya biljetter i kalendern. Kontrollera informationen.", contentIntent);

    notificationManager.notify(1, notification);

    if (PrefsHelper.isReplaceTicket(context) && successfulAddEvent) {
      for (Long id : eventIds) {
        CalendarHelper.removeEvent(id, context);
      }
    }

    if (PrefsHelper.isDeleteProcessedMessages(context) && successfulAddEvent) {
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
