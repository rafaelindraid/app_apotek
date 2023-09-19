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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;

public class tambahAdminActivity  extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextNama;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonAdd;
    private RadioGroup rdGroupRole;
    private RadioButton rbonterpilih, rbkasir,rbpemilik;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_admin);
        editTextNama = findViewById(R.id.editTextNama);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        rdGroupRole =findViewById(R.id.rgRole);
        rbkasir = findViewById(R.id.rkasir);
        rbpemilik = findViewById(R.id.rpemilik);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(tambahAdminActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            addAdmin();
            startActivity(new Intent(tambahAdminActivity.this, AdminActivity.class));
        }
    }
    private void addAdmin() {
        final String nama = editTextNama.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        int idRoleTerpilih = rdGroupRole.getCheckedRadioButtonId();
        rbonterpilih = findViewById(idRoleTerpilih);
        final String role = rbonterpilih.getText().toString();
        //Toast.makeText(this, ""+role, Toast.LENGTH_SHORT).show();



        class AddPeng extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(tambahAdminActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent i = new Intent(getApplicationContext(), tambahAdminActivity.class);
                startActivity(i);

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();

                params.put(config.KEY_PENG_NAMA,nama);
                params.put(config.KEY_PENG_UNAME,username);
                params.put(config.KEY_PENG_PASS,password);
                params.put(config.KEY_PENG_ROLE,role);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(config.URL_ADD_USER, params);
                return res;
            }
        }
        AddPeng apeng = new AddPeng();
        apeng.execute();


    }
}
