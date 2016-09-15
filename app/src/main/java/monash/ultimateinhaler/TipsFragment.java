package monash.ultimateinhaler;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TipsFragment extends Fragment {
    View rootView;
    Typeface ty1;

    public TipsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tips, container, false);
        //Set the tool bar
        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("Tips",null);
//        //Configure the typeface
        ty1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/PTSansWide.ttf");
        TextView textView1 = (TextView) rootView.findViewById(R.id.textView_tp1);
        TextView textView2 = (TextView) rootView.findViewById(R.id.textView_tp2);
        TextView textView3 = (TextView) rootView.findViewById(R.id.textView_tp3);
        TextView textView4 = (TextView) rootView.findViewById(R.id.textView_tp4);
        TextView textView5 = (TextView) rootView.findViewById(R.id.textView_tp5);
        TextView textView6 = (TextView) rootView.findViewById(R.id.textView_tp6);
        TextView textView7 = (TextView) rootView.findViewById(R.id.textView_tp7);

//        //Set the typeface of text

        textView1.setTypeface(ty1);
        textView2.setTypeface(ty1);
        textView3.setTypeface(ty1);
        textView4.setTypeface(ty1);
        textView5.setTypeface(ty1);
        textView6.setTypeface(ty1);
        textView7.setTypeface(ty1);


        // Inflate the layout for this fragment

        return rootView;
    }

}
