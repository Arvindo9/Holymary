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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import static com.king.holymary.constant.Config.REGISTRATION_URL;
import static com.king.holymary.constant.InitialInfo.BRANCH_STREAM_NAME;
import static com.king.holymary.constant.InitialInfo.COLLAGE_NAME;
import static com.king.holymary.constant.InitialInfo.DEPARTMENT_NAME;
import static com.king.holymary.constant.InitialInfo.USER_TYPE;

public class MainActivity extends AppCompatActivity {

    //    private String reqUrl = "http://vchmgi.com/holymary/";
//    private String reqUrl = "http://192.168.1.65/hm/";
//    private String reqUrl = "http://192.168.0.22/hm/";
    private Button register,login;
    private EditText id_et, name_et, email_et, phone_et, pass_et, pass_re_et;
    private String  registeration_type ="", id_s, name_s, email_s, phone_s, pass_s, pass_re_s;
//    private Toolbar toolbar;
    private Spinner colname,type,branch,dept;
    private String tspinner,depSpinner_s,bSpinner_s;
    private String deptName_s, branchName_s;
    private String yearS, semesterS, sectionS,cspinner;
    private String clgCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        cname_et = (EditText)findViewById(R.id.clgname) ;
        id_et = (EditText)findViewById(R.id.roll) ;
        name_et = (EditText)findViewById(R.id.student_name) ;
        phone_et = (EditText)findViewById(R.id.student_phno) ;
        email_et = (EditText)findViewById(R.id.student_email) ;
        pass_et = (EditText)findViewById(R.id.passwrd) ;
        pass_re_et = (EditText)findViewById(R.id.Cpassword) ;
        type = (Spinner) findViewById(R.id.type);
        branch = (Spinner) findViewById(R.id.branch);
        dept = (Spinner) findViewById(R.id.dept);
        colname = (Spinner)findViewById(R.id.colgname);

//        final List<String> typesList = new ArrayList<>(Arrays.asList(TypesArray));
//        final List<String> branchList = new ArrayList<>(Arrays.asList(BranchArray));
//        final List<String> deptList = new ArrayList<>(Arrays.asList(DeptArray));
        final ArrayAdapter<String> collegeArrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(),
                R.layout.marital_item, COLLAGE_NAME){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        collegeArrayAdapter.setDropDownViewResource(R.layout.marital_item);
        colname.setAdapter(collegeArrayAdapter);

        final ArrayAdapter<String> typesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.marital_item, USER_TYPE){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        typesArrayAdapter.setDropDownViewResource(R.layout.marital_item);
        type.setAdapter(typesArrayAdapter);
//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
        final ArrayAdapter<String> branchArrayAdapter =
                new ArrayAdapter<String>(getApplicationContext(),R.layout.marital_item, BRANCH_STREAM_NAME){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        branchArrayAdapter.setDropDownViewResource(R.layout.marital_item);
        branch.setAdapter(branchArrayAdapter);
        final ArrayAdapter<String> deptArrayAdapter = new
                ArrayAdapter<String>(getApplicationContext(),R.layout.marital_item, DEPARTMENT_NAME){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        deptArrayAdapter.setDropDownViewResource(R.layout.marital_item);
        dept.setAdapter(deptArrayAdapter);
        colname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cspinner = parent.getItemAtPosition(position).toString();
                switch (position){
                    case 1: clgCode = "1001";
                        break;
                    case 2: clgCode = "1002";
                        break;
                    case 3: clgCode = "1003";
                        break;
                    case 4: clgCode = "1004";
                        break;
                    default:clgCode = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 1:
                        registeration_type = "2";
                        id_et.setVisibility(View.VISIBLE);
                        dept.setVisibility(View.VISIBLE);
                        branch.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        registeration_type = "1";
                        id_et.setVisibility(View.VISIBLE);
                        dept.setVisibility(View.VISIBLE);
                        branch.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        registeration_type = "4";
                        dept.setVisibility(View.GONE);
                        branch.setVisibility(View.GONE);
                        break;
                    case 4:
                        registeration_type = "3";
                        id_et.setVisibility(View.GONE);
                        dept.setVisibility(View.GONE);
                        branch.setVisibility(View.GONE);
                        break;
                    case 5:
                        dept.setVisibility(View.GONE);
                        branch.setVisibility(View.GONE);
                        registeration_type = "5";
                    default:

                        break;
                }
               tspinner = adapterView.getItemAtPosition(i).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                 Toast.makeText(getApplicationContext(),"Please Select Type",Toast.LENGTH_SHORT).show();
            }
        });
        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                depSpinner_s = dept.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"Please Select Type",Toast.LENGTH_SHORT).show();

            }
        });
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bSpinner_s = branch.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"Please Select Type",Toast.LENGTH_SHORT).show();

            }
        });
        register = (Button)findViewById(R.id.registerbtn);
//        login = (Button)findViewById(R.id.login);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this,LogIn.class);
//                startActivity(i);
//                finish();
//            }
//        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Validations();

