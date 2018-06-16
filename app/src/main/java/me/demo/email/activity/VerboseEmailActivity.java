package me.demo.email.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.demo.email.R;
import me.demo.email.api.AEmail;
import me.demo.email.api.Email;

public class VerboseEmailActivity extends BaseActivity {
    @BindView(R.id.email_lab_title)
    TextView email_lab_title;
    @BindView(R.id.lab_title)
    TextView lab_title;
    @BindView(R.id.lab_content)
    TextView lab_content;
    @BindView(R.id.lab_sender)
    TextView lab_sender;
    @BindView(R.id.lab_date)
    TextView lab_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbose_email);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        email_lab_title.setText(intent.getStringExtra("bar_title"));
        String usr = intent.getStringExtra("usr");
        int id = intent.getIntExtra("id", -1);
        Email email = new Email(this, usr);
        AEmail aEmail = email.read(id);
        lab_title.setText(aEmail.getTitle());
        lab_content.setText(aEmail.getContent());
        lab_sender.setText(email.getUsr(aEmail.getReceiverId()));
        lab_date.setText(aEmail.getDate());
    }

}
