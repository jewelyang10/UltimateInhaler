package monash.ultimateinhaler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PredictionFragment extends Fragment {
    View rootview;
    TextView weather;

    public PredictionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_prediction, container, false);
        weather = (TextView) rootview.findViewById(R.id.textView_prediction);

        //Extract the bundle from the intent to use variables
        Bundle bundle = getActivity().getIntent().getExtras();
        //Extract each value from the bundle for usage
        String temperature = bundle.getString("Temperature");
        String wind = bundle.getString("Wind");
        String sunrise = bundle.getString("Sunrise");
        String sunset = bundle.getString("Sunset");

        weather.setText(temperature + " \n" + wind + "\n " + sunrise + "\n" + sunset);



        return rootview;
    }

}
