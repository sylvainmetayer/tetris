package fr.sylvainmetayer.tetris;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import fr.sylvainmetayer.tetris.metier.Piece;

public class MyAdapter extends ArrayAdapter<Integer> {

    private Context mContext;

    public MyAdapter(Context context, int textViewResourceId, Integer[] items) {
        super(context, textViewResourceId, items);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        int data = getItem(position);
        imageView.setImageResource(Piece.getImage(data));
        return imageView;

    }

}
