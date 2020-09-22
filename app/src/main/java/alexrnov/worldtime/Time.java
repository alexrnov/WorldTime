package alexrnov.worldtime;

import com.google.gson.annotations.SerializedName;

class Time {

  @SerializedName("datetime")
  private String dateTime;

  public Time(String dateTime) {
    this.dateTime = dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public String getDateTime() {
    return dateTime;
  }
}
