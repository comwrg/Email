package me.demo.email.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import me.demo.email.R;
import me.demo.email.api.AEmail;

import java.util.List;

public class EmailsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<AEmail> emails;
    private String type;

    public void setEmails(List<AEmail> emails, String type) {
        this.emails = emails;
        this.type = type;
    }

    public EmailsAdapter(Context context, List<AEmail> emails) {
        this.inflater = LayoutInflater.from(context);
        this.emails = emails;
    }

    @Override
    public int getCount() {
        return emails.size();
    }

    @Override
    public Object getItem(int position) {
        return emails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView Title;
        TextView Content;
        TextView Date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_email, null);
            viewHolder = new ViewHolder();
            viewHolder.Title = convertView.findViewById(R.id.item_title);
            viewHolder.Content = convertView.findViewById(R.id.item_content);
            viewHolder.Date = convertView.findViewById(R.id.item_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AEmail e = emails.get(position);
        viewHolder.Title.setText(e.getTitle());
        viewHolder.Title
                .getPaint()
                .setFakeBoldText(e.getReadStatus() == AEmail.ReadStatus.NOT_READ
                        && e.getSendStatus() == AEmail.SendStatus.HAS_SENT
                && type.equals("收件箱"));
        viewHolder.Content.setText(e.getContent());
        viewHolder.Date.setText(e.getDate());

        return convertView;
    }
}
