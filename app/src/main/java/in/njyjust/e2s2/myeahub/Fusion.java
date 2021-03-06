package in.njyjust.e2s2.myeahub;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by justin on 3/5/15.
 */
public class Fusion extends Fragment {

        String data, tpmv, hmmv, dumv;
        String username, password;

        public double tpReading, tpReading1, tpReading2,
                hmReading, hmReading1, hmReading2,
                duReading, duReading1, duReading2,
                tpmReading, tpvReading, hmmReading, hmvReading, dumReading, duvReading;

        private TextView dustUnits;
        private TextView tpTV, hmTV, duTV, tpmTV, tpvTV, hmmTV, hmvTV, dumTV, duvTV;
        private View connectStatus;
        private ProgressBar progressBar;

        public Fusion() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fusion, container, false);

            //Bundle bundle = this.getArguments();

            dustUnits = (TextView) v.findViewById(R.id.dustUnits); // RETRIEVE YOUR TEXT VIEW
            dustUnits.setText(Html.fromHtml("mg m<sup><small>-3</small></sup>"));

            username = //bundle.getString(username, "jeffrey");
                    "jeffrey";
            password = //bundle.getString(password, "mypass");
                    "mypass";

            data = "http://10.10.0.1/index.php?username="
                    + username
                    + "&password="
                    + password
                    + "&act=query_lrow&dbc=E2S2&tbl=air&sn=";

            tpmv = "http://10.10.0.1/index.php?username="
                    + username
                    + "&password="
                    + password
                    + "&act=query_lmv&dbc=E2S2&col=tp&sn=aggregate";

            hmmv = "http://10.10.0.1/index.php?username="
                    + username
                    + "&password="
                    + password
                    + "&act=query_lmv&dbc=E2S2&col=hm&sn=aggregate";

            dumv = "http://10.10.0.1/index.php?username="
                    + username
                    + "&password="
                    + password
                    + "&act=query_lmv&dbc=E2S2&col=du&sn=aggregate";

            tpTV = (TextView) v.findViewById(R.id.TemperatureView);
            hmTV = (TextView) v.findViewById(R.id.HumidityView);
            duTV = (TextView) v.findViewById(R.id.DustView);
            tpmTV = (TextView) v.findViewById(R.id.temperature_mean_value);
            tpvTV = (TextView) v.findViewById(R.id.temperature_variance_value);
            hmmTV = (TextView) v.findViewById(R.id.humidity_mean_value);
            hmvTV = (TextView) v.findViewById(R.id.humidity_variance_value);
            dumTV = (TextView) v.findViewById(R.id.dust_mean_value);
            duvTV = (TextView) v.findViewById(R.id.dust_variance_value);
            connectStatus = v.findViewById(R.id.connect_status);

            connect(data);
            connect(tpmv);
            connect(hmmv);
            connect(dumv);

            return v;
        }

        public void connect(final String theURL) {
            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                getDataTask performBackgroundTask = new getDataTask();

                                if (theURL.contains("query_lrow")) {

                                    performBackgroundTask.execute(theURL);

                                    tpReading = (tpReading1+tpReading2)/2;
                                    hmReading = (hmReading1+hmReading2)/2;
                                    duReading = (duReading1+duReading2)/2;

                                }

                                else {performBackgroundTask.execute(theURL);}

                                if (tpReading == 0.0 || tpmReading == .00)
                                {
                                    connectStatus.setBackgroundColor(Color.RED);
                                } else {
                                    connectStatus.setBackgroundColor(Color.GREEN);
                                }

                                DecimalFormat df2 = new DecimalFormat("#.00");
                                DecimalFormat dfe = new DecimalFormat("#.##E0");

                                tpTV.setText(df2.format(tpReading));
                                if (tpReading < 23 && tpReading != 0.0) {
                                    tpTV.setTextColor(Color.BLUE);
                                } else if (tpReading >= 23 && tpReading <= 25) {
                                    tpTV.setTextColor(Color.BLACK);
                                } else if (tpReading > 25) {
                                    tpTV.setTextColor(Color.RED);
                                }
                                hmTV.setText(df2.format(hmReading));
                                if (hmReading < 70 && hmReading != 0.0) {
                                    hmTV.setTextColor(Color.BLUE);
                                } else if (hmReading >= 70) {
                                    hmTV.setTextColor(Color.BLACK);
                                }
                                duTV.setText(df2.format(duReading));
                                if (duReading < 0.55 && duReading != 0.0) {
                                    duTV.setTextColor(Color.GREEN);
                                } else if (duReading >= 0.55) {
                                    duTV.setTextColor(Color.RED);
                                }

                                tpmTV.setText(df2.format(tpmReading));
                                tpvTV.setText(df2.format(tpvReading));
                                hmmTV.setText(df2.format(hmmReading));
                                hmvTV.setText(df2.format(hmvReading));
                                dumTV.setText(df2.format(dumReading));
                                duvTV.setText(dfe.format(duvReading));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };
            timer.schedule(doTask, 0, 3000);
        }

        private class getDataTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                String response = "";
                String data1 = "";
                String data2 = "";

                for (String url : urls) {
                    if (url.contains("&col=tp")) {
                        response = "tp";
                    } else if (url.contains("&col=hm")) {
                        response = "hm";
                    } else if (url.contains("&col=du")) {
                        response = "du";
                    } else {
                        response = "data";
                        data1 = url + "408BC7D6";
                        data2 = url + "408BC7C9";
                    }

                    DefaultHttpClient client = new DefaultHttpClient();
                    if (data1.contains("408BC7D6")) {
                        HttpGet httpGet1 = new HttpGet(data1);
                        HttpGet httpGet2 = new HttpGet(data2);
                        try {
                            HttpResponse execute = client.execute(httpGet1);
                            InputStream content = execute.getEntity().getContent();

                            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                            String s = "";

                            while ((s = buffer.readLine()) != null) {
                                response += s;
                            }

                            response += "/";

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            HttpResponse execute = client.execute(httpGet2);
                            InputStream content = execute.getEntity().getContent();

                            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                            String s = "";

                            while ((s = buffer.readLine()) != null) {
                                response += s;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    else {
                        HttpGet httpGet = new HttpGet(url);
                        try {
                            HttpResponse execute = client.execute(httpGet);
                            InputStream content = execute.getEntity().getContent();

                            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                            String s = "";

                            while ((s = buffer.readLine()) != null) {
                                response += s;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return response;
            }

            @Override
            protected void onPostExecute(String result) {

                int i=0;

                try {

                    if (result.startsWith("data"))
                    {
                        i=0;
                        result = result.substring(4,result.length());
                    }

                    else if (result.startsWith("tp"))
                    {
                        i=1;
                        result = result.substring(2,result.length());
                    }

                    else if (result.startsWith("hm"))
                    {
                        i=2;
                        result = result.substring(2,result.length());
                    }
                    else if (result.startsWith("du"))
                    {
                        i=3;
                        result = result.substring(2,result.length());
                    }

                    if (i==0) {
                        String result1 = result.substring(0,result.indexOf("/"));
                        String result2 = result.substring(result.indexOf("/")+1,result.length());

                        JSONObject jObject1 = new JSONObject(result1);
                        JSONObject jObject2 = new JSONObject(result2);

                        tpReading1 = jObject1.getDouble("tp");
                        hmReading1 = jObject1.getDouble("hm");
                        duReading1 = jObject1.getDouble("du");
                        tpReading2 = jObject2.getDouble("tp");
                        hmReading2 = jObject2.getDouble("hm");
                        duReading2 = jObject2.getDouble("du");
                    }

                    else if (i==1) {
                        JSONObject jObject = new JSONObject(result);

                        tpmReading = jObject.getDouble("mean");
                        tpvReading = jObject.getDouble("var");
                    }

                    else if (i==2) {
                        JSONObject jObject = new JSONObject(result);

                        hmmReading = jObject.getDouble("mean");
                        hmvReading = jObject.getDouble("var");
                    }
                    else if (i==3) {
                        JSONObject jObject = new JSONObject(result);

                        dumReading = jObject.getDouble("mean");
                        duvReading = jObject.getDouble("var");
                    }
                } catch (Exception e) {e.printStackTrace();}
            }
        }
}
