package me.demo.email.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import butterknife.*;
import me.demo.email.R;
import me.demo.email.adapter.EmailsAdapter;
import me.demo.email.api.AEmail;
import me.demo.email.api.Email;
import me.demo.email.api.EmailApi;

import java.io.Serializable;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.email_lab_title)
    TextView lab_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    EmailsAdapter emailsAdapter;
    Email email = null;
    List<AEmail> emails = null;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String usr = intent.getStringExtra("usr");
        email = new Email(this, usr);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        emails = new ArrayList<>();
        emailsAdapter = new EmailsAdapter(this, emails);
        listView.setAdapter(emailsAdapter);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1:
                int status = AEmail.DeleteStatus.SOFT_DELETE;
                if (menuItem.getItemId() == R.id.nav_deleted) {
                    status = AEmail.DeleteStatus.HARD_DELETE;
                }
                email.delete(emails.get(info.position).getId(), status);
                onNavigationItemSelected(menuItem);
            break;
        }
        return super.onContextItemSelected(item);
    }

    @OnItemClick(R.id.listview)
    void itemClick(int pos) {
        Intent intent = null;
        if (menuItem.getItemId() != R.id.nav_drafts) {
            intent = new Intent(this, VerboseEmailActivity.class);
            email.markRead(emails.get(pos).getId());
        } else {
            intent = new Intent(this, SendEmailActivity.class);
        }
        intent.putExtra("bar_title", lab_title.getText().toString());
        intent.putExtra("id", emails.get(pos).getId());
        intent.putExtra("usr", email.getUsr());
        intent.putExtra("title", emails.get(pos).getTitle());
        intent.putExtra("content", emails.get(pos).getContent());
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onNavigationItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        this.menuItem = item;
        item.setChecked(true);
        lab_title.setText(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        int itemId = item.getItemId();
        if (email == null)
            return true;

        if (itemId == R.id.nav_in) {
            emails = email.read(EmailApi.Box.IN);
        } else if (itemId == R.id.nav_out) {
            emails = email.read(EmailApi.Box.OUT);
        } else if (itemId == R.id.nav_drafts) {
            emails = email.read(EmailApi.Box.DRAFT);
        } else if (itemId == R.id.nav_deleted) {
            emails = email.read(EmailApi.Box.DELETE);
        } else if (itemId == R.id.nav_exit) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            return true;
        }

        if (emails == null)
            return true;

        emailsAdapter.setEmails(emails, item.getTitle().toString());
        emailsAdapter.notifyDataSetChanged();

        return true;
    }

    @OnClick(R.id.fab)
    void fabClick() {
        Intent intent = new Intent(this, SendEmailActivity.class);
        intent.putExtra("usr", email.getUsr());
        startActivityForResult(intent, 0);
    }
}
