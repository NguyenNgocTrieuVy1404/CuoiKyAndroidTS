package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.modeldata.Notification;
import com.example.technologyactivity.R;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.thongbao_layout_item, parent, false);
        }

        Notification notification = notifications.get(position);

        TextView titleTextView = convertView.findViewById(R.id.notification_title);
        TextView messageTextView = convertView.findViewById(R.id.notification_message);

        titleTextView.setText(notification.getTitle());
        messageTextView.setText(notification.getMessage());

        return convertView;
    }
}
