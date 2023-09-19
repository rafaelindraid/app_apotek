package com.example.apotekmedikafarma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Calendar;
import java.util.HashMap;

public class Detail_Pembelian_Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText edTextNama, editTextId,edTextTanggal, edTextjumlah, edTexthargabeli, edTextsub;
    private Spinner dropdownobat;
    private Button buttonAdd,buttonDel;

    ArrayList<String> obatList = new ArrayList<>();
    ArrayAdapter<String> obatAdapter;
    ArrayList<String> idobat = new ArrayList<>();
    ArrayList<String> namasupp = new ArrayList<>();
    RequestQueue requestQueue;
    DatePickerDialog picker;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembelian);
        Intent intent = getIntent();

        id = intent.getStringExtra(config.BELI_ID);

        editTextId = findViewById(R.id.eTextid);
        edTextNama = findViewById(R.id.eTextSupp);
        edTextTanggal = findViewById(R.id.eTextTanggal);
        edTextjumlah = findViewById(R.id.eTextJumlahobat);
        edTexthargabeli = findViewById(R.id.eTextHargaobat);
        edTextsub = findViewById(R.id.eTextSub);
        requestQueue = Volley.newRequestQueue(this);
        dropdownobat = (Spinner) findViewById(R.id.DropDownObat);

        buttonAdd = findViewById(R.id.buttonUpdateTrans);
        buttonAdd.setOnClickListener(this);
        buttonDel = findViewById(R.id.buttonDel);
        buttonDel.setOnClickListener(this);
        editTextId.setText(id);
        getBeli();

        JsonObjectRequest jsonORobat = new JsonObjectRequest(Request.Method.POST, config.URL_GET_SPINOBAT,
                null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("obat");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.optString("id");
                    String nama = jsonObject.optString("nama_obat");
                    String nama_sup = jsonObject.optString("nama_supp");
                    obatList.add(nama);
                    idobat.add(id);
                    namasupp.add(nama_sup);
                    obatAdapter = new ArrayAdapter<>(Detail_Pembelian_Activity.this,
                            android.R.layout.simple_spinner_item, obatList);
                    obatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdownobat.setAdapter(obatAdapter);

                    edTextjumlah.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(!edTextjumlah.getText().toString().isEmpty() && !edTexthargabeli.getText().toString().isEmpty()) {
                                int jumlah = Integer.parseInt(edTextjumlah.getText().toString());
                                int hargaBeli = Integer.parseInt(edTexthargabeli.getText().toString());
                                edTextsub.setText(Integer.toString(jumlah * hargaBeli));
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // not needed
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // not needed
                        }
                    });

                    edTexthargabeli.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(!edTextjumlah.getText().toString().isEmpty() && !edTexthargabeli.getText().toString().isEmpty()) {
                                int jumlah = Integer.parseInt(edTextjumlah.getText().toString());
                                int hargaBeli = Integer.parseInt(edTexthargabeli.getText().toString());
                                edTextsub.setText(Integer.toString(jumlah * hargaBeli));
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // not needed
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // not needed
                        }
                    });

                    edTextTanggal.setOnClickListener(v -> {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(Detail_Pembelian_Activity.this,
                                (view, year1, monthOfYear, dayOfMonth) -> edTextTanggal.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth), year, month, day);
                        picker.show();
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        dropdownobat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sp = idobat.get(position);
                String snamasup = namasupp.get(position);
                edTextNama.setText(snamasup);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        requestQueue.add(jsonORobat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Detail_Pembelian_Activity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getBeli() {
        class GetBeli extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_Pembelian_Activity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showBeli(s);
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(config.URL_GET_BELI,id);
                return s;
            }
        }
        GetBeli gp = new GetBeli();
        gp.execute();
    }
    private void showBeli(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            //String sid_kodeobat = c.getString("nama_obat");
            //dropdownobat.setSelection(getPosisi(dropdownobat,sid_kodeobat));
            String sjumlah = c.getString("jumlah");
            String stanggal = c.getString("tanggal");
            String sharga = c.getString("harga");
            String stotal = c.getString("subtotal");
            edTextjumlah.setText(sjumlah);
            edTextTanggal.setText(stanggal);
            edTexthargabeli.setText(sharga);
            edTextsub.setText(stotal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private int getPosisi(Spinner dropdownobat, String id_obat) {
        for(int i =0;i<dropdownobat.getCount();i++){
            if(id_obat.equals(dropdownobat.getItemAtPosition(i))){
                return i;
            }
        }
        return 0;
    }
    @Override
    public void onClick(View v) {
        if (v == buttonAdd) {
            UpdateTransBeli();
            startActivity(new Intent(Detail_Pembelian_Activity.this,Transaksi_Pembelian_Activity.class));
        }
        if(v == buttonDel){
            confirmDeleteBeli();

        }
    }

    private void UpdateTransBeli() {
        final String nama_obat = dropdownobat.getSelectedItem().toString();
        final String supp = edTextNama.getText().toString().trim();
        final String tanggal = edTextTanggal.getText().toString().trim();
        final String jumlah = edTextjumlah.getText().toString().trim();
        final String harga = edTexthargabeli.getText().toString().trim();
        final String subtotal = edTextsub.getText().toString().trim();

        class UpdateTransBeli extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_Pembelian_Activity.this,"Updating...","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(config.KEY_BELI_ID,id);
                hashMap.put(config.KEY_BELI_CODE,nama_obat);
                hashMap.put(config.KEY_BELI_SUPP,supp);
                hashMap.put(config.KEY_BELI_TANGGAL,tanggal);
                hashMap.put(config.KEY_BELI_JUMLAH,jumlah);
                hashMap.put(config.KEY_BELI_HARGA,harga);
                hashMap.put(config.KEY_BELI_SUB,subtotal);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(config.URL_UPDATE_BELI,hashMap);
                return s;
            }

        }

        UpdateTransBeli ue = new UpdateTransBeli();
        ue.execute();
    }
    private void deleteBeli(){
        class DeleteBeli extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Detail_Pembelian_Activity.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Detail_Pembelian_Activity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(config.URL_DELETE_BELI, id);
                return s;
            }
        }

        DeleteBeli de = new DeleteBeli();
        de.execute();
    }
    private void confirmDeleteBeli(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Transaksi ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteBeli();
                        startActivity(new Intent(Detail_Pembelian_Activity.this,Transaksi_Pembelian_Activity.class));
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


}
