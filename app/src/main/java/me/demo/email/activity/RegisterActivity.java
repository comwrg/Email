package me.demo.email.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.demo.email.R;
import me.demo.email.api.Email;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.reg_txt_usr)
    EditText txt_usr;
    @BindView(R.id.reg_txt_pwd1)
    EditText txt_pwd1;
    @BindView(R.id.reg_txt_pwd2)
    EditText txt_pwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.reg_btn_reg)
    void btnRegisterClick() {
        String usr = txt_usr.getText().toString();
        String pwd1 = txt_pwd1.getText().toString();
        String pwd2 = txt_pwd2.getText().toString();
        if (usr.equals("") || pwd1.equals("") || pwd2.equals("")) {
            Toast.makeText(this, "请输入！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!usr.contains("@")) {
            Toast.makeText(this, "邮箱格式不正确!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd1.equals(pwd2)) {
            Toast.makeText(this, "两次密码输入不一样！", Toast.LENGTH_SHORT).show();
            return;
        }
        Email email = new Email(this);
        Email.RegisterCode code = email.register(usr, pwd1);
        if (Email.RegisterCode.SUCC.equals(code)) {
            Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
            finish();
        } else if (Email.RegisterCode.USR_HAS_REGISTERED.equals(code)) {
            Toast.makeText(this, "此用户名已被注册！", Toast.LENGTH_SHORT).show();
        } else if (Email.RegisterCode.UNKNOWN_ERROR.equals(code)) {
            Toast.makeText(this, "注册失败，未知原因。", Toast.LENGTH_SHORT).show();
        }
    }
}
