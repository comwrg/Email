package me.demo.email.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import me.demo.email.R;
import me.demo.email.api.AEmail;

import java.util.List;


public class EmailsAdapter extends BaseAdapter implements View.OnClickListener {
    private LayoutInflater inflater;
    private List<AEmail> emails;
    private String type;
    public StarClickListener starClick;

    @Override
    public void onClick(View v) {
        ImageView img = (ImageView) v;
        StarTag tag = (StarTag) v.getTag();
        tag.star = !tag.star;
        if (tag.star) {
            img.setImageResource(R.drawable.filled_star);
        } else {
            img.setImageResource(R.drawable.star);
        }
        starClick.starClick(v);
    }

    public interface StarClickListener {
        public void starClick(View v);
    }

    public class StarTag {
        public int pos;
        public boolean star = false;
        public StarTag(int pos) {
            this.pos = pos;
        }
    }

    public EmailsAdapter(Context context, List<AEmail> emails, StarClickListener starClick) {
        this.inflater = LayoutInflater.from(context);
        this.emails = emails;
        this.starClick = starClick;
    }

    public void setEmails(List<AEmail> emails, String type) {
        this.emails = emails;
        this.type = type;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        StarTag starTag = new StarTag(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_email, null);
            viewHolder = new ViewHolder();
            viewHolder.Title = convertView.findViewById(R.id.item_title);
            viewHolder.Content = convertView.findViewById(R.id.item_content);
            viewHolder.Date = convertView.findViewById(R.id.item_date);
            viewHolder.Star = convertView.findViewById(R.id.img_star);
            viewHolder.Star.setTag(starTag);
            viewHolder.Star.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AEmail e = emails.get(position);
        boolean unread = e.getReadStatus() == AEmail.ReadStatus.NOT_READ
                        && e.getSendStatus() == AEmail.SendStatus.HAS_SENT
                        && type.equals("收件箱");
        String unreadPrefix = "";
        if (unread) {
            unreadPrefix = "（未读 " + e.getSender() +"） ";
        }
        viewHolder.Title.setText(unreadPrefix + e.getTitle());
        viewHolder.Title
                .getPaint()
                .setFakeBoldText(unread);
        viewHolder.Content.setText(e.getContent());
        viewHolder.Date.setText(e.getDate());
        if (e.getStarStatus() == AEmail.StarStatus.STAR) {
            starTag.star = true;
            viewHolder.Star.setImageResource(R.drawable.filled_star);
        }

        return convertView;
    }


    static class ViewHolder {
        TextView Title;
        TextView Content;
        TextView Date;
        ImageView Star;
    }
}
