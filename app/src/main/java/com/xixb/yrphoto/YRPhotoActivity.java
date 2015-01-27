package com.xixb.yrphoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class YRPhotoActivity extends Activity {

    private Handler handler = new Handler();
    ImageView imageview;
    TextView textview;

    int alpha = 255;
    int b = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yr_activity);
        imageview = (ImageView) this.findViewById(R.id.ImageView01);
        textview = (TextView) this.findViewById(R.id.TextView01);

        imageview.setImageAlpha(alpha);
        new Thread(new Runnable() {
            public void run() {
                initApp();

                while (b < 2) {
                    try {
                        if (b == 0) {
                            Thread.sleep(1000);
                            b = 1;
                        } else {
                            Thread.sleep(50);
                        }

                        updateApp();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageview.setImageAlpha(alpha);
                imageview.invalidate();

            }
        };

    }

    public void initApp(){

    }

    public void updateApp() {
        alpha -= 5;

        if (alpha <= 0) {
            b = 2;
            Intent in = new Intent(this, com.xixb.yrphoto.YRPhotoMainActivity.class);
            startActivity(in);
            this.finish();
        }

        handler.sendMessage(handler.obtainMessage());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
