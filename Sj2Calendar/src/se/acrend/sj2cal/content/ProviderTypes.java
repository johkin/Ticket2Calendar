package se.acrend.sj2cal.content;

import android.net.Uri;

public class ProviderTypes {

  static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
  static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
  static final String TICKET_MIME_TYPE = "se.acrend.sj2cal.ticket";

  public static final String TICKET_ITEM_TYPE = MIME_ITEM_PREFIX + "/"
      + TICKET_MIME_TYPE;
  public static final String TICKET_MULTIPLE_TYPE = MIME_DIR_PREFIX + "/"
      + TICKET_MIME_TYPE;

  public static final String AUTHORITY = "se.acrend.sj2cal";
  public static final String PATH_SINGLE = "tickets/#";
  public static final String PATH_MULTIPLE = "tickets";

  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
      + "/" + PATH_MULTIPLE);

}
