package com.example.technologyactivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.adapter.NotificationAdapter;
import com.example.modeldata.Notification;

import java.util.List;

public class NotificationFragment extends Fragment {
    private List<Notification> notificationList;
    private NotificationAdapter notificationAdapter;

    public NotificationFragment(List<Notification> notificationList, NotificationAdapter notificationAdapter) {
        this.notificationList = notificationList;
        this.notificationAdapter = notificationAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thongbao_layout, container, false);

        TextView noNotificationsText = view.findViewById(R.id.no_notifications_text);
        ListView listView = view.findViewById(R.id.notification_list_view);
        ImageView noNotificationsGif = view.findViewById(R.id.no_notifications_gif);

        if (notificationList == null || notificationList.isEmpty()) {
            noNotificationsText.setVisibility(View.VISIBLE);
            noNotificationsGif.setVisibility(View.VISIBLE);


            Glide.with(this)
                    .load(R.drawable.gifnebanoi)
                    .into(noNotificationsGif);

            listView.setVisibility(View.GONE);
        } else {
            noNotificationsText.setVisibility(View.GONE);
            noNotificationsGif.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(notificationAdapter);
        }

        return view;
    }
}
