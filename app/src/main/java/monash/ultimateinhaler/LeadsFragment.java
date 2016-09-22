package monash.ultimateinhaler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeadsFragment extends Fragment {
    private ListView mLeadsList;
    private LeadsAdapter mLeadsAdapter;

    public LeadsFragment() {
        // Required empty public constructor
    }

    public static LeadsFragment newInstance(/*parámetros*/) {
        LeadsFragment fragment = new LeadsFragment();
        // Setup parámeters
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámeters
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_leads, container, false);

        // Instance ListView.
        mLeadsList = (ListView) root.findViewById(R.id.leads_list);

        //
        mLeadsAdapter = new LeadsAdapter(getActivity(),
                LeadsRepository.getInstance().getLeads());

        //
        mLeadsList.setAdapter(mLeadsAdapter);

        //
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Lead currentLead = mLeadsAdapter.getItem(position);
            }
        });

        setHasOptionsMenu(true);

        //Set the title of this fragment
        StartActivity startActivity = (StartActivity) getActivity();

        // Set title bar
        startActivity.setToolBar("FAQ", null);


        return root;
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
            //
//

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
