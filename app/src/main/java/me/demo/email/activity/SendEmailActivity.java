package me.demo.email.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.demo.email.R;
import me.demo.email.api.AEmail;
import me.demo.email.api.Email;

public class SendEmailActivity extends BaseActivity {
    @BindView(R.id.email_lab_title)
    TextView lab_title;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.txt_receiver)
    EditText txt_receiver;
    @BindView(R.id.txt_title)
    EditText txt_title;
    @BindView(R.id.txt_content)
    EditText txt_content;

    Email email = null;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        ButterKnife.bind(this);
        lab_title.setText("写邮件");

        Intent intent = getIntent();
        email = new Email(this, intent.getStringExtra("usr"));
        id = intent.getIntExtra("id", -1);
        if (id > -1) {
            AEmail aEmail = email.read(id);
            txt_title.setText(aEmail.getTitle());
            txt_content.setText(aEmail.getContent());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (id == -2)
            return;
        String receiver = txt_receiver.getText().toString();
        String title = txt_title.getText().toString();
        String content = txt_content.getText().toString();
        boolean r = false;
        if (id > -1) {
            r = email.updateDrafts(id, title, content, receiver);
        } else {
            r = email.addDrafts(title, content, receiver);
        }
        String s;
        if (r) {
            s = "邮件已存为草稿。";
        } else {
            s = "存草稿失败！";
        }
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab)
    void fabClick() {
        String receiver = txt_receiver.getText().toString();
        String title = txt_title.getText().toString();
        String content = txt_content.getText().toString();
        if (receiver.equals("") || title.equals("") || content.equals("")) {
            Toast.makeText(this, "请填写内容！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean r;
        if (id > -1) {
            r = email.sendEmail(id, receiver, title, content);
        } else {
            r = email.sendEmail(receiver, title, content);
        }
        if (r) {
            Toast.makeText(this, "发送成功！", Toast.LENGTH_SHORT).show();
            id = -2;

            finish();
        } else {
            Toast.makeText(this, "发送失败，原因未知！", Toast.LENGTH_SHORT).show();
        }
    }
}
