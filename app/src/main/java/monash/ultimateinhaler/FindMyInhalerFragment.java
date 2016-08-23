package monash.ultimateinhaler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindMyInhalerFragment extends Fragment {
    View rootView;
    WebView webView;

    public FindMyInhalerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_find_my_inhaler, container, false);
//        webView = (WebView) rootView.findViewById(R.id.webView_inhaler);
////        webView.getSettings().setJavaScriptEnabled(true);
//
////        //String customHtml = new String(Files.readAllBytes(Paths.get("./index.html")));
////        webView.loadData(customHtml, "text/html", "UTF-8");
//
//        webView.getSettings().setJavaScriptEnabled(true);
//        //webView.loadUrl("http://www.google.com");
//
//        File file = new File("../index.html");
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
//        StringBuilder total = new StringBuilder();
//        String line;
//
//        try {
//            while ((line = r.readLine()) != null) {
//                total.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String customHtml = total.toString();
//
//        Log.v("File", customHtml);
//        //webView.loadData(customHtml, "text/html", "UTF-8");
//

        return rootView;
    }

}
