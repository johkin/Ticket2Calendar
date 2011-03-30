package se.acrend.sj2cal.taginfo;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

import se.acrend.sj2cal.taginfo.TagInfoModel.StationModel;
import android.test.AndroidTestCase;

public class TagInfoParserTest extends AndroidTestCase {

  public void testNormalInfo() {
    InputStream is = this.getClass().getResourceAsStream("/testdata/taginfo/10542.xml");

    TagInfoParser parser = new TagInfoParser();

    TagInfoModel model = parser.parse(is);

    assertEquals("10542", model.getTrainNo());
    List<StationModel> stations = model.getStations();
    assertEquals(10, stations.size());

    boolean found = false;
    for (StationModel station : stations) {
      if ("Stockholm C".equals(station.getName())) {
        found = true;
        assertEquals(Timestamp.valueOf("2011-03-30 19:58:00"),
            new Timestamp(station.getCalcArrival().getTimeInMillis()));
      }
    }
    assertEquals(true, found);
  }

}
