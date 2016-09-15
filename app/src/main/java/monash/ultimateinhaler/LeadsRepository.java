package monash.ultimateinhaler;

/**
 * Created by jewel on 9/15/16.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Repositorio ficticio de leads
 */
public class LeadsRepository {
    private static LeadsRepository repository = new LeadsRepository();
    private HashMap<String, Lead> leads = new HashMap<>();

    public static LeadsRepository getInstance() {
        return repository;
    }

    private LeadsRepository() {
        saveLead(new Lead("How do I navigate around the new app?", "There is a navigation bar in the bottom of the screen, where you can click on each icon to access the different functions.","", R.drawable.lead_photo_1));
        saveLead(new Lead("How can I add my location?", "The location is obtained automatically from the device GPS. In case the App is not showing your current location, please check your device GPS is active.", "", R.drawable.lead_photo_2));
        saveLead(new Lead("App doesn't work", "In case the App is not working, please try to uninstall it and install it again.", "", R.drawable.lead_photo_3));
        saveLead(new Lead("Where does the weather come from?", "The App is using the Yahoo API weather", "", R.drawable.lead_photo_4));
        saveLead(new Lead("Where does the pollen count come from?", "Data", "", R.drawable.lead_photo_5));
        saveLead(new Lead("Where does the biodiversity comes from?", "The biodiversity details are obtained from data.melbourne.vic.gov.au\n", "", R.drawable.lead_photo_6));

    }

    private void saveLead(Lead lead) {
        leads.put(lead.getId(), lead);
    }

    public List<Lead> getLeads() {
        return new ArrayList<>(leads.values());
    }
}