//                Intent i = new Intent(MainActivity.this,LogIn.class);
//                startActivity(i);

//                new RegisterTask().execute(REGISTRATION_URL);
            }
        });
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

    }


    private class RegisterTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
//            registeration_type = tspinner;
//            colName_s = cname_et.getText().toString();
            id_s = id_et.getText().toString();
            name_s = name_et.getText().toString();
            phone_s = phone_et.getText().toString();
            email_s = email_et.getText().toString();
//            dob_s = dob_et.getText().toString();
            pass_s = pass_et.getText().toString();
            pass_re_s = pass_re_et.getText().toString();
//            deptName_s = "";
//            branchName_s = "";
            yearS = "";
            semesterS = "";
            sectionS = "";

            if (isNetworkAvailable()) {
                dialog.setTitle("Uploading");
                dialog.setMessage("Processing...");
                dialog.setCancelable(false);
                dialog.show();
            }
            else{
                Toast.makeText(MainActivity.this, "Sorry! Check internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... url) {
            return sendData(url[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("regis ", registeration_type);
            Log.e("response df ", result);
            if (result!=null && result.contains("success")) {
                //inset in my sqlite db
                //userid, pass, type, ph
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                DataBaseAwake_1 db = new DataBaseAwake_1(MainActivity.this);
                try {
                    db.insertRegistrationTB(new DB_HandlerAwakw_1(id_s, registeration_type, "false", "ok"));
                } catch (SQLException e) {
                    try {
                        db.insertRegistrationTB(new DB_HandlerAwakw_1(id_s, registeration_type, "false", "ok"));
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

                Intent i = new Intent(MainActivity.this,LogIn.class);
                startActivity(i);
                finish();
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

    private String sendData(String url1) {

        try {

            URL url = new URL(REGISTRATION_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.connect();

            String reqHead = "Accept:application/json";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection","Keep-Alive"+reqHead);
            //Header header = new Header();

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            switch (registeration_type){
                case "1": //student
                    entity.addPart("register_type",new StringBody(registeration_type));
                    entity.addPart("user_id",new StringBody(id_s));
                    entity.addPart("college_name",new StringBody(clgCode));
                    entity.addPart("department",new StringBody(depSpinner_s));
                    entity.addPart("branch",new StringBody(bSpinner_s));
                    entity.addPart("name_s",new StringBody(name_s));
                    entity.addPart("email",new StringBody(email_s));
                    entity.addPart("phone_number",new StringBody(phone_s));
                    entity.addPart("password",new StringBody(pass_s));
                    entity.addPart("current_year",new StringBody(yearS));
                    entity.addPart("current_semester",new StringBody(semesterS));
                    entity.addPart("current_section",new StringBody(sectionS));
                    break;

                case "2": //admin
                    entity.addPart("register_type",new StringBody(registeration_type));
                    entity.addPart("user_id",new StringBody(id_s));
                    entity.addPart("college_name",new StringBody(clgCode));
                    entity.addPart("department",new StringBody(depSpinner_s));
                    entity.addPart("branch",new StringBody(bSpinner_s));
                    entity.addPart("name_s",new StringBody(name_s));
                    entity.addPart("email",new StringBody(email_s));
                    entity.addPart("phone_number",new StringBody(phone_s));
                    entity.addPart("password",new StringBody(pass_s));
                    break;

                case "3": //staff
                    entity.addPart("register_type",new StringBody(registeration_type));
                    entity.addPart("user_id",new StringBody(id_s));
                    entity.addPart("college_name",new StringBody(clgCode));
                    entity.addPart("department",new StringBody(depSpinner_s));
                    entity.addPart("branch",new StringBody(branchName_s));
                    entity.addPart("name_s",new StringBody(name_s));
                    entity.addPart("email",new StringBody(email_s));
                    entity.addPart("phone_number",new StringBody(phone_s));
                    entity.addPart("password",new StringBody(pass_s));
                    break;

                case "4": //parents
                    entity.addPart("register_type",new StringBody(registeration_type));
                    entity.addPart("user_id",new StringBody(id_s));
                    entity.addPart("email",new StringBody(email_s));
                    entity.addPart("college_name",new StringBody(clgCode));
                    entity.addPart("department",new StringBody(depSpinner_s));
//                    entity.addPart("branch",new StringBody(bSpinner));
                    entity.addPart("name_s",new StringBody(name_s));
                    entity.addPart("phone_number",new StringBody(phone_s));
                    entity.addPart("password",new StringBody(pass_s));
                    break;

                case "5": //others
                    int a = 10 + (int)(Math.random() * 50);
                    int b = 100 + (int)(Math.random() * 1000);
                    int c = 10 + (int)(Math.random() * 1000);
                    String userId = "user_" + a + "_" + b + "_" + c;
                    entity.addPart("register_type",new StringBody(registeration_type));
                    entity.addPart("user_id",new StringBody(userId));
                    entity.addPart("email",new StringBody(email_s));
                    entity.addPart("college_name",new StringBody(clgCode));
//                    entity.addPart("department",new StringBody(depSpinner_s));
//                    entity.addPart("branch",new StringBody(bSpinner_s));
                    entity.addPart("name_s",new StringBody(name_s));
                    entity.addPart("phone_number",new StringBody(phone_s));
                    entity.addPart("password",new StringBody(pass_s));
                    break;
            }
//            entity.addPart("register_type",new StringBody(registeration_type));
//            entity.addPart("user_id",new StringBody(id_s));
//            entity.addPart("email",new StringBody(email_s));
//            entity.addPart("department",new StringBody(depSpinner));
//            entity.addPart("branch",new StringBody(bSpinner));
//            entity.addPart("name_s",new StringBody(name_s));
//            entity.addPart("phone_number",new StringBody(phone_s));
//            entity.addPart("password",new StringBody(pass_s));
//            entity.addPart("",new StringBody(cpass));
//            entity.addPart("document",picBody);
            connection.addRequestProperty("content-length",entity.getContentLength()+"");
            connection.addRequestProperty(entity.getContentType().getName(),entity.getContentType().getValue());

            BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
            entity.writeTo(connection.getOutputStream());
            os.close();
            Log.d("HITTING","hitting url");
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
            while ((line = reader.readLine())!=null){
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

        return response;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void Validations(){
//        colName_s = cname_et.getText().toString();
        id_s = id_et.getText().toString();
        name_s = name_et.getText().toString();
        phone_s = phone_et.getText().toString();
        email_s = email_et.getText().toString();
//            dob_s = dob_et.getText().toString();
        pass_s = pass_et.getText().toString();
        pass_re_s = pass_re_et.getText().toString();
        yearS = "";
        semesterS = "";
        sectionS = "";
         if(clgCode.equals("")){
            TextView errorText = (TextView)colname.getSelectedView();
            errorText.setTextColor(Color.RED);
            errorText.setText("Please Select");
             Toast.makeText(getApplicationContext(),"Please Fill",Toast.LENGTH_LONG).show();
        }else if(tspinner.equals("Type")){
            TextView errorText = (TextView)type.getSelectedView();
            errorText.setTextColor(Color.RED);
            errorText.setText("Please Select");
        }else  if(id_s.length()==0){
            id_et.setError("Please Fill");
        }else if(name_s.length()==0) {
            name_et.setError("Please Fill");
        }else if(depSpinner_s.equals("Select Department")){
            TextView errorText = (TextView)dept.getSelectedView();
            errorText.setTextColor(Color.RED);
            errorText.setText("Please Select");
        }else if(bSpinner_s.equals("Select Stream")) {
            TextView errorText = (TextView) branch.getSelectedView();
            errorText.setTextColor(Color.RED);
            errorText.setText("Please Select");
        }else if (phone_s.length()==0){
            phone_et.setError("Please Fill");
        }else if (email_s.length()==0){
            email_et.setError("Please Fill");
        }else if (pass_s.length()==0){
            pass_et.setError("Please Fill");
        }else if (pass_re_s.length()==0){
            pass_re_et.setError("Please Fill");
        }else if (!pass_s.equals(pass_re_s)) {
                pass_re_et.setError("Password doesn't match");
        }else {
             new RegisterTask().execute(REGISTRATION_URL);
            }
        }

    public void Validation(){
//        colName_s = cname_et.getText().toString();
        id_s = id_et.getText().toString();
        name_s = name_et.getText().toString();
        phone_s = phone_et.getText().toString();
        email_s = email_et.getText().toString();
//            dob_s = dob_et.getText().toString();
        pass_s = pass_et.getText().toString();
        pass_re_s = pass_re_et.getText().toString();
        yearS = "";
        semesterS = "";
        sectionS = "";
        if(cspinner.equals("Select College Name")){
            TextView errorText = (TextView)colname.getSelectedView();
            errorText.setTextColor(Color.RED);
            errorText.setText("Please Select");
            Toast.makeText(getApplicationContext(),"Please Fill",Toast.LENGTH_LONG).show();
        }else if(tspinner.equals("Type")){
            TextView errorText = (TextView)type.getSelectedView();
            errorText.setTextColor(Color.RED);
            errorText.setText("Please Select");
        }else  if(id_s.length()==0){
            id_et.setError("Please Fill");
        }else if(name_s.length()==0) {
            name_et.setError("Please Fill");
        }else if (phone_s.length()==0){
            phone_et.setError("Please Fill");
        }else if (email_s.length()==0){
            email_et.setError("Please Fill");
        }else if (pass_s.length()==0){
            pass_et.setError("Please Fill");
        }else if (pass_re_s.length()==0){
            pass_re_et.setError("Please Fill");
        }else if (!pass_s.equals(pass_re_s)) {
            pass_re_et.setError("Password doesn't match");
        }else {
            new RegisterTask().execute(REGISTRATION_URL);
        }
    }
}
