package monash.ultimateinhaler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


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
        //Configure tabHost
        final TabHost tabHost = (TabHost) rootview.findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Diary");
        tabSpec.setContent(R.id.diary);
        tabSpec.setIndicator("Diary");

        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Calendar/Report");
        tabSpec.setContent(R.id.calendar);
        tabSpec.setIndicator("Calendar/Report");
        tabHost.addTab(tabSpec);
//        //When click the second tab, execute startTimer function
//        tabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // re enforce selecting targeted tab to re apply main listener to default behaviour
//                tabHost.setCurrentTab(1);
//                startTimer(rootview);
//                // re enforce selecting targeted tab to re apply main listener to default behaviour
//                tabHost.setCurrentTab(1);
//            }
//        });

        return rootview;
    }

}
