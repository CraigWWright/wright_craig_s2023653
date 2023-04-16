


// Update the package name to include your Student Identifier
package org.me.gcu.wright_craig_s2023653;

//imports

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;



// Name                 Craig Wright
// Student ID           s2023653
// Programme of Study   Computing


public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {
    //rawDataDisplay no longer used
    //private TextView rawDataDisplay;

    //Initialise variables and UI widgets
    private Button startButton;
    private String result;
    private String url1 = "";
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    LinkedList<EarthquakeClass> alist;
    private ListView listView;
    private Button searchDateButton;
    private Button specificsearchButton;
    private Button closestNorthenButton;
    private Button closestSouthernButton;
    private Button closestEasternButton;
    private Button closestWesternButton;
    private Button largestButton;
    private Button deepestButton;
    private Button shallowestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set up the raw links to the graphical components
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // More Code goes here
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        alist = new LinkedList<EarthquakeClass>();

        searchDateButton = (Button) findViewById(R.id.searchDateButton);
        searchDateButton.setOnClickListener(this);

        specificsearchButton = (Button) findViewById(R.id.specificSearch);
        specificsearchButton.setOnClickListener(this);

        closestNorthenButton = (Button) findViewById(R.id.northSearchButton);
        closestNorthenButton.setOnClickListener(this);

        closestSouthernButton = (Button) findViewById(R.id.southSearchButton);
        closestSouthernButton.setOnClickListener(this);

        closestEasternButton = (Button) findViewById(R.id.eastSearchButton);
        closestEasternButton.setOnClickListener(this);

        closestWesternButton = (Button) findViewById(R.id.westSearchButton);
        closestWesternButton.setOnClickListener(this);

        largestButton = (Button) findViewById(R.id.largestButton);
        largestButton.setOnClickListener(this);

        deepestButton = (Button) findViewById(R.id.deepestButton);
        deepestButton.setOnClickListener(this);

        shallowestButton = (Button) findViewById(R.id.shallowestButton);
        shallowestButton.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    public void onItemClick(AdapterView<?> parenr, View view, int position, long id) {
        //Method for dealing with clicking on an earthquake in the list view

        //set message to detailed view of the selected earthquake
        String message = alist.get(position).detailedView();
        //set text colour
        int magnitudeColour = R.color.black;
        SpannableString spannableString = null;

        //check magnitude and set colour as appropriate
        if (alist.get(position).getMagnitude() <= 1) {
            magnitudeColour = getResources().getColor(R.color.green);
        } else if (alist.get(position).getMagnitude() > 1 && alist.get(position).getMagnitude() <= 2) {
            magnitudeColour = getResources().getColor(R.color.yellow);
        } else if (alist.get(position).getMagnitude() > 2 && alist.get(position).getMagnitude() <= 3) {
            magnitudeColour = getResources().getColor(R.color.orange);
        } else if (alist.get(position).getMagnitude() > 3) {
            magnitudeColour = getResources().getColor(R.color.red);
        }

        //set text colour to the Magnitude part of the view
        spannableString = new SpannableString(message);
        int start = spannableString.toString().indexOf("Magnitude: ");
        int end = start + 15;
        spannableString.setSpan(new ForegroundColorSpan(magnitudeColour), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //create and show dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(spannableString)
                .setTitle("Earthquake Detail")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onClick(View aview) {
        if (aview == startButton) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                startProgress();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Warning")
                        .setMessage("Your device is not connected to the internet. Please connect before continuing")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
        if (aview == searchDateButton) {
            searchDate();
        }
        if (aview == specificsearchButton) {
            specificSearch();
        }
        if (aview == closestNorthenButton) {
            findClosestNorthen();
        }
        if (aview == closestSouthernButton) {
            findClosestSouthern();
        }
        if (aview == closestEasternButton) {
            findClosestEastern();
        }
        if (aview == closestWesternButton) {
            findClosestWestern();
        }
        if (aview == largestButton) {
            findLargestEarthquake();
        }
        if (aview == deepestButton) {
            findDeepestEarthquake();
        }
        if (aview == shallowestButton) {
            finsShallowestEarthquake();
        }
    }

    public void startProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                int lineCount = 0;
                //
                // Throw away the first 2 header lines before parsing
                //
                while ((inputLine = in.readLine()) != null) {
                    if (lineCount >= 13) {
                        result = result + inputLine;
                    }
                    lineCount++;

                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //

            //remove "null" which was being added to the start of the string
            result = result.substring(4);
            //colons were causing issues when parsing the lat and long so remove from string
            result = result.replaceAll("geo:", "");

            EarthquakeClass earthquake = null;
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));
                alist = new LinkedList<EarthquakeClass>();
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    //Found a start tag
                    if (eventType == XmlPullParser.START_TAG) {
                        //Check which tag we have
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            Log.e("MyTag", "Item Start Tag found");
                            earthquake = new EarthquakeClass();
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            String temp = xpp.nextText();
                            Log.e("MyTag", "Title is " + temp);
                            earthquake.setTitle(temp);
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            String temp = xpp.nextText();
                            Log.e("MyTag", "Description is " + temp);
                            earthquake.setDescription(temp);
                            //split string so location, depth and magnitude and can be assigned
                            String[] split = temp.split("; ");
                            earthquake.setLocation(split[1].substring(10));
                            String tempDepth = split[3];
                            tempDepth = tempDepth.substring(7);
                            tempDepth = tempDepth.replace(" km ", "");
                            earthquake.setDepth(Integer.parseInt(tempDepth));
                            String tempMagnitude = split[4];
                            tempMagnitude = tempMagnitude.substring(11);
                            earthquake.setMagnitude(Double.parseDouble(tempMagnitude));
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            String temp = xpp.nextText();
                            Log.e("MyTag", "Link is " + temp);
                            earthquake.setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            String temp = xpp.nextText();
                            Log.e("MyTag", "PubDate is " + temp);
                            earthquake.setPubDate(temp);

                            //change format of date
                            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.UK);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);

                            try {
                                Date date = inputFormat.parse(temp);
                                String formattedDate = outputFormat.format(date);
                                earthquake.setDate(formattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.e("MyTag", earthquake.getDate());

                        } else if (xpp.getName().equalsIgnoreCase("category")) {
                            String temp = xpp.nextText();
                            Log.e("MyTag", "Category is " + temp);
                            earthquake.setCategory(temp);
                        } else if (xpp.getName().equalsIgnoreCase("lat")) {
                            String temp = xpp.nextText();
                            Log.e("MyTag", "Latitude is " + temp);
                            earthquake.setLatitude(Double.valueOf(temp));
                        } else if (xpp.getName().equalsIgnoreCase("long")) {
                            String temp = xpp.nextText();
                            Log.e("MyTag", "Longitude is " + temp);
                            earthquake.setLongitude(Double.valueOf(temp));
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            Log.e("MyTag", "Earthquake is " + earthquake.toString());
                            alist.add(earthquake);
                        } else if (xpp.getName().equalsIgnoreCase("channel")) {
                            int size;
                            size = alist.size();
                            Log.e("MyTag", "earthquake collection size is " + size);
                        }
                    }

                    eventType = xpp.next();
                }
            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }

            Log.e("MyTag", "End document");


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !


            listView.post(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    //update list view with earthquake list
                    ArrayAdapter<EarthquakeClass> adapter = new ArrayAdapter<EarthquakeClass>(
                            MainActivity.this, android.R.layout.simple_list_item_1, alist);
                    listView.setAdapter(adapter);
                }
            });
        }

    }

    public void searchDate() {
        //Method for searching for earthquakes on a date

        //correct regex for date input
        String pattern = "^\\d{1,2}/\\d{1,2}/\\d{4}$";

        //create dialog box allowing user to enter date
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Search");

        // Inflate the layout for the dialog box
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search_date, null);
        builder.setView(dialogView);

        //take in user input
        EditText search = dialogView.findViewById(R.id.edit_text_date);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String query = search.getText().toString();
                String message;
                int magnitudeColour = R.color.black;
                SpannableString spannableString = null;
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
                //checks date is in correct format
                if (query.matches(pattern)) {

                    int counter = 0;
                    //compare dates to user input
                    for (int j = 0; j < alist.size(); j++) {
                        if (query.equals(alist.get(j).getDate())) {
                            message = alist.get(j).toString()  + "\n"  + "\n";

                            //check magnitude and set colour as appropriate
                            if (alist.get(j).getMagnitude() <= 1) {
                                magnitudeColour = getResources().getColor(R.color.green);
                            } else if (alist.get(j).getMagnitude() > 1 && alist.get(j).getMagnitude() <= 2) {
                                magnitudeColour = getResources().getColor(R.color.yellow);
                            } else if (alist.get(j).getMagnitude() > 2 && alist.get(j).getMagnitude() <= 3) {
                                magnitudeColour = getResources().getColor(R.color.orange);
                            } else if (alist.get(j).getMagnitude() > 3) {
                                magnitudeColour = getResources().getColor(R.color.red);
                            }

                            //set text colour to the Magnitude part of the view
                            spannableString = new SpannableString(message);
                            int start = spannableString.toString().indexOf("M ");
                            int end = start + 6;
                            spannableString.setSpan(new ForegroundColorSpan(magnitudeColour), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            stringBuilder.append(spannableString);

                            //message = message + alist.get(j).toString() + "\n\n";
                            counter++;
                        }
                    }
                    if (counter == 0) {
                        stringBuilder = SpannableStringBuilder.valueOf("There was no earthquakes on this day");
                    }
                } else {
                    stringBuilder = SpannableStringBuilder.valueOf("Please enter the date in the correct format 'DD/MM/YYYY'");
                }
                    //create dialog box for search query results
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("Search results")
                            .setMessage(stringBuilder)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog dialog1 = builder1.create();
                    dialog1.show();
                }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancel button click
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void specificSearch() {
        //Method for searching for an earthquake on a specific date and location

        //correct regex for date input
        String pattern = "^\\d{1,2}/\\d{1,2}/\\d{4}$";


        //create dialog box allowing user to enter date and location
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Search");

        // Inflate the layout for the dialog box
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search_specific, null);
        builder.setView(dialogView);

        //take in user input
        EditText date = dialogView.findViewById(R.id.edit_text_date);
        EditText location = dialogView.findViewById(R.id.edit_text_location);

        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int magnitudeColour = R.color.black;
                SpannableString spannableString = null;
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
                String dateQuery = date.getText().toString();
                String locationQuery = location.getText().toString().toUpperCase();
                String message = "";
                int counter = 0;

                //checks that entries haven't been left blank
                if (dateQuery.equals("") || locationQuery.equals("")) {
                    stringBuilder = SpannableStringBuilder.valueOf("Please make sure to enter both the date and a location");
                    counter++;
                    //checks date is in correct format
                } else if (!dateQuery.matches(pattern)) {
                    stringBuilder = SpannableStringBuilder.valueOf("Please enter the date in the correct format 'DD/MM/YYYY'");
                    counter++;
                } else {
                    for (int j = 0; j < alist.size(); j++) {
                        //compare dates and location to user input
                        if ((dateQuery.equals(alist.get(j).getDate()) && alist.get(j).getLocation().contains(locationQuery))) {
                            message = alist.get(j).toString()  + "\n"  + "\n";

                            //check magnitude and set colour as appropriate
                            if (alist.get(j).getMagnitude() <= 1) {
                                magnitudeColour = getResources().getColor(R.color.green);
                            } else if (alist.get(j).getMagnitude() > 1 && alist.get(j).getMagnitude() <= 2) {
                                magnitudeColour = getResources().getColor(R.color.yellow);
                            } else if (alist.get(j).getMagnitude() > 2 && alist.get(j).getMagnitude() <= 3) {
                                magnitudeColour = getResources().getColor(R.color.orange);
                            } else if (alist.get(j).getMagnitude() > 3) {
                                magnitudeColour = getResources().getColor(R.color.red);
                            }

                            //set text colour to the Magnitude part of the view
                            spannableString = new SpannableString(message);
                            int start = spannableString.toString().indexOf("M ");
                            int end = start + 6;
                            spannableString.setSpan(new ForegroundColorSpan(magnitudeColour), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            stringBuilder.append(spannableString);

                            //message = message + alist.get(j).toString() + "\n\n";
                            counter++;
                        }
                    }
                }
                if (counter == 0) {
                    stringBuilder = SpannableStringBuilder.valueOf("No earthquakes on this date at this location");
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Search results")
                        .setMessage(stringBuilder)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancel button click
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void findClosestNorthen() {
        //Method for finding the closest northen earthquake to glasgow

        //Latitude of GCU
        double glasgowLat = 55.86683265763648;
        //set temp to max possible northen lat
        double temp = 90;
        int index = 0;
        String message = "";
        for (int i = 0; i < alist.size(); i++) {
            //check if lat is north of glasgow but south of current temp
            if (alist.get(i).getLatitude() > glasgowLat && alist.get(i).getLatitude() < temp) {
                temp = alist.get(i).getLatitude();
                index = i;
            }
        }

        //find distance between result and glasgow
        double distance = distanceToGlasgow(alist.get(index).getLatitude(), alist.get(index).getLongitude());
        distance = Math.round(distance);
        //set message
        message = "The closest earthquake north of Glasgow was " + distance + " km away at " + alist.get(index).getLocation() + " on " + alist.get(index).getDate();

        //create dialog box to display result
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Closest Earthquake North of Glasgow")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void findClosestSouthern() {
        //Method for finding the closest southern earthquake to glasgow
        //Latitude of GCU
        double glasgowLat = 55.86683265763648;
        //set temp to max possible southern lat
        double temp = -90;
        int index = 0;
        String message = "";
        for (int i = 0; i < alist.size(); i++) {
            //check if lat is south of glasgow but north of current temp
            if (alist.get(i).getLatitude() < glasgowLat && alist.get(i).getLatitude() > temp) {
                temp = alist.get(i).getLatitude();
                index = i;
            }
        }

        //find distance between result and glasgow
        double distance = distanceToGlasgow(alist.get(index).getLatitude(), alist.get(index).getLongitude());
        distance = Math.round(distance);
        //set message
        message = "The closest earthquake south of Glasgow was " + distance + " km away at " + alist.get(index).getLocation() + " on " + alist.get(index).getDate();

        //create dialog box to display result
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Closest Earthquake South of Glasgow")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void findClosestEastern() {
        //Method for finding the closest eastern earthquake to glasgow
        //Longitude of GCU
        double glasgowLong = -4.2499387518176865;
        //set temp to max possible eastern long
        double temp = 180;
        int index = 0;
        String message = "";
        for (int i = 0; i < alist.size(); i++) {
            //check if long is east of glasgow but west of current temp
            if (alist.get(i).getLongitude() > glasgowLong && alist.get(i).getLongitude() < temp) {
                temp = alist.get(i).getLongitude();
                index = i;
            }
        }

        //find distance between result and glasgow
        double distance = distanceToGlasgow(alist.get(index).getLatitude(), alist.get(index).getLongitude());
        distance = Math.round(distance);
        //set message
        message = "The closest earthquake east of Glasgow was " + distance + " km away at " + alist.get(index).getLocation() + " on " + alist.get(index).getDate();

        //create dialog box to display result
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Closest Earthquake East of Glasgow")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void findClosestWestern() {
        //Method for finding the closest western earthquake to glasgow
        //Longitude of GCU
        double glasgowLong = -4.2499387518176865;
        //set temp to max possible weststern long
        double temp = -180;
        int index = 0;
        String message = "";
        for (int i = 0; i < alist.size(); i++) {
            //check if long is west of glasgow but east of current temp
            if (alist.get(i).getLongitude() < glasgowLong && alist.get(i).getLongitude() > temp) {
                temp = alist.get(i).getLongitude();
                index = i;
            }
        }

        //find distance between result and glasgow
        double distance = distanceToGlasgow(alist.get(index).getLatitude(), alist.get(index).getLongitude());
        distance = Math.round(distance);
        //set message
        message = "The closest earthquake west of Glasgow was " + distance + " km away at " + alist.get(index).getLocation() + " on " + alist.get(index).getDate();

        //create dialog box to display result
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Closest Earthquake West of Glasgow")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static double distanceToGlasgow(double lat1, double lon1) {
        //Method for calculating distance between Glasgow and parameters

        //set Glasgow lat and long
        double glasgowLat = 55.86683265763648;
        double glasgowLon = -4.2499387518176865;
        int R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(glasgowLat - lat1);
        double dLon = Math.toRadians(glasgowLon - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(glasgowLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km
        return distance;
    }

    public void findLargestEarthquake() {
        double temp = 0;
        String message = "";
        for (int i = 0; i < alist.size(); i++) {
            //check if magnitude is larger than temp and set new largest
            if (alist.get(i).getMagnitude() > temp) {
                temp = alist.get(i).getMagnitude();
                message = "The largest earthquake was one of Magnitude: " + temp + " at " + alist.get(i).getLocation() + "on " + alist.get(i).getDate() + ".\n \n";
                //if magnitude is equal to the largest add both to the message
            } else if (alist.get(i).getMagnitude() == temp) {
                message = message + "The largest earthquake was one of Magnitude: " + temp + " at " + alist.get(i).getLocation() + "on " + alist.get(i).getDate() + ".\n \n";
            }
        }
        //Create dialog box to show result
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Largest Earthquake")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void findDeepestEarthquake() {
        int temp = 0;
        String message = "";
        for (int i = 0; i < alist.size(); i++) {
            //check if depth is bigger than current temp
            if (alist.get(i).getDepth() > temp) {
                temp = alist.get(i).getDepth();
                message = "The deepest earthquake was one of Depth: " + temp + "km at " + alist.get(i).getLocation() + " on " + alist.get(i).getDate() + ". \n \n";
                //if depth is equal to the deepest add both to the message
            } else if (alist.get(i).getDepth() == temp) {
                message = message + "The deepest earthquake was one of Depth: " + temp + "km at " + alist.get(i).getLocation() + " on " + alist.get(i).getDate() + ". \n \n";
            }
        }
        //Create dialog box to show result
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Deepest Earthquake")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void finsShallowestEarthquake() {
        int temp = 100;
        String message = "";
        for (int i = 0; i < alist.size(); i++) {
            //check if depth is smaller than current temp
            if (alist.get(i).getDepth() < temp) {
                temp = alist.get(i).getDepth();
                message = "The shallowest earthquake was one of Depth: " + temp + "km at " + alist.get(i).getLocation() + " on " + alist.get(i).getDate() + ". \n \n";
                //if depth is equal to the shallowest add both to the message
            } else if (alist.get(i).getDepth() == temp) {
                message = message + "The shallowest earthquake was one of Depth: " + temp + "km at " + alist.get(i).getLocation() + " on " + alist.get(i).getDate() + ". \n \n";
            }
        }
        //Create dialog box to show result
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Shallowest Earthquake")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}