package com.example.apotekmedikafarma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Detail_admin_Activity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextId;
    private EditText editTextNama;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private RadioGroup rdGroupRole;
    private RadioButton rbonterpilih, rbkasir, rbpemilik;

    private Button buttonUpdate, buttonDelete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_admin);
        Intent intent = getIntent();

        id = intent.getStringExtra(config.PENG_ID);


        editTextId = findViewById(R.id.eTextid);
        editTextNama = findViewById(R.id.editTextNama);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        rdGroupRole = findViewById(R.id.rgRole);
        rbkasir = findViewById(R.id.rkasir);
        rbpemilik = findViewById(R.id.rpemilik);


        buttonUpdate = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDel);
        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        editTextId.setText(id);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getPetugas();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Detail_admin_Activity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getPetugas() {
        class GetPetugas extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_admin_Activity.this, "Fetching...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showPetugas(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(config.URL_GET_USER, id);
                return s;
            }
        }
        GetPetugas gp = new GetPetugas();
        gp.execute();
    }

    private void showPetugas(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String snama_pet = c.getString(config.TAG_NAMA);
            String suname = c.getString(config.TAG_UNAME);
            String spass = c.getString(config.TAG_PASS);
            String srole = c.getString(config.TAG_ROLE);
            if (srole.equals("Kasir")) {
                rbkasir.setChecked(true);
            } else {
                rbpemilik.setChecked(true);
            }
            editTextNama.setText(snama_pet);
            editTextUsername.setText(suname);
            editTextPassword.setText(spass);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updatePetugas() {

        final String unamapeng   = editTextNama.getText().toString().trim();
        final String upass      = editTextPassword.getText().toString().trim();
        final String uusername  = editTextUsername.getText().toString().trim();

        int idRoleTerpilih      = rdGroupRole.getCheckedRadioButtonId();
        rbonterpilih            = findViewById(idRoleTerpilih);
        final String urole      = rbonterpilih.getText().toString();
        Toast.makeText(this, "" + urole, Toast.LENGTH_SHORT).show();

        class UpdatePetugas extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_admin_Activity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(config.KEY_PENG_ID, id);
                hashMap.put(config.KEY_PENG_NAMA, unamapeng);
                hashMap.put(config.KEY_PENG_UNAME, uusername);
                hashMap.put(config.KEY_PENG_PASS, upass);
                hashMap.put(config.KEY_PENG_ROLE, urole);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(config.URL_UPDATE_USER, hashMap);
                return s;
            }

        }

        UpdatePetugas ue = new UpdatePetugas();
        ue.execute();
    }

    private void deletePetugas() {
        class DeletePetugas extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_admin_Activity.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Detail_admin_Activity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(config.URL_DELETE_USER, id);
                return s;
            }
        }

        DeletePetugas de = new DeletePetugas();
        de.execute();
    }

    private void confirmDeletePetugas() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Pegawai ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletePetugas();
                        startActivity(new Intent(Detail_admin_Activity.this, AdminActivity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonUpdate) {
            updatePetugas();
            startActivity(new Intent(Detail_admin_Activity.this, AdminActivity.class));
        }

        if (v == buttonDelete) {
            confirmDeletePetugas();
        }
    }
}
