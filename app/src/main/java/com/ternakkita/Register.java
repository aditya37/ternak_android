package com.ternakkita;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    EditText editText1,editText2;
    Button button1,btnok,btncancle;

    Spinner spn;
    String lvl_konsumen;
    private  String[] level = {"Pedagang","Pembeli"};

    LayoutInflater inflater ;
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = null;
    String nama,password;

    private ProgressDialog pDialog;
    AlertDialog.Builder ab;
    View dialogView;
    private static String url = "http://us2.tunnel.my.id:1501/android/register.php";
    private static final String tag_success = "success";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_register);

        button1   = (Button)findViewById(R.id.btnReg);
        editText1 = (EditText)findViewById(R.id.etUsername);
        editText2 = (EditText)findViewById(R.id.etPassword);

        spn = (Spinner)findViewById(R.id.spinner2);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, level);
        spn.setAdapter(adapter);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama     = editText1.getText().toString();
                password = editText2.getText().toString();
                lvl_konsumen = spn.getSelectedItem().toString();

                new Isi().execute();
            }
        });


    }

    private void showAlerterr(){

        Register.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ab = new AlertDialog.Builder(Register.this);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.alertstyle, null);
                ab.setView(dialogView);
                ab.setCancelable(false);
                btncancle = (Button)dialogView.findViewById(R.id.btnCancel);
                btncancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                AlertDialog ad = ab.create();
                ad.show();
            }
        });

    }

    private void showAlertfix(){

        Register.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ab = new AlertDialog.Builder(Register.this);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.alertstyloke, null);
                ab.setView(dialogView);
                ab.setCancelable(false);

                btnok = (Button)dialogView.findViewById(R.id.btnBerhasil);
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                AlertDialog ad = ab.create();
                ad.show();
            }
        });

    }

    class Isi extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating Account.....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            List<Pair<String,String>> args = new ArrayList<Pair<String, String>>();

            args.add(new Pair<>("username",nama));
            args.add(new Pair<>("password",lvl_konsumen));

            try {
                jsonObject = jsonParser.makeHttpRequest(url,"POST",args);

            }catch (IOException e){
                Log.d("Networking ", e.getLocalizedMessage());
            }

            Log.d("Create Response",jsonObject.toString());

            try {
                int success = jsonObject.getInt(tag_success);

                if(success == 1){
                    showAlertfix();
                    pDialog.dismiss();
                }else{
                    showAlerterr();
                    pDialog.dismiss();
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
