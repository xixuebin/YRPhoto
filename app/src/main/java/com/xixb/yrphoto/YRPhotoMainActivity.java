package com.xixb.yrphoto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class YRPhotoMainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button btnSayHello;
    private Button btnQuitApp;
    private Button btnContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("YR Photo");
        setContentView(R.layout.activity_yrphoto_main);

        btnSayHello=(Button)findViewById(R.id.say_hello);
        btnSayHello.setOnClickListener(this);

        btnQuitApp = (Button) findViewById(R.id.quit_app);
        btnQuitApp.setOnClickListener(this);

        btnContacts = (Button) findViewById(R.id.get_contacts);
        btnContacts.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yrphoto_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v.equals(btnSayHello)){
            showMessage("hello, this is a demo app");
        }else if (v.equals(btnQuitApp)){
            QuitApp();
        }else if (v.equals(btnContacts)){
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);  //开始跳转
        }else {
            QuitApp();
        }

    }

    public void showMessage(String string){

        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(string)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                            }
                        }).show();
    }

    public void QuitApp() {
        new AlertDialog.Builder(YRPhotoMainActivity.this).setTitle("提示").setMessage(
                "确定退出?").setIcon(R.drawable.quit).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                }).setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();

    }
}
