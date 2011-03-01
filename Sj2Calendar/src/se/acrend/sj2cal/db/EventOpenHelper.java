package se.acrend.sj2cal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventOpenHelper extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "sj2calendar";
  private static final String DICTIONARY_TABLE_NAME = "event";
  // private static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE " +
  // DICTIONARY_TABLE_NAME + " (" + KEY_WORD
  // + " TEXT, " + KEY_DEFINITION + " TEXT);";
  private static final String[] columns = { "_id", "" };

  EventOpenHelper(final Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(final SQLiteDatabase db) {
    // db.execSQL(DICTIONARY_TABLE_CREATE);
  }

  @Override
  public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    // TODO Auto-generated method stub

  }
}