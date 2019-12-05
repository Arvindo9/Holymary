package com.king.holymary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Arvindo on 07-04-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class First_Activity extends AppCompatActivity {

    Button register,login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpage);
        register  = (Button)findViewById(R.id.registerbtn);
        login = (Button)findViewById(R.id.signinbtn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerClass = new Intent(First_Activity.this, MainActivity.class);
                startActivity(registerClass);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginClass = new Intent(First_Activity.this, LogIn.class);
                startActivity(loginClass);
            }
        });

    }
}
