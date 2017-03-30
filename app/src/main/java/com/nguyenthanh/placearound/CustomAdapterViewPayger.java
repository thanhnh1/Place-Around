package com.nguyenthanh.placearound;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Nguyen Thanh on 3/25/2017.
 */

public class CustomAdapterViewPayger extends PagerAdapter {

    private int[] img = {R.drawable.pic_0, R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3, R.drawable.pic_4};
    private LayoutInflater inflater;
    private Context context;

    public CustomAdapterViewPayger(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_view_pager, container, false);
        ImageView img1 = (ImageView) v.findViewById(R.id.imageView);
        //TextView tv1 = (TextView) v.findViewById(R.id.textView);
        img1.setImageResource(img[position]);
        //tv1.setText("Image: " + position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
