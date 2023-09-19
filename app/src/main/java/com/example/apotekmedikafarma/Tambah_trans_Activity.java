package com.example.apotekmedikafarma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

public class Tambah_trans_Activity extends AppCompatActivity implements View.OnClickListener{
    private EditText edTextNama,edTextTanggal,edTextjumlah,edTexthargabeli,edTextsub;
    private Spinner dropdownobat;
    private Button buttonAdd;
    ArrayList<String> obatList = new ArrayList<>();
    ArrayAdapter<String> obatAdapter;
    ArrayList<String> idobat = new ArrayList<>();
    ArrayList<String> namasupp = new ArrayList<>();
    RequestQueue requestQueue;
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_trans);

        edTextNama = findViewById(R.id.eTextSupp);
        edTextTanggal = findViewById(R.id.eTextTanggal);
        edTextjumlah = findViewById(R.id.eTextJumlahobat);
        edTexthargabeli = findViewById(R.id.eTextHargaobat);
        edTextsub = findViewById(R.id.eTextSub);
        requestQueue = Volley.newRequestQueue(this);
        dropdownobat = (Spinner) findViewById(R.id.DropDownObat);

        buttonAdd = findViewById(R.id.buttonAddTrans);
        buttonAdd.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    obatAdapter = new ArrayAdapter<>(Tambah_trans_Activity.this,
                            android.R.layout.simple_spinner_item, obatList);
                    obatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdownobat.setAdapter(obatAdapter);

                    edTextTanggal.setOnClickListener(v -> {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(Tambah_trans_Activity.this,
                                (view ,year1,monthOfYear,dayOfMonth) -> edTextTanggal.setText(year1+ "-"+(monthOfYear + 1)+"-"+dayOfMonth ), year, month, day);
                        picker.show();
                    });
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        dropdownobat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sp =idobat.get(position);
                String snamasup = namasupp.get(position);
                edTextNama.setText(snamasup);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        requestQueue.add(jsonORobat);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Tambah_trans_Activity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            addTransBeli();
        }
    }
    private void addTransBeli() {
        final String nama_obat = dropdownobat.getSelectedItem().toString();
        final String supp = edTextNama.getText().toString().trim();
        final String tanggal = edTextTanggal.getText().toString().trim();
        final String jumlah = edTextjumlah.getText().toString().trim();
        final String harga = edTexthargabeli.getText().toString().trim();
        final String subtotal = edTextsub.getText().toString().trim();

        Toast.makeText(this, ""+subtotal, Toast.LENGTH_SHORT).show();
        class AddSupp extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Tambah_trans_Activity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent i = new Intent(getApplicationContext(), Transaksi_Pembelian_Activity.class);
                startActivity(i);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(config.KEY_BELI_CODE,nama_obat);
                params.put(config.KEY_BELI_SUPP,supp);
                params.put(config.KEY_BELI_TANGGAL,tanggal);
                params.put(config.KEY_BELI_JUMLAH,jumlah);
                params.put(config.KEY_BELI_HARGA,harga);
                params.put(config.KEY_BELI_SUB,subtotal);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(config.URL_ADD_BELI, params);
                return res;
            }
        }
        AddSupp apeng = new AddSupp();
        apeng.execute();

    }
}