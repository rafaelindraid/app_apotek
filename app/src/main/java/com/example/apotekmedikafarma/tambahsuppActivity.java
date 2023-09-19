package com.example.apotekmedikafarma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class tambahsuppActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edTextNama;
    private EditText editTextAlamat;
    private Button buttonAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_supp);
        edTextNama = findViewById(R.id.eTextNama);
        editTextAlamat = findViewById(R.id.eTextAlamat);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(tambahsuppActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            addAdmin();
        }
    }
    private void addAdmin() {
        final String nama = edTextNama.getText().toString().trim();
        final String alamat = editTextAlamat.getText().toString().trim();

        class AddSupp extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(tambahsuppActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent i = new Intent(getApplicationContext(), tambahsuppActivity.class);
                startActivity(i);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();

                params.put(config.KEY_SUPP_NAMA,nama);
                params.put(config.KEY_SUPP_ALAMAT,alamat);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(config.URL_ADD_SUPPLIER, params);
                return res;
            }
        }
        AddSupp apeng = new AddSupp();
        apeng.execute();


    }
}
