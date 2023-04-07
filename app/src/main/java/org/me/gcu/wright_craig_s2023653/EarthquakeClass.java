package org.me.gcu.wright_craig_s2023653;

public class EarthquakeClass {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String latitude;
    private String longitude;

    public EarthquakeClass() {
        title = "";
        description = "";
        link = "";
        pubDate = "";
        category = "";
        latitude = "";
        longitude = "";
    }

    public EarthquakeClass(String aTitle, String aDescription, String aLink, String aPubDate, String aCategory, String aLatitude, String aLongitude) {
        title = aTitle;
        description = aDescription;
        link = aLink;
        pubDate = aPubDate;
        category = aCategory;
        latitude = aLatitude;
        longitude = aLongitude;
    }

    public String getTitle() {return title;}

    public void setTitle(String aTitle) {title = aTitle;}

    public String getDescription() {return description;}

    public void setDescription(String aDescription) {description = aDescription;}

    public String getLink() {return link;}

    public void setLink(String aLink) {link = aLink;}

    public String getPubDate() {return pubDate;}

    public void setPubDate(String aPubDate) {pubDate = aPubDate;}

    public String getCategory() {return category;}

    public void setCategory(String aCategory) {category = aCategory;}

    public String getLatitude() {return latitude;}

    public void setLatitude(String aLatitude) {latitude = aLatitude;}

    public String getLongitude() {return longitude;}

    public void setLongitude(String aLongitude) {longitude = aLongitude;}


    public String toString(){
        return title;
    }

    public String detailedView() {
        String temp = "";

        //temp = title + " " + description + " " + link + " " + pubDate  + " " + category + " " + latitude + " " + longitude;
        temp = "Title: " + title  + "\n" + "Description: " + description  + "\n" + "Link: " + link  + "\n" + "Date Published: " + pubDate  + "\n" + "Category: " + category  + "\n" + "Latitude: " + latitude  + "\n" + "Longitude: " + longitude;

        return temp;
    }
}
