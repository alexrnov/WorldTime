package alexrnov.worldtime;

import com.google.gson.annotations.SerializedName;

class Movie {

  // this annotation is used to map the POJO object into to JSON response properties.
  @SerializedName("title")
  private String title;

  @SerializedName("image")
  private String imageUrl;

  public Movie(String title, String imageUrl) {
    this.title = title;
    this.imageUrl = imageUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
