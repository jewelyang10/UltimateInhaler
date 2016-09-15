package monash.ultimateinhaler;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AsthmaNewsFragment extends Fragment {
    private View rootView;
    private ListView newsList;
    private NewsAdapter adapter;
    private ArrayList<News> partyNews;


    public static final String JSON_DOWNLOAD_LOCATION ="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20xml%20where%20url%3D'https%3A%2F%2Frss.sciencedaily.com%2Fhealth_medicine%2Fasthma.xml'&format=json&diagnostics=true&callback=";

    public AsthmaNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_asthma_news, container, false);
        // Inflate the layout for this fragment
        newsList = (ListView) rootView.findViewById(R.id.listView);
        partyNews = new ArrayList<News>();

        //Populate the asthma news
        new SetupNewsTask().execute(JSON_DOWNLOAD_LOCATION);
        newsList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> av, View v, int i, long l) {
                        // Get selected news
                        News r = (News) newsList.getAdapter().getItem(i);
                        //Open browser to view the selected news
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(r.getLink()));
                        startActivity(browserIntent);

                    }

                });
        return rootView;
    }


    /*
        Get the news from https://rss.sciencedaily.com/health_medicine/asthma.xml
     */

    private class SetupNewsTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        // Before we retrieve the JSON, let's activate a loading dialog
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        // Download JSON resource and return String representation
        @Override
        protected String doInBackground(String... urls) {
            try {
                // Setup HTTP client and request
                URL downloadUrl = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
                InputStream input = connection.getInputStream();
                // Process each line of response data
                String result1 = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder sb = new StringBuilder();
                while ((result1 = reader.readLine()) != null) {
                    sb.append(result1);
                }
                System.out.print(sb);
                //  return sb.toString();
                JSONObject newsContents = new JSONObject(sb.toString());
                JSONObject resultJ = newsContents.getJSONObject("query").getJSONObject("results").getJSONObject("rss").getJSONObject("channel");
                //System.out.println(resultJ.toString());
                JSONArray queries = resultJ.getJSONArray("item");

                for (int i = 0; i < queries.length(); i++) {
                    // Get the title, description, link and image url

                    String title = (String) ((JSONObject) queries.get(i)).get("title");

                    String descp = ((JSONObject) queries.get(i)).get("description").toString().trim().replace("<p>", "").replace("</p>", "");
                    String link = (String) ((JSONObject) queries.get(i)).get("link");

                    long id = i + 1;
                    News news = new News(id, title, link, descp);
                    //Add news into partyNews list
                    partyNews.add(news);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // Execute once our background job is complete
        @Override
        protected void onPostExecute(String result) {
            adapter = new NewsAdapter(getActivity().getBaseContext(), partyNews);
            // Associate adapter with ListView
            newsList.setAdapter(adapter);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

}
