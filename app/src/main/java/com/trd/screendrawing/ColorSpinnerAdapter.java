package com.trd.screendrawing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;


public class ColorSpinnerAdapter extends ArrayAdapter {
    private LayoutInflater layoutInflater;

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.color_spinner_bg, parent, false);
        return provideView(view, position);
    }

    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View cv = convertView;
        if (convertView == null) {
            cv = this.layoutInflater.inflate(R.layout.color_item, parent, false);
        }
        return this.provideView(cv, position);
    }

    private View provideView(View view, int position) {
        ColorItem colorItem = (ColorItem) this.getItem(position);
        if (colorItem != null) {
            View colorBlob = view.findViewById(R.id.colorBlob);
            if (colorBlob != null) {
                Drawable colorBlobBackground = colorBlob.getBackground();
                if (colorBlobBackground != null) {
                    colorBlobBackground.setTint(Color.parseColor(colorItem.getHexHash()));
                }
            }
        }
        return view;
    }

    public ColorSpinnerAdapter(Context context, List list) {
        super(context, 0, list);
        this.layoutInflater = LayoutInflater.from(context);
    }
}