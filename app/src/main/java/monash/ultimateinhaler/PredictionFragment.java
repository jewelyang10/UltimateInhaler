package monash.ultimateinhaler;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class PredictionFragment extends Fragment {
    View rootview;
    public PredictionFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //this.getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Bundle rawData = this.getArguments();



        rootview = inflater.inflate(R.layout.fragment_prediction, container, false);
        WebView predictBase = (WebView) rootview.findViewById(R.id.predictBaseContainer);
        WebSettings webSettings = predictBase.getSettings();
        webSettings.setJavaScriptEnabled(true);
        predictBase.setBackgroundColor(Color.TRANSPARENT);
        //predictBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        predictBase.setWebChromeClient(new WebChromeClient());
        predictBase.setWebViewClient(new WebViewClient());


        class WebAppInterface {
            Context mContext;
            Bundle RawVars;

            String LTP = "[12, 19, 25, 5, 2]";
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
                    Toast.makeText(getContext(), "Prediction report is in offline mode due to network issue", Toast.LENGTH_LONG).show();
                }
            }

            @JavascriptInterface
            public String GetLTP() {
                return this.LTP;
            }
            @JavascriptInterface
            public String GetPollen(){
                return this.pollen;
            }
            @JavascriptInterface
            public String GetOD(){
                return this.OD;
            }
        }

        predictBase.addJavascriptInterface(new WebAppInterface(rootview.getContext(),rawData), "Android");
        predictBase.loadUrl("file:///android_asset/predictionpage.html");
        return rootview;
    }

}
