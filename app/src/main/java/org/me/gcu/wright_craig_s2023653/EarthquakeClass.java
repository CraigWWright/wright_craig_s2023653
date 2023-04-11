package org.me.gcu.wright_craig_s2023653;

import java.util.Date;

public class EarthquakeClass {
    private String title;
    private String location;
    private String description;
    private String link;
    private String pubDate;
    private String formattedDate;
    private String category;
    private double latitude;
    private double longitude;
    private int depth;
    private double magnitude;

    public EarthquakeClass() {
        title = "";
        location = "";
        description = "";
        link = "";
        pubDate = "";
        formattedDate = "";
        category = "";
        latitude = 0;
        longitude = 0;
        depth = 0;
        magnitude = 0;
    }

    public EarthquakeClass(String aTitle, String aLocation, String aDescription, String aLink, String aPubDate, String aDate, String aCategory, Double aLatitude, Double aLongitude, int aDepth, double aMagnitude) {
        title = aTitle;
        location = aLocation;
        description = aDescription;
        link = aLink;
        pubDate = aPubDate;
        formattedDate = aDate;
        category = aCategory;
        latitude = aLatitude;
        longitude = aLongitude;
        depth = aDepth;
        magnitude = aMagnitude;
    }

    public String getTitle() {return title;}

    public void setTitle(String aTitle) {title = aTitle;}

    public String getLocation() {return location;}
    public void setLocation(String aLocation) {location = aLocation;}

    public String getDescription() {return description;}

    public void setDescription(String aDescription) {description = aDescription;}

    public String getLink() {return link;}

    public void setLink(String aLink) {link = aLink;}

    public String getPubDate() {return pubDate;}

    public void setPubDate(String aPubDate) {pubDate = aPubDate;}

    public String getDate() {return formattedDate;}

    public void setDate(String aDate) {formattedDate = aDate;}

    public String getCategory() {return category;}

    public void setCategory(String aCategory) {category = aCategory;}

    public Double getLatitude() {return latitude;}

    public void setLatitude(Double aLatitude) {latitude = aLatitude;}

    public Double getLongitude() {return longitude;}

    public void setLongitude(Double aLongitude) {longitude = aLongitude;}

    public int getDepth() {return depth;}

    public void setDepth(int aDepth) {depth = aDepth;}

    public double getMagnitude() {return magnitude;}

    public void setMagnitude(double aMagnitude) {magnitude = aMagnitude;}


    public String toString(){
        return title;
    }

    public String detailedView() {
        String temp = "";

        //temp = title + " " + description + " " + link + " " + pubDate  + " " + category + " " + latitude + " " + longitude;
        temp = "Title: " + title  + "\n\n" + "Description: " + description  + "\n\n" + "Link: " + link  + "\n\n" + "Date Published: " + pubDate  + "\n\n" + "Category: " + category  + "\n\n" + "Latitude: " + latitude  + "\n\n" + "Longitude: " + longitude + "\n\n" + "Magnitude: " + magnitude;

        return temp;
    }
}
