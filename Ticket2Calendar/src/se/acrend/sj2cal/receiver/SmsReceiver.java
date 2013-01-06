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
import se.acrend.sj2cal.parser.OresundsTicketParser;
import se.acrend.sj2cal.parser.SmsTicketParser;
import se.acrend.sj2cal.parser.SwebusTicketParser;
import se.acrend.sj2cal.preference.PreferencesInstance;
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

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class SmsReceiver extends BroadcastReceiver {

  private static final String TAG = "SmsReceiver";

  private List<MessageParser> parsers;

  @Override
  public void onReceive(final Context context, final Intent intent) {
    EasyTracker easyTracker = EasyTracker.getInstance();
    easyTracker.setContext(context);
    Tracker tracker = EasyTracker.getTracker();
    try {
      initParsers(context);

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

      tracker.trackEvent("Sms", "parse", parser.getParserName(), null);

      CalendarHelper calendarHelper = CalendarHelper.getInstance();

      eventIds = calendarHelper.findEvents(ticket.getCode(), Confirmation.TICKET_TYPE, context);

      Uri data = CalendarHelper.getInstance().addEvent(ticket, context);

      if (data != null) {
        successfulAddEvent = true;
      }

      createNotification(context, "Ny biljett mottagen.", "Lagt till biljett.",
          "Har lagt till ny biljett i kalendern. Kontrollera informationen.", data, ticket);

      if (PrefsHelper.isReplaceTicket(context) && successfulAddEvent) {
        tracker.trackEvent("Sms", "replace ticket", null, null);
        for (Long id : eventIds) {
          calendarHelper.removeEvent(id, context);
        }
      }

      if (PrefsHelper.isDeleteProcessedMessages(context) && successfulAddEvent) {
        tracker.trackEvent("Sms", "delete processed", null, null);
        abortBroadcast();
      }
    } finally {
      if (tracker != null) {
        tracker.close();
      }
      if (easyTracker != null) {
        easyTracker.dispatch();
      }
    }
  }

  private void initParsers(final Context context) {
    if (parsers == null || parsers.isEmpty()) {

      PreferencesInstance preferencesInstance = PrefsHelper.getInstance(context);

      parsers = new ArrayList<MessageParser>();
      parsers.add(new ConfirmationParser(preferencesInstance));
      parsers.add(new SmsTicketParser(preferencesInstance));
      parsers.add(new SwebusTicketParser(preferencesInstance));
      parsers.add(new OresundsTicketParser(preferencesInstance));
    }

  }

  private void createNotification(final Context context, final String tickerText, final String title,
      final String message, final Uri data, final EventBase ticket) {
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
    notificationIntent.putExtra("beginTime", ticket.getDeparture().getTimeInMillis());
    notificationIntent.putExtra("endTime", ticket.getArrival().getTimeInMillis());
    notificationIntent.putExtra("attendeeStatus", 1);

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
