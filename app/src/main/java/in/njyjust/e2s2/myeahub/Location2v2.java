package in.njyjust.e2s2.myeahub;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
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

/**
 * Created by justin on 22/4/15.
 */
// Instances of this class are fragments representing a single
// object in our collection.

public class Location2v2 extends Fragment {

    String[] urls = new String[4];
    String data, tpmv, hmmv, dumv;
    String username, password;

    public double tpReading, hmReading, duReading,
            tpmReading, tpvReading, hmmReading, hmvReading, dumReading, duvReading;


    private TextView dustUnits;
    private TextView tpTV, hmTV, duTV, tpmTV, tpvTV, hmmTV, hmvTV, dumTV, duvTV;
    private View connectStatus;
    private ProgressBar progressBar;

    public Location2v2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View v = inflater.inflate(R.layout.location2, container, false);

        dustUnits = (TextView) v.findViewById(R.id.dustUnits); // RETRIEVE YOUR TEXT VIEW
        dustUnits.setText(Html.fromHtml("mg m<sup><small>-3</small></sup>"));

        username = //getArguments().getString(username, "jeffrey");
                "jeffrey";
        password = //getArguments().getString(password, "mypass");
                "mypass";

        data = "http://10.10.0.1/index.php?username="
                + username
                + "&password="
                + password
                + "&act=query_lrow&dbc=E2S2&tbl=air&sn=408BC7C9";
        urls[0] = data;

        tpmv = "http://10.10.0.1/index.php?username="
                + username
                + "&password="
                + password
                + "&act=query_lmv&dbc=E2S2&col=tp&sn=408BC7C9";
        urls[1] = tpmv;

        hmmv = "http://10.10.0.1/index.php?username="
                + username
                + "&password="
                + password
                + "&act=query_lmv&dbc=E2S2&col=hm&sn=408BC7C9";
        urls[2] = hmmv;

        dumv = "http://10.10.0.1/index.php?username="
                + username
                + "&password="
                + password
                + "&act=query_lmv&dbc=E2S2&col=du&sn=408BC7C9";
        urls[3] = dumv;

        tpTV = (TextView) v.findViewById(R.id.TemperatureView);
        hmTV = (TextView) v.findViewById(R.id.HumidityView);
        duTV = (TextView) v.findViewById(R.id.DustView);
        tpmTV = (TextView) v.findViewById(R.id.temperature_mean_value);
        tpvTV = (TextView) v.findViewById(R.id.temperature_variance_value);
        hmmTV = (TextView) v.findViewById(R.id.humidity_mean_value);
        hmvTV = (TextView) v.findViewById(R.id.humidity_variance_value);
        dumTV = (TextView) v.findViewById(R.id.dust_mean_value);
        duvTV = (TextView) v.findViewById(R.id.dust_variance_value);
        connectStatus = (View) v.findViewById(R.id.connect_status);

        Thread dataThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);

                        String result;
                        for (String url : urls) {
                            int i = 0;
                            if (url.contains("&col=tp")) {
                                result = "tp";
                            } else if (url.contains("&col=hm")) {
                                result = "hm";
                            } else if (url.contains("&col=du")) {
                                result = "du";
                            } else {
                                result = "data";
                            }

                            DefaultHttpClient client = new DefaultHttpClient();
                            HttpGet httpGet = new HttpGet(url);
                            try {
                                HttpResponse execute = client.execute(httpGet);
                                InputStream content = execute.getEntity().getContent();

                                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                                String s = "";

                                while ((s = buffer.readLine()) != null) {
                                    result += s;
                                }

                                if (result.startsWith("data")) {
                                    i = 0;
                                    result = result.substring(4, result.length());
                                } else if (result.startsWith("tp")) {
                                    i = 1;
                                    result = result.substring(2, result.length());
                                } else if (result.startsWith("hm")) {
                                    i = 2;
                                    result = result.substring(2, result.length());
                                } else if (result.startsWith("du")) {
                                    i = 3;
                                    result = result.substring(2, result.length());
                                }

                                JSONObject jObject = new JSONObject(result);

                                if (i == 0) {
                                    tpReading = jObject.getDouble("tp");
                                    hmReading = jObject.getDouble("hm");
                                    duReading = jObject.getDouble("du");
                                } else if (i == 1) {
                                    tpmReading = jObject.getDouble("mean");
                                    tpvReading = jObject.getDouble("var");
                                } else if (i == 2) {
                                    hmmReading = jObject.getDouble("mean");
                                    hmvReading = jObject.getDouble("var");
                                } else if (i == 3) {
                                    dumReading = jObject.getDouble("mean");
                                    duvReading = jObject.getDouble("var");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (isAdded()) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    if (tpReading == 0.0 || tpmReading == .00) {
                                        connectStatus.setBackgroundColor(Color.RED);
                                    } else {
                                        connectStatus.setBackgroundColor(Color.GREEN);
                                    }
                                    DecimalFormat df2 = new DecimalFormat("#.00");
                                    DecimalFormat dfe = new DecimalFormat("#.##E0");

                                    tpTV.setText(Double.toString(tpReading));
                                    if (tpReading < 23 && tpReading != 0.0) {
                                        tpTV.setTextColor(Color.BLUE);
                                    } else if (tpReading >= 23 && tpReading <= 25) {
                                        tpTV.setTextColor(Color.BLACK);
                                    } else if (tpReading > 25) {
                                        tpTV.setTextColor(Color.RED);
                                    }
                                    hmTV.setText(Double.toString(hmReading));
                                    if (hmReading < 70 && hmReading != 0.0) {
                                        hmTV.setTextColor(Color.BLUE);
                                    } else if (hmReading >= 70) {
                                        hmTV.setTextColor(Color.BLACK);
                                    }
                                    duTV.setText(Double.toString(duReading));
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
                                }
                            });
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        dataThread.start();

        return v;
    }

}