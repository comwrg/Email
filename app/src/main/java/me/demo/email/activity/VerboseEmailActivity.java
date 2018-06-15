package me.demo.email.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.demo.email.R;

public class VerboseEmailActivity extends BaseActivity {
    @BindView(R.id.email_lab_title)
    TextView email_lab_title;
    @BindView(R.id.lab_title)
    TextView lab_title;
    @BindView(R.id.lab_content)
    TextView lab_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbose_email);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        email_lab_title.setText(intent.getStringExtra("bar_title"));
        lab_title.setText(intent.getStringExtra("title"));
        lab_content.setText(intent.getStringExtra("content"));
    }

}
