package com.glitterlab.gridsearchimage.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glitterlab.gridsearchimage.R;
import com.glitterlab.gridsearchimage.models.ImageSearchResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chetan on 6/13/2015.
 */
public class ImageSearchAdapter extends ArrayAdapter<ImageSearchResult> {

    public ImageSearchAdapter(Context context, List<ImageSearchResult> image) {
        super(context,R.layout.search_item, image);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageSearchResult imageInfo= getItem(position);
        if (convertView == null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.search_item,parent,false);
        }
        ImageView image= (ImageView)convertView.findViewById(R.id.ivImage);
        TextView tvtitle = (TextView)convertView.findViewById(R.id.tvTitle);
image.setImageResource(0);
        tvtitle.setText(Html.fromHtml(ImageSearchResult.title));
        Picasso.with(getContext()).load(ImageSearchResult.thumburl).into(image);



        return convertView;
    }
}
