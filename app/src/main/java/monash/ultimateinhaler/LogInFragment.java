package monash.ultimateinhaler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment  {
    View rootView;
    TextView detail;
    private static final String TAG = "SignInActivity";
    private ListView listView;

    public LogInFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        this.listView = (ListView) rootView.findViewById(R.id.listView);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.getContext());
        databaseAccess.open();
        List<String> quotes = databaseAccess.getQuotes("-37","145");
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, quotes);
        this.listView.setAdapter(adapter);
        return rootView;
    }




}
