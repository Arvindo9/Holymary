package com.king.holymary;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.king.holymary.database_sqlite.DataBaseAwake_1;

import java.sql.SQLException;

/**
 * Created by Arvindo on 31-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class StartApp extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_app);

        Fresco.initialize(getApplicationContext());

        new Progress().execute("");
    }


    private class Progress extends AsyncTask<String, Integer, Boolean>{
        private DataBaseAwake_1 db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;



            db = new DataBaseAwake_1(StartApp.this);
            try {
                if(db.checkStatus()){
                    result = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Intent i = new Intent(StartApp.this, Tabs_Activity.class);
                StartApp.this.startActivity(i);
                finish();
            }
            else{
                StartApp.this.startActivity(new Intent(StartApp.this, First_Activity.class));
                finish();
            }
        }
    }
}
