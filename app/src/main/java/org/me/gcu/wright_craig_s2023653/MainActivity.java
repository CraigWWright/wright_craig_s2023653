
/*  Starter project for Mobile Platform Development in Semester B Session 2018/2019
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Craig Wright
// Student ID           s2023653
// Programme of Study   Computing
// Test
//

// Update the package name to include your Student Identifier
package org.me.gcu.wright_craig_s2023653;

//import android.support.v7.app.AppCompatActivity;
//import android.support.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
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

//import gcu.mpd.bgsdatastarter.R;

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener
{
    //rawDataDisplay no longer used
    //private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    LinkedList <EarthquakeClass> alist;
    private ListView listView;
    private Button searchDateButton;
    private Button specificsearchButton;
    private Button closestNorthenButton;
    private Button closestSouthernButton;
    private Button closestEasternButton;
    private Button closestWesternButton;
    private Button largestButton;
    private Button deepestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        //rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
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
    }

    public void onItemClick(AdapterView<?> parenr, View view, int position, long id) {
        for (int i = 0; i < alist.size(); i++) {
            if (position == i) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(alist.get(i).detailedView())
                        .setTitle("Earthquake Details")
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
    }

    public void onClick(View aview)
    {
        if (aview==startButton) {
            startProgress();
        }
        if (aview==searchDateButton) {
            searchDate();
        }
        if (aview==specificsearchButton) {
            specificSearch();
            Log.e("TEST", "Method called");
        }
        if (aview==closestNorthenButton) {
            findClosestNorthen();
        }
        if (aview==closestSouthernButton) {
            findClosestSouthern();
        }
        if (aview==closestEasternButton) {
            findClosestEastern();
        }
        if (aview==closestWesternButton) {
            findClosestWestern();
        }
        if (aview==largestButton) {
            findLargestEarthquake();
        }
        if (aview==deepestButton) {
            findDeepestEarthquake();
        }
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                int lineCount =0;
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                while ((inputLine = in.readLine()) != null)
                {
                    if (lineCount>=13) {
                        result = result + inputLine;
                    }
                    lineCount++;

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //

            result = result.substring(4);
            result = result.replaceAll("geo:", "");

            EarthquakeClass earthquake = null;
            //LinkedList <EarthquakeClass> alist = null;
            try
            {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( new StringReader( result ));
                alist = new LinkedList<EarthquakeClass>();
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT)
                {

                    //Found a start tag
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        /*if (xpp.getName().equalsIgnoreCase("channel"))
                        {
                            //alist  = new LinkedList<WidgetClass>();
                            alist = new LinkedList<EarthquakeClass>();
                        }
                        */

                        //Check which tag we have
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            Log.e("MyTag", "Item Start Tag found");
                            earthquake = new EarthquakeClass();
                        }
                        else
                            if (xpp.getName().equalsIgnoreCase("title"))
                            {
                                String temp = xpp.nextText();
                                Log.e("MyTag", "Title is " + temp);
                                earthquake.setTitle(temp);
                            }
                            else
                                if (xpp.getName().equalsIgnoreCase("description"))
                                {
                                    String temp = xpp.nextText();
                                    Log.e("MyTag", "Description is " + temp);
                                    earthquake.setDescription(temp);
                                    String[] split = temp.split("; ");
                                    earthquake.setLocation(split[1].substring(10));
                                    String tempDepth = split[3];
                                    tempDepth = tempDepth.substring(7);
                                    tempDepth = tempDepth.replace(" km ", "");
                                    earthquake.setDepth(Integer.parseInt(tempDepth));
                                    String tempMagnitude = split[4];
                                    tempMagnitude = tempMagnitude.substring(11);
                                    earthquake.setMagnitude(Double.parseDouble(tempMagnitude));
                                }
                                else
                                    if (xpp.getName().equalsIgnoreCase("link"))
                                    {
                                        String temp = xpp.nextText();
                                        Log.e("MyTag", "Link is " + temp);
                                        earthquake.setLink(temp);
                                    }
                                    else
                                        if (xpp.getName().equalsIgnoreCase("pubDate"))
                                        {
                                            String temp = xpp.nextText();
                                            Log.e("MyTag", "PubDate is " + temp);
                                            earthquake.setPubDate(temp);

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

                                        }
                                        else
                                            if (xpp.getName().equalsIgnoreCase("category"))
                                            {
                                                String temp = xpp.nextText();
                                                Log.e("MyTag", "Category is " + temp);
                                                earthquake.setCategory(temp);
                                            }
                                            else
                                                if (xpp.getName().equalsIgnoreCase("lat"))
                                                {
                                                    String temp = xpp.nextText();
                                                    Log.e("MyTag", "Latitude is " + temp);
                                                    earthquake.setLatitude(Double.valueOf(temp));
                                                }
                                                else
                                                    if (xpp.getName().equalsIgnoreCase("long"))
                                                    {
                                                        String temp = xpp.nextText();
                                                        Log.e("MyTag", "Longitude is " + temp);
                                                        earthquake.setLongitude(Double.valueOf(temp));
                                                    }
                    }
                    else
                        if(eventType == XmlPullParser.END_TAG)
                        {
                            if (xpp.getName().equalsIgnoreCase("item"))
                            {
                                Log.e("MyTag", "Earthquake is " + earthquake.toString());
                                alist.add(earthquake);
                            }
                            else
                                if (xpp.getName().equalsIgnoreCase("channel"))
                                {
                                    int size;
                                    size = alist.size();
                                    Log.e("MyTag", "earthquake collection size is " + size);
                                }
                        }

                        eventType = xpp.next();
                }
            } catch (XmlPullParserException ae1)
            {
                Log.e("MyTag","Parsing error" + ae1.toString());
            }
            catch (IOException ae1)
            {
                Log.e("MyTag","IO error during parsing");
            }

            Log.e("MyTag","End document");


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !


            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    //rawDataDisplay.setText(result);

                    ArrayAdapter<EarthquakeClass> adapter = new ArrayAdapter<EarthquakeClass>(
                    MainActivity.this, android.R.layout.simple_list_item_1, alist);
                    listView.setAdapter(adapter);
                }
            });
        }

    }

    public void searchDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Search");

        // Inflate the layout for the dialog box
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search_date, null);
        builder.setView(dialogView);

        EditText search = dialogView.findViewById(R.id.edit_text_date);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String query = search.getText().toString();
                String output ="";
                int counter =  0;
                for (int j=0; j < alist.size(); j++) {
                    if (query.equals(alist.get(j).getDate())) {
                        output = output + alist.get(j).toString() + " \n \n";
                        counter++;
                    }
                }
                if (counter==0) {
                    output = "There was no earthquakes on this day";
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Search results")
                        .setMessage(output)
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
        Log.e("TEST", "In method");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Search");

        // Inflate the layout for the dialog box
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search_specific, null);
        builder.setView(dialogView);

        EditText date = dialogView.findViewById(R.id.edit_text_date);
        EditText location = dialogView.findViewById(R.id.edit_text_location);

        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String dateQuery = date.getText().toString();
                String locationQuery = location.getText().toString();
                //locationQuery = locationQuery.toUpperCase();
                String message = "";
                int counter = 0;
                for (int j=0; j < alist.size(); j++) {
                    if ((dateQuery.equals(alist.get(j).getDate()) && alist.get(j).getLocation().contains(locationQuery))){
                        message = message + " " + alist.get(j).toString() + " \n \n";
                        counter++;
                    }
                    if (counter==0) {
                        message = "No results found";
                    }
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Search results")
                        .setMessage(message)
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
        //Latitude of GCU
        double glasgowLat = 55.86683265763648;
        double temp = 90;
        String message = "";
        for (int i=0; i < alist.size(); i++) {
            if (alist.get(i).getLatitude() > glasgowLat && alist.get(i).getLatitude() < temp) {
                message = "The closest earthquake north of Glasgow was at " + alist.get(i).getLocation();
                temp = alist.get(i).getLatitude();
            }
        }
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
        //Latitude of GCU
        double glasgowLat = 55.86683265763648;
        double temp = -90;
        String message = "";
        for (int i=0; i < alist.size(); i++) {
            if (alist.get(i).getLatitude() < glasgowLat && alist.get(i).getLatitude() > temp) {
                message = "The closest earthquake south of Glasgow was at " + alist.get(i).getLocation();
                temp = alist.get(i).getLatitude();
            }
        }
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
        //Longitude of GCU
        double glasgowLong = -4.2499387518176865;
        double temp = 20;
        String message = "";
        for (int i=0; i < alist.size(); i++) {
            if (alist.get(i).getLongitude() > glasgowLong && alist.get(i).getLongitude() < temp) {
                message = "The closest earthquake east of Glasgow was at " + alist.get(i).getLocation();
                temp = alist.get(i).getLongitude();
            }
        }
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
        //Longitude of GCU
        double glasgowLong = -4.2499387518176865;
        double temp = -20;
        String message = "";
        for (int i=0; i < alist.size(); i++) {
            if (alist.get(i).getLongitude() < glasgowLong && alist.get(i).getLongitude() > temp) {
                message = "The closest earthquake east of Glasgow was at " + alist.get(i).getLocation();
                temp = alist.get(i).getLongitude();
            }
        }
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

    public void findLargestEarthquake() {
        double temp = 0;
        String message = "";
        for (int i=0; i < alist.size(); i++) {
            if (alist.get(i).getMagnitude() > temp) {
                temp = alist.get(i).getMagnitude();
                message = "The largest earthquake was one of Magnitude: " + temp + " at " + message + alist.get(i).getLocation() + "on " + alist.get(i).getDate() + ".\n \n";
            }
            else if (alist.get(i).getMagnitude() == temp) {
                message = message + "The largest earthquake was one of Magnitude: " + temp + " at " + message + alist.get(i).getLocation() + "on " + alist.get(i).getDate() + ".\n \n";
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Largest Earthquake")
                .setMessage("The largest earthquake was one of Magnitude: " + temp + " at " + message)
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
        for (int i=0; i < alist.size(); i++) {
            if (alist.get(i).getDepth() > temp) {
                temp = alist.get(i).getDepth();
                message = "The largest earthquake was one of Depth: " + temp + "km at " + alist.get(i).getLocation() + " on " + alist.get(i).getDate() + ". \n \n";
            }
            else if (alist.get(i).getDepth() == temp) {
                message = message + "The largest earthquake was one of Depth: " + temp + "km at " + alist.get(i).getLocation() + " on " + alist.get(i).getDate() + ". \n \n";
            }
        }
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

}