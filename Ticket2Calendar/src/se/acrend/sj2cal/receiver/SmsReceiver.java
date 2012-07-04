package se.acrend.sj2cal.receiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.acrend.sj2cal.R;
import se.acrend.sj2cal.calendar.CalendarHelper;
import se.acrend.sj2cal.model.Confirmation;
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
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

  private static final String TAG = "SmsReceiver";

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
    String sender = null;
    for (Object message2 : messages) {
      SmsMessage message = SmsMessage.createFromPdu((byte[]) message2);

      String displayOriginatingAddress = message.getDisplayOriginatingAddress();
      sender = displayOriginatingAddress;
      msgBody += message.getDisplayMessageBody();
    }
    MessageParser parser = getMessageParser(sender, msgBody);
    if (parser == null) {
      return;
    }

    List<Long> eventIds = Collections.emptyList();

    EventBase ticket = parser.parse(msgBody);

    CalendarHelper calendarHelper = CalendarHelper.getInstance();

    eventIds = calendarHelper.findEvents(ticket.getCode(), Confirmation.TICKET_TYPE, context);

    Uri data = CalendarHelper.getInstance().addEvent(ticket, context);

    if (data != null) {
      successfulAddEvent = true;
    }

    createNotification(context, "Nya biljetter mottagna.", "Lagt till biljett.",
        "Har lagt till nya biljetter i kalendern. Kontrollera informationen.", data);

    if (PrefsHelper.isReplaceTicket(context) && successfulAddEvent) {
      for (Long id : eventIds) {
        calendarHelper.removeEvent(id, context);
      }
    }

    if (PrefsHelper.isDeleteProcessedMessages(context) && successfulAddEvent) {
      abortBroadcast();
    }
  }

  private void createNotification(final Context context, final String tickerText, final String title,
      final String message, final Uri data) {
    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);

    Notification notification = new Notification();
    notification.icon = R.drawable.ticket2calendar_bw;
    notification.when = System.currentTimeMillis();
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    notification.tickerText = tickerText;
    String value = PrefsHelper.getNotificationSound(context);
    if (value.trim().length() > 0) {
      notification.sound = Uri.parse(value);
    }
    notification.defaults = Notification.DEFAULT_LIGHTS;
    if (PrefsHelper.isNotificationVibration(context)) {
      notification.defaults |= Notification.DEFAULT_VIBRATE;
    }
    
    Intent notificationIntent = new Intent(Intent.ACTION_VIEW, data);
    
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

    notification.setLatestEventInfo(context, title, message, contentIntent);

    notificationManager.notify(1, notification);
  }

  private MessageParser getMessageParser(final String sender, final String message) {
    for (MessageParser parser : parsers) {
      if (parser.supports(sender, message)) {
        return parser;
      }
    }
    return null;
  }
}
