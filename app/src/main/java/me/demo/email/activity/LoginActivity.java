package me.demo.email.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.demo.email.R;
import me.demo.email.api.Email;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_txt_usr)
    EditText txt_usr;
    @BindView(R.id.login_txt_pwd)
    EditText txt_pwd;
    @BindView(R.id.login_btn_login)
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_btn_login)
    void btnLoginClick() {
        String usr = txt_usr.getText().toString();
        String pwd = txt_pwd.getText().toString();
        if (usr.equals("") || pwd.equals("")) {
            Toast.makeText(this, "请填写帐号密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        Email email = new Email(this);
        boolean r = email.login(usr, pwd);
        if (!r) {
            Toast.makeText(this, "帐号或密码错误！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, EmailActivity.class);
        intent.putExtra("usr", usr);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.login_btn_register)
    void btnRegisterClick() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}
