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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Detail_Supplier_Activity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextId,edTextNama,editTextAlamat;

    private Button buttonAdd;
    private Button buttonDel;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_supplier);
        Intent intent = getIntent();

        id = intent.getStringExtra(config.SUPP_ID);

        editTextId = findViewById(R.id.eTextid);
        edTextNama = findViewById(R.id.eTextNama);
        editTextAlamat = findViewById(R.id.eTextAlamat);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDel = findViewById(R.id.buttonDel);

        buttonAdd.setOnClickListener(this);
        buttonDel.setOnClickListener(this);

        editTextId.setText(id);
        getSupplier();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Detail_Supplier_Activity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSupplier() {
        class GetSupp extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_Supplier_Activity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showSupplier(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(config.URL_GET_SUPPLIER,id);
                return s;
            }
        }
        GetSupp gp = new  GetSupp();
        gp.execute();
    }
    private void showSupplier(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String snama = c.getString(config.TAG_NAMA);
            String salamat = c.getString(config.TAG_ALMTSUPP);
            edTextNama.setText(snama);
            editTextAlamat.setText(salamat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateSupplier(){

        final String unama = edTextNama.getText().toString().trim();
        final String ualamat = editTextAlamat.getText().toString().trim();

        class UpdateSupplier extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_Supplier_Activity.this,"Updating...","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(config.KEY_SUPP_ID,id);
                hashMap.put(config.KEY_SUPP_NAMA,unama);
                hashMap.put(config.KEY_SUPP_ALAMAT,ualamat);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(config.URL_UPDATE_SUPPLIER,hashMap);
                return s;
            }

        }

        UpdateSupplier ue = new UpdateSupplier();
        ue.execute();
    }
    private void deleteSupplier(){
        class DeleteSupplier extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_Supplier_Activity.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Detail_Supplier_Activity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(config.URL_DELETE_SUPPLIER, id);
                return s;
            }
        }

        DeleteSupplier de = new DeleteSupplier();
        de.execute();
    }
    private void confirmDeleteSupplier(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Supplier ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteSupplier();
                        startActivity(new Intent(Detail_Supplier_Activity.this,SupplierActivity.class));
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
        if(v == buttonAdd){
            updateSupplier();
            startActivity(new Intent(Detail_Supplier_Activity.this,SupplierActivity.class));
        }

        if(v == buttonDel){
            confirmDeleteSupplier();
        }
    }
}