package monash.ultimateinhaler;

/**
 * Created by jewel on 9/15/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Adapter leads
 */
public class LeadsAdapter extends ArrayAdapter<Lead> {
    public LeadsAdapter(Context context, List<Lead> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        //
        if (null == convertView) {
            //
            convertView = inflater.inflate(
                    R.layout.list_item_lead,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        Lead lead = getItem(position);

        // Setup.
        holder.name.setText(lead.getName());
        holder.title.setText(lead.getTitle());

        Glide.with(getContext()).load(lead.getImage()).into(holder.avatar);

        return convertView;
    }

    static class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView title;

    }
}
