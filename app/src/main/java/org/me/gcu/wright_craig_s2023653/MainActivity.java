
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

//import gcu.mpd.bgsdatastarter.R;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    LinkedList <EarthquakeClass> alist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LinkedList<EarthquakeClass> alist = null;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // More Code goes here
    }

    public void onClick(View aview)
    {
        startProgress();
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
                        //Log.e("TEST", inputLine);
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
                                                    earthquake.setLatitude(temp);
                                                }
                                                else
                                                    if (xpp.getName().equalsIgnoreCase("long"))
                                                    {
                                                        String temp = xpp.nextText();
                                                        Log.e("MyTag", "Longitude is " + temp);
                                                        earthquake.setLongitude(temp);
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
                    rawDataDisplay.setText("");
                    for (int i=0; i < alist.size(); i++) {
                        rawDataDisplay.append("\n" + alist.get(i).toString() + "\n");
                    }
                }
            });
        }

    }

}