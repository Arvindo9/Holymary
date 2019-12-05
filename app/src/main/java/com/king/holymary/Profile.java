package com.king.holymary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.king.holymary.database_sqlite.DataBaseAwake_1;
import com.king.holymary.others.Utility;
import com.king.holymary.server.AndroidMultiPartEntity;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.king.holymary.constant.Config.PROFILE_DATA_LOAD;
import static com.king.holymary.constant.Config.PROFILE_PIC_FOLDER;
import static com.king.holymary.constant.Config.PROFILE_PIC_PAGE;

/**
 * Created by Arvindo on 22-02-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class Profile extends android.support.v4.app.Fragment implements View.OnClickListener {

    private CircleImageView profilePic;

    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final int SELECT_FILE = 1;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private Bitmap mImageBitmap;
    private String userChoosenTask;
    private String pathOfPic;
    private File fileForUpload;
    private String fileExtention;
    private FileBody picBody;
    private long fileSize;
    private ProgressBar progressBar;
    private String userID_;
    private TextView userId_tv, userName_tv, phNum_tv, email_tv, clgName_tv, deptName_tv,
                     branchName_tv, date_tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBaseAwake_1 db = new DataBaseAwake_1(getActivity());
        try {
            if(db.checkStatus()){
                userID_ = db.getTrueUserId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isNetworkAvailable()){
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        new Profile.ConnectingToServer().execute("");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile,container,false);
        profilePic = (CircleImageView) view.findViewById(R.id.profile_pic_u);
        userId_tv = (TextView) view.findViewById(R.id.user_id);
        userName_tv = (TextView) view.findViewById(R.id.user_name);
        email_tv = (TextView) view.findViewById(R.id.email);
        phNum_tv = (TextView) view.findViewById(R.id.ph_num);
        clgName_tv = (TextView) view.findViewById(R.id.clg_name);
        branchName_tv = (TextView) view.findViewById(R.id.branch_name);
        deptName_tv = (TextView) view.findViewById(R.id.department_name);
        date_tv = (TextView) view.findViewById(R.id.dt_of_reg);
        profilePic.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_pic_u:
                selectImage();
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                try {
                    boolean result = Utility.checkPermission(getActivity());

                    if (items[item].equals("Take Photo")) {
//                    userChoosenTask ="Take Photo";
                        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);

                    } else if (items[item].equals("Choose from Library")) {
                        userChoosenTask = "Choose from Gallery";
                        if (result)
                            galleryIntent();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
                catch (IllegalStateException e){
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "please try later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        switch (requestCode) {
            case ACTION_TAKE_PHOTO_S:
                if (resultCode == RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;

            case SELECT_FILE:
//                onSelectFromGalleryResult(data);
                if(resultCode == RESULT_OK) {
                    onSelectFromGalleryResult(data);
                }

                break;
        }
    }

    // ---------------------------------
    // for camera

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
        super.onSaveInstanceState(outState);
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, actionCode);
        }
        else{
            Toast.makeText(getActivity(),getResources().getText(R.string.camere_avli), Toast.LENGTH_LONG)
                    .show();
        }
    }

    //-----------------------------------------------------
    //for gallery

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");

        BitmapDrawable bmD = new BitmapDrawable(getResources(), mImageBitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            Bitmap bmp = (Bitmap) extras.getParcelable("data");
//            profilePic.setBackground(bmD);
            profilePic.setImageBitmap(bmp);
        }
        else{
            profilePic.setBackgroundDrawable(bmD);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                saveImgToInternalStorage(mImageBitmap);
            }
        }).start();
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext()
                                .getContentResolver(),
                        data.getData());

                BitmapDrawable bmD = new BitmapDrawable(getResources(), mImageBitmap);

                profilePic.setImageDrawable(bmD);
            } catch (IOException e) {
                e.printStackTrace();
            }


            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveImgToInternalStorage(mImageBitmap);
                }
            }).start();
        }
    }

    private void saveImgToInternalStorage(Bitmap bitmap){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir("temp_folder", Context.MODE_PRIVATE);
        int a = (int)(Math.random() * 100);
        int b = 100 + (int)(Math.random() * 500);
        int c = 700 + (int)(Math.random() * 1000);

        pathOfPic = "img_" + a + "_" + b + "_" + c + ".png" ;

        File imgPath = new File(directory, pathOfPic);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(imgPath);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        imgPath = new File(directory, pathOfPic);
        if (imgPath.exists()){
            fileForUpload = imgPath;
            fileExtention = pathOfPic.substring(pathOfPic.lastIndexOf(".") + 1, pathOfPic.length());
            picBody = new FileBody(fileForUpload);
            fileSize = fileForUpload.length();

            new Profile.SubmitToServer().execute(userID_);

            Log.e("fils exist", directory.getAbsolutePath() + "/" + pathOfPic);
        }
    }

    //-------------------------------------------------------

    private class SubmitToServer extends AsyncTask<String, Integer, String> {

//        private ProgressDialog dialog = new ProgressDialog(getActivity());
        String response;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
//            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
//            progressBar.setProgress(progress[0]);

            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String... url) {
            return uploadFile(url[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // showing the server response in an alert dialog
//            showAlert(result);
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
            }

//            dialog.dismiss();
            Log.d("DownloadTextTask", result);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String userID) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(PROFILE_PIC_PAGE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // Adding file data to http body
                entity.addPart("user_id", new StringBody(userID));
                entity.addPart("pic_path_name", picBody);
//                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            Log.e("method used " + "no", String.valueOf(fileSize));
            return responseString;
        }
    }

    //----------------------------------------------------------------------------------------------
    //loading data from server

    private class ConnectingToServer extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... url) {
            return requestServerForDataString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(getActivity(), "could not load this time", Toast.LENGTH_SHORT).show();
            }

            Log.e("DownloadTextTask", result);
        }
    }

    private String requestServerForDataString() {
        try {
            URL url = new URL(PROFILE_DATA_LOAD);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.connect();

            String reqHead = "Accept:application/json";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection","Keep-Alive"+reqHead);
            //Header header = new Header();

            @SuppressWarnings("deprecation")
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            entity.addPart("register_type",new StringBody(id_s));

            entity.addPart("user_id",new StringBody(userID_));

            connection.addRequestProperty("content-length",entity.getContentLength()+"");
            connection.addRequestProperty(entity.getContentType().getName(),entity.getContentType().getValue());

            OutputStream os = connection.getOutputStream();
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

        try {
            initializedData(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private void initializedData(String response) throws Exception {
        if(response != null && !response.equals("")){
            try {
                Log.e("sdf", response);
                JSONArray jsonArray = new JSONArray(response);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);
                    final String user_id = c.getString("user_id");
                    final String name = c.getString("name");
                    final String college_name = c.getString("college_name");
                    final String department_name = c.getString("department_name");
                    final String branch_name = c.getString("branch_name");
                    final String email = c.getString("email");
                    final String phone_number = c.getString("phone_number");
                    String pic_path_name = c.getString("pic_path_name");
                    final String dateTime = "";//c.getString("date");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userId_tv.setText(user_id);
                            userName_tv.setText(name);
                            clgName_tv.setText(college_name);
                            deptName_tv.setText(department_name);
                            branchName_tv.setText(branch_name);
                            email_tv.setText(email);
                            phNum_tv.setText(phone_number);
                            date_tv.setText(dateTime);
                        }
                    });

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    final String url = PROFILE_PIC_FOLDER + pic_path_name;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(getContext())
                                    .load(url)
                                    .placeholder(R.drawable.face) //
                                    .error(R.drawable.face) //
                                    .fit() //
                                    .into(profilePic);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
