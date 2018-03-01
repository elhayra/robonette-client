package com.robonette.argubit.robonette;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoListAdapter extends ArrayAdapter<InfoItem>
{
    private static final String TAG = "InfoListAdapter";
    private Context context;
    private int resource;

    static class ViewHolder
    {
        TextView label, data, unit;
    }

    public InfoListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<InfoItem> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        String label = getItem(position).getLabel();
        String data = getItem(position).getData();
        String unit = getItem(position).getUnit();

        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();

            holder.label = convertView.findViewById(R.id.textView1);
            holder.data = convertView.findViewById(R.id.textView2);
            holder.unit = convertView.findViewById(R.id.textView3);

            holder.label.setText(label);
            holder.data.setText(data);
            holder.unit.setText(unit);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
