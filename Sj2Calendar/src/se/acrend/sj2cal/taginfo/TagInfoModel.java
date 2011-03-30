package se.acrend.sj2cal.taginfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TagInfoModel {

  private String trainNo;
  private String date;
  private final List<StationModel> stations = new ArrayList<TagInfoModel.StationModel>();

  public String getTrainNo() {
    return trainNo;
  }

  public void setTrainNo(final String trainNo) {
    this.trainNo = trainNo;
  }

  public String getDate() {
    return date;
  }

  public void setDate(final String date) {
    this.date = date;
  }

  public List<StationModel> getStations() {
    return stations;
  }

  static class StationModel {
    private String name;
    private String track;
    private Calendar orgArrival;
    private Calendar orgDeparture;

    private Calendar calcArrival;
    private Calendar calcDeparture;

    private Calendar actualArrival;
    private Calendar actualDeparture;

    public String getName() {
      return name;
    }

    public void setName(final String name) {
      this.name = name;
    }

    public String getTrack() {
      return track;
    }

    public void setTrack(final String track) {
      this.track = track;
    }

    public Calendar getOrgArrival() {
      return orgArrival;
    }

    public void setOrgArrival(final Calendar orgArrival) {
      this.orgArrival = orgArrival;
    }

    public Calendar getOrgDeparture() {
      return orgDeparture;
    }

    public void setOrgDeparture(final Calendar orgDeparture) {
      this.orgDeparture = orgDeparture;
    }

    public Calendar getCalcArrival() {
      return calcArrival;
    }

    public void setCalcArrival(final Calendar calcArrival) {
      this.calcArrival = calcArrival;
    }

    public Calendar getCalcDeparture() {
      return calcDeparture;
    }

    public void setCalcDeparture(final Calendar calcDeparture) {
      this.calcDeparture = calcDeparture;
    }

    public Calendar getActualArrival() {
      return actualArrival;
    }

    public void setActualArrival(final Calendar actualArrival) {
      this.actualArrival = actualArrival;
    }

    public Calendar getActualDeparture() {
      return actualDeparture;
    }

    public void setActualDeparture(final Calendar actualDeparture) {
      this.actualDeparture = actualDeparture;
    }

    public StationModel copy() {
      StationModel copy = new StationModel();
      copy.actualArrival = this.actualArrival;
      copy.actualDeparture = this.actualDeparture;
      copy.calcArrival = this.calcArrival;
      copy.calcDeparture = this.calcDeparture;
      copy.name = this.name;
      copy.orgArrival = this.orgArrival;
      copy.orgDeparture = this.orgDeparture;
      copy.track = this.track;
      return copy;
    }
  }
}
