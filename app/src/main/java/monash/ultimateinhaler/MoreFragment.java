package monash.ultimateinhaler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {
    View rootView;
    ImageButton prediction, faq;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_more, container, false);

        prediction = (ImageButton) rootView.findViewById(R.id.prediction_fragment);
        faq = (ImageButton) rootView.findViewById(R.id.faq_fragment);

        prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        title = "Prediction";
                        PredictionFragment fragment5 = new PredictionFragment();
                        FragmentTransaction fragmentTransaction5 =
                                getFragmentManager().beginTransaction();
                        fragmentTransaction5.replace(R.id.fragment_containerStart, fragment5);
                        fragmentTransaction5.addToBackStack(null);
                        fragmentTransaction5.commit();
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                title = "About";
                        LeadsFragment fragment6 = new LeadsFragment();
                        FragmentTransaction fragmentTransaction6 =
                                getFragmentManager().beginTransaction();
                        fragmentTransaction6.replace(R.id.fragment_containerStart, fragment6);
                        fragmentTransaction6.addToBackStack(null);
                        fragmentTransaction6.commit();

            }
        });

        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("More", null);
//        startActivity.setToolBarColor();


        return rootView;
    }

}
