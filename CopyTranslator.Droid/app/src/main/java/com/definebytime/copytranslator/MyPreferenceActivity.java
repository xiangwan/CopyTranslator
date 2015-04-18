package com.definebytime.copytranslator;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xiangwan on 2015/4/18.
 */
public class MyPreferenceActivity extends PreferenceActivity {
    private List<Header> mCopyHeaders;

    @Override
    public void loadHeadersFromResource(int resid, List<Header> target) {
        super.loadHeadersFromResource(resid, target);
        mCopyHeaders = target;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mCopyHeaders != null && mCopyHeaders.size() > 0) {
            setListAdapter(new HeaderAdapter(this, mCopyHeaders));
            getListView().setFooterDividersEnabled(false);
        }
    }

    public class HeaderAdapter extends ArrayAdapter<Header> {
        private class HeaderViewHolder {
            public ImageView icon;
            public TextView title;
            public TextView summary;
        }

        private LayoutInflater mInflater;

        public HeaderAdapter(Context context, List<Header> objects) {
            super(context, 0, objects);

            mInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public android.view.View getView(int position, View convertView, ViewGroup parent) {

            HeaderViewHolder holder;
            View view;

            view = mInflater.inflate(R.layout.preference_header_item, parent, false);

            holder = new HeaderViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.setting_header_icon);
            holder.title = (TextView) view.findViewById(R.id.setting_header_title);
            holder.summary = (TextView) view.findViewById(R.id.setting_header_summary);


            // All view fields must be updated every time, because the view may be recycled
            Header header = getItem(position);
            if (header.iconRes == 0) {
                view.setClickable(false);
                holder.icon.setVisibility(View.GONE);
                holder.title.setVisibility(View.GONE);
                holder.summary.setVisibility(View.GONE);
                view.setBackgroundColor(getContext().getResources().getColor(R.color.background_window));
            } else {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(header.iconRes);
                holder.title.setText(header.getTitle(getContext().getResources()));
                String summary = header.getSummary(getContext().getResources()).toString();
                if (summary != "") {
                    holder.summary.setVisibility(View.VISIBLE);
                    holder.summary.setText(summary);
                }
            }
            return view;
        }
    }
}
