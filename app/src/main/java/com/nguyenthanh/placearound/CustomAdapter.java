package com.nguyenthanh.placearound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nguyenthanh.placearound.model_places.Information;

import java.util.List;

/**
 * Created by Administrator on 3/29/2017.
 */

public class CustomAdapter extends ArrayAdapter<Information> {

    private LayoutInflater inflater;
    public List<Information> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvId;
        TextView tvContent;
        TextView tvCapaign;
        TextView tvCreatorName;
//        ImageView imgChoice;
    }

    public CustomAdapter(List<Information> data, Context context) {
        super(context, R.layout.activity_single_item, data);
        this.dataSet = data;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Information dataModel = getItem(position);
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
            viewHolder.tvContent = (TextView)
                    convertView.findViewById(R.id.tv_content);
            viewHolder.tvCapaign = (TextView)
                    convertView.findViewById(R.id.tv_campaign);
//            viewHolder.imgChoice = (ImageView)
//                    convertView.findViewById(R.id.chossing);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


//        viewHolder.tvId.setTextColor(
//                dataModel.
//                        isChecked() ?
//                        Color.parseColor("#5A9B97") :
//                        Color.BLACK);
//
//        viewHolder.tvId.setTypeface(null,
//                (dataModel.isChecked() ?
//                        Typeface.BOLD :
//                        Typeface.NORMAL));
//
//        viewHolder.imgChoice.setVisibility(
//                dataModel.isChecked() ?
//                        View.VISIBLE :
//                        View.GONE);

        viewHolder.tvContent.setText("Name Place : " + dataModel.getName());
        viewHolder.tvCapaign.setText("Address: " + dataModel.getAddress());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Information getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }

//    public void setCheck(int position) {
//        for (int i = 0; i < getCount(); i++) {
//            getItem(i).setChecked(false);
//
//        }
//        getItem(position).setChecked(true);
//        notifyDataSetChanged();
//    }

}