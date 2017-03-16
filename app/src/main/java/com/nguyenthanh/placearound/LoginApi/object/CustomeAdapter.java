package com.nguyenthanh.placearound.LoginApi.object;

/**
 * Created by Administrator on 3/16/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenthanh.placearound.R;

import java.util.List;

public class CustomeAdapter extends ArrayAdapter<ObjectResponseData> {

    private LayoutInflater inflater;
    public List<ObjectResponseData> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvId;
        TextView tvContent;
        TextView tvCapaign;
        TextView tvCreatorName;
        ImageView imgChoice;
    }

    public CustomeAdapter(List<ObjectResponseData> data, Context context) {
        super(context, R.layout.activity_single_item, data);
        this.dataSet = data;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ObjectResponseData dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(
                    R.layout.activity_single_item,
                    parent,
                    false
            );
            viewHolder.tvId = (TextView)
                    convertView.findViewById(R.id.tv_id);
            viewHolder.tvContent = (TextView)
                    convertView.findViewById(R.id.tv_content);
            viewHolder.tvCapaign = (TextView)
                    convertView.findViewById(R.id.tv_campaign);
            viewHolder.tvCreatorName = (TextView)
                    convertView.findViewById(R.id.tv_creatorName);
            viewHolder.imgChoice = (ImageView)
                    convertView.findViewById(R.id.chossing);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tvId.setTextColor(
                dataModel.
                        isChecked() ?
                        Color.parseColor("#5A9B97") :
                        Color.BLACK);

        viewHolder.tvId.setTypeface(null,
                (dataModel.isChecked() ?
                        Typeface.BOLD :
                        Typeface.NORMAL));

        viewHolder.imgChoice.setVisibility(
                dataModel.isChecked() ?
                        View.VISIBLE :
                        View.GONE);

        viewHolder.tvId.setText("id: " + dataModel.getId());
        viewHolder.tvContent.setText("Content: " + dataModel.getContent());
        viewHolder.tvCapaign.setText("Campaign: " + dataModel.getCampaign());
        viewHolder.tvCreatorName.setText("CreatorName: " + dataModel.getCreatorName());
        // Return the completed view to render on screen
        return convertView;
    }

    public void setCheck(int position) {
        for (int i = 0; i < getCount(); i++) {
            getItem(i).setChecked(false);

        }
        getItem(position).setChecked(true);
        notifyDataSetChanged();
    }

}