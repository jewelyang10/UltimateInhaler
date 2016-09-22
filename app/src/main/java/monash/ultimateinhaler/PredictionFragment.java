package monash.ultimateinhaler;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;


public class PredictionFragment extends Fragment {
    View rootview;
    HttpsURLConnection connection = null;

    public PredictionFragment() {
        // Required empty public constructor
    }

    //utility function to get string
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public static int sigmoidConvert(ArrayList<Integer> base) {
        int range = Collections.max(base) - Collections.min(base);

        for (int singleItem : base) {
            double result = (singleItem - Collections.min(base)) / range;
        }
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        Bundle rawData = this.getArguments();
        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("PREDICTION", null);
        //set up basic layout
        rootview = inflater.inflate(R.layout.fragment_prediction, container, false);
        WebView predictBase = (WebView) rootview.findViewById(R.id.predictBaseContainer);

        //get GPS location


        //get Weather


        //interactive class between java and javascript
        class WebAppInterface {
            Context mContext;
            Bundle RawVars;

            String LTP = "[12, 19, 25, 5]";
            String pollen =  "[90, 70, 50, 25, 30, 40, 80]";
            String OD =  "[25, 5, 70]";

            /** Instantiate the interface and set the context */
            WebAppInterface(Context c, Bundle realData) {
                mContext = c;
                RawVars = realData;

                if (RawVars != null)
                {
                    LTP = RawVars.getString("LTP" );
                    pollen =RawVars.getString("pollen");
                    OD = RawVars.getString("OD");

                }else
                {
                    Toast.makeText(getContext(), "Prediction report is still under development...", Toast.LENGTH_LONG).show();
                }
            }

            //further development required after the buddle issue
            @JavascriptInterface
            public String GetLTP()
            {
                return this.LTP;
            }
            @JavascriptInterface
            public String GetPollen()
            {
                return this.pollen;
            }
            @JavascriptInterface
            public String GetOD()
            {
                return this.OD;
            }
            @JavascriptInterface
            public void clickDate(String date)
            {
                Toast.makeText( getContext() , "date: "+date+" are clicked and i need a buddle from Jewel to continue!" , Toast.LENGTH_LONG).show();
            }
        }

        WebSettings webSettings = predictBase.getSettings();
        webSettings.setJavaScriptEnabled(true);
        predictBase.setBackgroundColor(Color.TRANSPARENT);
        //predictBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        predictBase.setWebChromeClient(new WebChromeClient());
        predictBase.setWebViewClient(new WebViewClient());

        predictBase.addJavascriptInterface(new WebAppInterface(rootview.getContext(),rawData), "Android");
        predictBase.loadUrl("file:///android_asset/predictionpage.html");
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_back_diary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit_diary) {
            // Eliminar todos los leads
//            mLeadsAdapter.clear();

            MoreFragment fragment6 = new MoreFragment();
            FragmentTransaction fragmentTransaction6 =
                    getFragmentManager().beginTransaction();
            fragmentTransaction6.replace(R.id.fragment_containerStart, fragment6);
            fragmentTransaction6.addToBackStack(null);
            fragmentTransaction6.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}