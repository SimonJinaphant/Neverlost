package com.neverlost.ubc.neverlost;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neverlost.ubc.neverlost.firebase.CloudMessageUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Simon Jinaphant on 04-Apr-2017.
 */

public class CloudUserListAdapter extends ArrayAdapter<CloudMessageUser> {

    ArrayList<CloudMessageUser> connectionsLists;
    Context context;
    int resource;

    // Facebook URI
    private static final String FACEBOOK_URI_BASE = "https://graph.facebook.com/";
    private static final String FACEBOOK_URI_ENDPOINT_PICTURE = "/picture?height=400&width=400&migration_overrides=%7Boctober_2012%3Atrue%7D";


    public CloudUserListAdapter(Context context, int resource, ArrayList<CloudMessageUser> objects) {
        super(context, resource, objects);
        this.connectionsLists = objects;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_clouduser, null, true);
        }
            CloudMessageUser connection = getItem(position);

            CircleImageView connectionPicture = (CircleImageView) convertView.findViewById(R.id.connection_picture);
            Picasso.with(context)
                    .load(FACEBOOK_URI_BASE + connection.getFacebookId() + FACEBOOK_URI_ENDPOINT_PICTURE)
                    .into(connectionPicture);

            TextView connectionName = (TextView) convertView.findViewById(R.id.connection_name);
            TextView connectionDescription = (TextView) convertView.findViewById(R.id.connection_description);

            connectionName.setText(connection.getName());
            connectionDescription.setText(connection.getFacebookId());


        return convertView;
    }
}

