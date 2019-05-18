package com.lockesec.quizapp401;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ProfileListAdapter extends ArrayAdapter<Profile> {

    private Context context;
    private int resource;

    public ProfileListAdapter(Context context, int resource, List<Profile> list)
    {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String name = getItem(position).getName();
        int score = getItem(position).getScore();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        ImageView pictureImageView = convertView.findViewById(R.id.leaderboard_profile_pic);
        TextView nameTextView = convertView.findViewById(R.id.list_view_textView1);
        TextView scoreTextView = convertView.findViewById(R.id.list_view_textView2);

        nameTextView.setText(name);
        scoreTextView.setText(score + "");
        Glide.with(convertView).load(getItem(position).getProfilePicURL()).apply(new RequestOptions().override(420, 420)).circleCrop().into(pictureImageView);

        return convertView;
    }
}
