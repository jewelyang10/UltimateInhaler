package monash.ultimateinhaler;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class PredictionFragment extends Fragment {
    View rootview;
    public PredictionFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_prediction, container, false);
        WebView predictBase = (WebView) rootview.findViewById(R.id.predictBaseContainer);
        WebSettings webSettings = predictBase.getSettings();
        webSettings.setJavaScriptEnabled(true);
        predictBase.setBackgroundColor(Color.TRANSPARENT);
        //predictBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        predictBase.setWebChromeClient(new WebChromeClient());
        predictBase.setWebViewClient(new WebViewClient());

        predictBase.loadUrl("file:///android_asset/predictionpage.html");
        return rootview;
    }

}
