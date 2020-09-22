package alexrnov.worldtime;

import com.google.gson.annotations.SerializedName;

class Movie {

  // this annotation is used to map the POJO object into to JSON response properties.
  @SerializedName("title")
  private String title;

  @SerializedName("image")
  private String imageUrl;

  @SerializedName("id")
  private Integer id;

  public Movie(String title, String imageUrl, Integer id) {
    this.title = title;
    this.imageUrl = imageUrl;
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }


}
