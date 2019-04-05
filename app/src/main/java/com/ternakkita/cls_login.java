package com.ternakkita;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class cls_login extends AppCompatActivity {

    TextView buat_akun;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_login);

        buat_akun = findViewById(R.id.txtBuat);

        buat_akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg= new Intent(cls_login.this,Register.class);
                startActivity(reg);
            }
        });

        if (checkInternet()){
            Toast.makeText(this,"Terhubung Ke Internet",Toast.LENGTH_SHORT).show();
        } else {
        Intent kon = new Intent(cls_login.this,cls_cekKoneksi.class);
        startActivity(kon);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkInternet()){

        } else {
            Intent kon = new Intent(cls_login.this,cls_cekKoneksi.class);
            startActivity(kon);
        }
    }

    public boolean checkInternet(){
        boolean connectStatus = true;
        ConnectivityManager ConnectionManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ) {
            connectStatus = true;
        }
        else {
            connectStatus = false;
        }
        return connectStatus;
    }
}

