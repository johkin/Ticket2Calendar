package se.acrend.sj2cal.taginfo;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.acrend.sj2cal.taginfo.TagInfoModel.StationModel;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

public class TagInfoParser {

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH:mm");

  public TagInfoModel parse(final InputStream is) {
    final TagInfoModel model = new TagInfoModel();
    RootElement root = new RootElement("tag");

    root.getChild("tagNr").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        model.setTrainNo(body);
      }
    });
    root.getChild("datum").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        model.setDate(body);
      }
    });
    final StationModel stationModel = new StationModel();
    Element station = root.getChild("stationer").getChild("station");
    station.setEndElementListener(new EndElementListener() {
      @Override
      public void end() {
        model.getStations().add(stationModel.copy());
      }
    });
    station.getChild("namn").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        stationModel.setName(body);
      }
    });
    station.getChild("spar").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        stationModel.setTrack(body);
      }
    });
    station.getChild("ordAnkomst").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        stationModel.setOrgArrival(parseTime(model.getDate(), body));
      }
    });
    station.getChild("ordAvgång").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        stationModel.setOrgDeparture(parseTime(model.getDate(), body));
      }
    });
    station.getChild("berAnkomst").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        stationModel.setCalcArrival(parseTime(model.getDate(), body));
      }
    });
    station.getChild("berAvgång").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        stationModel.setCalcDeparture(parseTime(model.getDate(), body));
      }
    });
    station.getChild("ankom").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        stationModel.setActualArrival(parseTime(model.getDate(), body));
      }
    });
    station.getChild("avgick").setEndTextElementListener(new EndTextElementListener() {
      @Override
      public void end(final String body) {
        stationModel.setActualDeparture(parseTime(model.getDate(), body));
      }
    });

    try {
      Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return model;
  }

  private Calendar parseTime(final String date, final String time) {
    Calendar cal = Calendar.getInstance();
    try {
      cal.setTime(dateFormat.parse(date + time));
    } catch (ParseException e) {
      Log.e("TågInfoParser", "Kunde inte läsa tid: " + date + time, e);
    }
    return cal;
  }
}
