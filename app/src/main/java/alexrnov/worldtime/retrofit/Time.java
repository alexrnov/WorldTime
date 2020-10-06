package alexrnov.worldtime.retrofit;

import com.google.gson.annotations.SerializedName;

public class Time {

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
