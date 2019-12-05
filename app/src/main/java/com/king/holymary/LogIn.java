package com.king.holymary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.king.holymary.data_handler.DB_HandlerAwakw_1;
import com.king.holymary.database_sqlite.DataBaseAwake_1;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import static com.king.holymary.constant.Config.LOGIN_URL;
import static com.king.holymary.constant.InitialInfo.USER_TYPE;

/**
 * Created by Arvindo on 22-02-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class LogIn extends AppCompatActivity {
    private Button login;
    private DataBaseAwake_1 db;
    private EditText id_;
    private EditText pass_;
    private String loginType;

    private String id, tspinner;
    private String pass;
    private Spinner type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        id_ = (EditText) findViewById(R.id.user_id_et);
        pass_ = (EditText) findViewById(R.id.password_et);


        db = new DataBaseAwake_1(this);
        login = (Button) findViewById(R.id.loginbtn);
        type = (Spinner) findViewById(R.id.type);

        final ArrayAdapter<String> typesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.marital_item, USER_TYPE) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        typesArrayAdapter.setDropDownViewResource(R.layout.marital_item);
        type.setAdapter(typesArrayAdapter);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = id_.getText().toString();
                pass = pass_.getText().toString();
                Validations();
                String userID = "mvb__23][;/.12!@_)(2";
                try {
                    userID = db.getLastUserId();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (userID == null) {
                    new LoginTask().execute(LOGIN_URL);
                } else if (userID.equals(id)) {
                    new LoginTask().execute(LOGIN_URL);
                } else {
                    Toast.makeText(LogIn.this, "wrong user ID", Toast.LENGTH_SHORT).show();
                }

//                Intent i = new Intent(LogIn.this,Tabs_Activity.class);
//                startActivity(i);
            }
        });

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        //admin
                        loginType = "2";
                        break;
                    case 2:
                        //student
                        loginType = "1";
                        break;
                    case 3:
                        //staff
                        loginType = "4";
                        break;
                    case 4:
                        //parents
                        loginType = "3";
                        break;
                    default:
                        //others
                        loginType = "5";
                        break;
                }
                tspinner = adapterView.getItemAtPosition(i).toString();
//                tspinner = type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Please Select Type", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    public void Validations(){
//        if(tspinner.equals("Type")){
//            TextView errorText = (TextView)type.getSelectedView();
//            errorText.setTextColor(Color.RED);
//            errorText.setText("Please Select");
//        }else if (id.length()==0){
//            id_.setError("Please Fill");
//        }else if (pass.length()==0){
//            pass_.setError("Please Fill");
//        }else {
//        }


    private class LoginTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(LogIn.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            id = id_.getText().toString();
            pass = pass_.getText().toString();

            if (loginType.equals("5")) {
                try {
                    String i = db.getOnlyRegisteredUserID();
                    if (i != null) {
                        id = i;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (isNetworkAvailable()) {
                dialog.setTitle("Uploading");
                dialog.setMessage("Processing...");
                dialog.setCancelable(false);
                dialog.show();
            } else {
                Toast.makeText(LogIn.this, "Sorry! internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... url) {
            return sendData();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("regis ", loginType);
            Log.e("response df ", result);
            if (result != null && result.contains("success")) {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(LogIn.this, Tabs_Activity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();

//            Intent i = new Intent(MainActivity.this,LogIn.class);
//            startActivity(i);
//            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            Log.e("DownloadTextTask", result);
        }
    }

    private String sendData() {
        try {

            URL url = new URL(LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.connect();

            String reqHead = "Accept:application/json";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection", "Keep-Alive" + reqHead);
            //Header header = new Header();

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            switch (loginType) {
                case "1": //student
                    entity.addPart("login_type", new StringBody(loginType));
                    entity.addPart("user_id", new StringBody(id));
                    entity.addPart("password", new StringBody(pass));
                    break;

                case "2": //admin
                    entity.addPart("login_type", new StringBody(loginType));
                    entity.addPart("user_id", new StringBody(id));
                    entity.addPart("password", new StringBody(pass));
                    break;

                case "3": //staff
                    entity.addPart("login_type", new StringBody(loginType));
                    entity.addPart("user_id", new StringBody(id));
                    entity.addPart("password", new StringBody(pass));
                    break;

                case "4": //parents
                    entity.addPart("login_type", new StringBody(loginType));
                    entity.addPart("user_id", new StringBody(id));
                    entity.addPart("password", new StringBody(pass));
                    break;

                case "5": //others
                    entity.addPart("login_type", new StringBody(loginType));
                    entity.addPart("user_id", new StringBody(id));
                    entity.addPart("password", new StringBody(pass));
                    break;
            }

            connection.addRequestProperty("content-length", entity.getContentLength() + "");
            connection.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());

            BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
            entity.writeTo(connection.getOutputStream());
            os.close();
            Log.e("HITTING", "hitting url");
            connection.connect();

            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                return readStream(connection.getInputStream());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fails";
    }


    private String readStream(InputStream inputStream) {
        String response = "";
        BufferedReader reader;
        StringBuilder builder = new StringBuilder();

        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                Log.e("\n", builder.toString());
            }
            response = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("responset form server ", response);

        response = initializedData(response);

        return response;
    }

    private String initializedData(String response) {
        String success = "";
        if(response != null && !response.equals("")){
            try {
                Log.e("sdf", response);
                JSONArray jsonArray = new JSONArray(response);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);
                    String name = "";
                    String college_name = "";
                    String department_name = "";
                    String branch_name = "";
                    switch (loginType){
                        case "1":
                            success = c.getString("success");
                            name = c.getString("name");
                            college_name = c.getString("college_name");
                            department_name = c.getString("department_name");
                            branch_name = c.getString("branch_name");

                            if(success.equals("success")) {
                                try {
                                    db.insertRegistrationTB(new DB_HandlerAwakw_1(id, loginType, "true", "ok"));
                                    db.insertUserDetailsTB(new DB_HandlerAwakw_1(id, name, college_name,
                                            department_name, branch_name));
                                } catch (SQLException e) {
                                    try {
                                        db.insertRegistrationTB(new DB_HandlerAwakw_1(id, loginType, "true", "ok"));
                                        db.insertUserDetailsTB(new DB_HandlerAwakw_1(id, name, college_name,
                                                department_name, branch_name));
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                            break;

                        case "2":
                            success = c.getString("success");
                            name = c.getString("name");
                            college_name = c.getString("college_name");
                            department_name = c.getString("department_name");

                            if(success.equals("success")) {
                                try {
                                    db.insertRegistrationTB(new DB_HandlerAwakw_1(id, loginType, "true", "ok"));
                                    db.insertUserDetailsTB(new DB_HandlerAwakw_1(id, name,
                                            college_name, department_name, ""));
                                } catch (SQLException e) {
                                    try {
                                        db.insertRegistrationTB(new DB_HandlerAwakw_1(id, loginType, "true", "ok"));
                                        db.insertUserDetailsTB(new DB_HandlerAwakw_1(id, name,
                                                college_name, department_name, ""));
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                            break;

                        default:
                            success = c.getString("success");
                            name = c.getString("name");
                            college_name = c.getString("college_name");

                            if(success.equals("success")) {
                                try {
                                    db.insertRegistrationTB(new DB_HandlerAwakw_1(id, loginType, "true", "ok"));
                                    db.insertUserDetailsTB(new DB_HandlerAwakw_1(id, name, college_name, "",
                                            ""));
                                } catch (SQLException e) {
                                    try {
                                        db.insertRegistrationTB(new DB_HandlerAwakw_1(id, loginType, "true", "ok"));
                                        db.insertUserDetailsTB(new DB_HandlerAwakw_1(id, name, college_name, "",
                                                ""));
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return success;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void Validations() {
        if (tspinner.equals("Type")) {
            TextView errorText = (TextView) type.getSelectedView();
            errorText.setTextColor(Color.RED);
            errorText.setText("Please Select");
        } else if (id.length() == 0) {
            id_.setError("Please Fill");
        } else if (pass.length() == 0) {
            pass_.setError("Please Fill");
        } else {
        }
    }
}