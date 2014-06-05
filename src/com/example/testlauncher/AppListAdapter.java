package com.example.testlauncher;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Arnab Chakraborty
 */
public class AppListAdapter extends ArrayAdapter<AppModel> {
    private final LayoutInflater mInflater;

    public AppListAdapter (final Context context) {
        super(context, android.R.layout.simple_list_item_2);

        mInflater = LayoutInflater.from(context);
    }

    public void setData(final ArrayList<AppModel> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addAll(final Collection<? extends AppModel> items) {
        //If the platform supports it, use addAll, otherwise add in loop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.addAll(items);
        }else{
            for(final AppModel item: items){
                super.add(item);
            }
        }
    }

    /**
     * Populate new items in the list.
     */
    @Override public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item_icon_text, parent, false);
        } else {
            view = convertView;
        }

        final AppModel item = getItem(position);
        ((ImageView)view.findViewById(R.id.icon)).setImageDrawable(item.getIcon());
        ((TextView)view.findViewById(R.id.text)).setText(item.getLabel());

        return view;
    }
}
