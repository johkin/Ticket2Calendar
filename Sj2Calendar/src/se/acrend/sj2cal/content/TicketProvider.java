package se.acrend.sj2cal.content;

import java.util.HashMap;
import java.util.Map;

import se.acrend.sj2cal.db.TicketColumns;
import se.acrend.sj2cal.db.TicketDatabaseHelper;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TicketProvider extends ContentProvider {

  private UriMatcher uriMatcher = null;
  private Map<String, String> projectionMap = null;

  private static final int TICKET = 1;
  private static final int TICKETS = 2;

  private TicketDatabaseHelper ticketDatabaseHelper;

  @Override
  public boolean onCreate() {

    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    uriMatcher.addURI(ProviderTypes.AUTHORITY, ProviderTypes.PATH_SINGLE, TICKET);
    uriMatcher.addURI(ProviderTypes.AUTHORITY, ProviderTypes.PATH_MULTIPLE, TICKETS);

    projectionMap = new HashMap<String, String>();
    projectionMap.put(TicketColumns.ARRIVAL, "arrival");
    projectionMap.put(TicketColumns.CALENDAR_EVENT_ID, "calendarEventId");
    projectionMap.put(TicketColumns.DELAYED_MINUTES, "delayedMinutes");
    projectionMap.put(TicketColumns.DEPARTURE, "departure");
    projectionMap.put(TicketColumns.FROM, "fromStation");
    projectionMap.put(TicketColumns.NOTIFY, "notify");
    projectionMap.put(TicketColumns.TICKET_CODE, "ticketCode");
    projectionMap.put(TicketColumns.TICKET_TEXT, "ticketText");
    projectionMap.put(TicketColumns.TO, "toStation");
    projectionMap.put(TicketColumns.TRAIN_NO, "trainNo");
    projectionMap.put(TicketColumns._ID, "_id");

    return true;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {

    int count = 0;
    switch (uriMatcher.match(uri)) {
    case TICKET:
      String segment = uri.getPathSegments().get(1);
      String whereSql = "_id=" + segment;
      if (!TextUtils.isEmpty(selection)) {
        whereSql += "AND (" + selection + ")";
      }

      count = getDatabaseHelper().getWritableDatabase().delete(TicketDatabaseHelper.DATABASE_NAME, whereSql,
          selectionArgs);
      break;
    case TICKETS:
      count = getDatabaseHelper().getWritableDatabase().delete(TicketDatabaseHelper.DATABASE_NAME, selection,
          selectionArgs);
      break;
    default:
      throw new IllegalArgumentException("Uknonw URI: " + uri);
    }

    return count;
  }

  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
    case TICKET:
      return ProviderTypes.TICKET_ITEM_TYPE;
    case TICKETS:
      return ProviderTypes.TICKET_MULTIPLE_TYPE;
    default:
      throw new IllegalArgumentException("Uknonw URI: " + uri);
    }
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

    SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
    String orderBy = "departure ASC";

    switch (uriMatcher.match(uri)) {
    case TICKET:
      builder.setTables("ticket");

      builder.appendWhere("_id=" + uri.getPathSegments().get(1));

      break;
    case TICKETS:
      builder.setTables("ticket");

      builder.setProjectionMap(projectionMap);
      break;
    default:
      throw new IllegalArgumentException("Uknonw URI: " + uri);
    }

    if (!TextUtils.isEmpty(sortOrder)) {
      orderBy = sortOrder;
    }
    Cursor cursor = builder.query(getDatabaseHelper().getReadableDatabase(), projection, selection, selectionArgs,
        null, null, orderBy);
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {

    if (uriMatcher.match(uri) != TICKETS) {
      throw new IllegalArgumentException("Unkown Uri: " + uri + ". Should be " + ProviderTypes.CONTENT_URI);
    }

    long id = getDatabaseHelper().getWritableDatabase().insert("ticket", null, values);

    if (id > 0) {
      Uri result = ContentUris.withAppendedId(ProviderTypes.CONTENT_URI, id);
      getContext().getContentResolver().notifyChange(result, null);
      return result;
    }

    throw new SQLException("Failed to insert row into " + uri);
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    // TODO Auto-generated method stub
    return 0;
  }

  TicketDatabaseHelper getDatabaseHelper() {
    if (ticketDatabaseHelper == null) {
      ticketDatabaseHelper = new TicketDatabaseHelper(getContext());
    }
    return ticketDatabaseHelper;
  }

}
