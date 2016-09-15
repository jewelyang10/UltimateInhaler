package monash.ultimateinhaler;

/**
 * Created by jewel on 9/12/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jewel on 4/15/16.
 */
public class NewsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<News> newses;


    public NewsAdapter(Context context, ArrayList<News> newses) {
        this.context = context;
        this.newses = newses;
    }

    public static class ViewHolder {
        TextView articleTitle;
        TextView description;
        ImageView img;

    }

    @Override
    public int getCount() {
        return newses.size();
    }

    @Override
    public News getItem(int i) {
        return newses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;

        // Check if the view has been created for the row. If not, lets inflate it
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Reference list item layout here
            view = inflater.inflate(R.layout.list_news_item, null);

            // Setup ViewHolder and attach to view
            vh = new ViewHolder();
            vh.articleTitle = (TextView) view.findViewById(R.id.articalText);
            vh.description = (TextView) view.findViewById(R.id.descpText);
            view.setTag(vh);
        } else {
            // View has already been created, fetch our ViewHolder
            vh = (ViewHolder) view.getTag();
        }

        // Assign values to the TextViews and ImageView using the New object
        vh.articleTitle.setText(newses.get(i).getTitle());
        vh.description.setText(newses.get(i).getDescription());


        // Return the completed View of the row being processed
        return view;
    }



}

