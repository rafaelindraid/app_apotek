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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Detail_obat_Activity extends AppCompatActivity  implements View.OnClickListener{
    private EditText editTextId,edTextNama,editTextKode,editTextJumlah,editTextHarga;
    private Spinner dropdownsupp;
    private Button buttonAdd;
    private Button buttonDel;
    ArrayList<String> suppList = new ArrayList<>();
    ArrayAdapter<String> suppAdapter;
    ArrayList<String> idsupp = new ArrayList<>();
    RequestQueue requestQueue;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_obat);
        Intent intent = getIntent();

        id = intent.getStringExtra(config.OBAT_ID);

        editTextId = findViewById(R.id.eTextid);
        edTextNama = findViewById(R.id.eTextNamaObat);
        editTextKode = findViewById(R.id.eTextKodeobat);
        editTextJumlah = findViewById(R.id.eTextJumlahobat);
        editTextHarga = findViewById(R.id.eTextHargaobat);
        requestQueue = Volley.newRequestQueue(this);
        dropdownsupp = (Spinner) findViewById(R.id.DropDownSupp);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDel = findViewById(R.id.buttonDel);
        buttonAdd.setOnClickListener(this);
        buttonDel.setOnClickListener(this);

        JsonObjectRequest jsonORobat = new JsonObjectRequest(Request.Method.POST, config.URL_GET_SPSUPP,
                null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("supplier");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.optString("id");
                    String nama = jsonObject.optString("nama");
                    suppList.add(nama);
                    idsupp.add(id);
                    suppAdapter = new ArrayAdapter<>(Detail_obat_Activity.this,
                            android.R.layout.simple_spinner_item, suppList);
                    suppAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdownsupp.setAdapter(suppAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        requestQueue.add(jsonORobat);

        editTextId.setText(id);
        getObat();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Detail_obat_Activity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private int getPosisi(Spinner dropdownsupp, String id_supp) {
        for(int i =0;i<dropdownsupp.getCount();i++){
            if(id_supp.equals(dropdownsupp.getItemAtPosition(i))){
                return i;
            }
        }
        return 0;
    }

    private void getObat() {
        class GetObat extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_obat_Activity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showObat(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(config.URL_GET_OBAT,id);
                return s;
            }
        }
        GetObat gp = new  GetObat();
        gp.execute();
    }
    private void showObat(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String snama = c.getString(config.TAG_NAMA_OBAT);
            String skode = c.getString(config.TAG_KODE_OBAT);
            String sid_sup = c.getString(config.TAG_NAMA_SUPP);
            dropdownsupp.setSelection(getPosisi(dropdownsupp,sid_sup));

            String sjumlah = c.getString("jumlah_obat");
            String sharga = c.getString("harga_obat");
            edTextNama.setText(snama);
            editTextKode.setText(skode);
            editTextJumlah.setText(sjumlah);
            editTextHarga.setText(sharga);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateObat(){
        final String ukode = editTextKode.getText().toString().trim();
        final String uid_supp = dropdownsupp.getSelectedItem().toString();
        final String unama_obat = edTextNama.getText().toString().trim();
        final String ujumlah = editTextJumlah.getText().toString().trim();
        final String uharga = editTextHarga.getText().toString().trim();

        class UpdateObat extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_obat_Activity.this,"Updating...","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(config.KEY_OBAT_ID,id);
                hashMap.put(config.KEY_OBAT_KODE,ukode);
                hashMap.put(config.KEY_OBAT_NAMA,unama_obat);
                hashMap.put(config.KEY_OBAT_SUPP,uid_supp);
                hashMap.put(config.KEY_OBAT_JUMLAH,ujumlah);
                hashMap.put(config.KEY_OBAT_HARGA,uharga);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(config.URL_UPDATE_OBAT,hashMap);
                return s;
            }

        }

        UpdateObat ue = new UpdateObat();
        ue.execute();

    }
    private void deleteObat(){
        class DeleteObat extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_obat_Activity.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Detail_obat_Activity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(config.URL_DELETE_OBAT, id);
                return s;
            }
        }

        DeleteObat de = new DeleteObat();
        de.execute();
    }
    private void confirmDeleteObat(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Obat ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteObat();
                        startActivity(new Intent(Detail_obat_Activity.this,ObatActivity.class));
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
            updateObat();
            startActivity(new Intent(Detail_obat_Activity.this,ObatActivity.class));
        }

        if(v == buttonDel){
            confirmDeleteObat();

        }
    }
}