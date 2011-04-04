package se.acrend.sj2cal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TicketDatabaseHelper extends SQLiteOpenHelper {

  private static final String TAG = "TicketDatabaseHelper";

  private static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "sj2cal.db";

  //
  // private final Context context;
  //
  // private DatabaseUtils.InsertHelper ticketInserter;

  public TicketDatabaseHelper(final Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    // this.context = context;
  }

  @Override
  public void onCreate(final SQLiteDatabase db) {
    db.execSQL("CREATE TABLE ticket (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "ticketCode TEXT NOT NULL ,"
        + "ticketText TEXT NOT NULL ," + "trainNo TEXT," + "departure INTEGER NOT NULL ,"
        + // milliseconds since epoch
        "arrival INTEGER NOT NULL ,"
        + // milliseconds since epoch
        "fromStation TEXT NOT NULL ," + "toStation TEXT NOT NULL ," + "notify INTEGER NOT NULL DEFAULT 1,"
        + "delayedMinutes INTEGER NOT NULL DEFAULT 0," + "calendarEventId INTEGER);");
  }

  @Override
  public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

  }

  @Override
  public void onOpen(final SQLiteDatabase db) {
    super.onOpen(db);
    // ticketInserter = new DatabaseUtils.InsertHelper(db, "ticket");
  }

  // public long insertTicket(final ContentValues values) {
  // return ticketInserter.insert(values);
  // }
  //
  // public long replaceTicket(final ContentValues values) {
  // return ticketInserter.replace(values);
  // }

}