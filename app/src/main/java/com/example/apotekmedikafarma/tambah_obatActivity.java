package com.example.apotekmedikafarma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class tambah_obatActivity extends AppCompatActivity implements View.OnClickListener {
        private EditText edTextKode,edTextNama,edTextjumlah,edTextharga;
        private Spinner dropdownsupp;
        private Button buttonAdd;
        ArrayList<String> suppList = new ArrayList<>();
        ArrayAdapter<String> suppAdapter;
        ArrayList<String> idsupp = new ArrayList<>();
        ArrayList<String> namasupp = new ArrayList<>();
        RequestQueue requestQueue;
@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_obat);
        edTextKode = findViewById(R.id.eTextKodeobat);
        edTextNama = findViewById(R.id.eTextNamaObat);
        edTextjumlah = findViewById(R.id.eTextJumlahobat);
        edTextharga = findViewById(R.id.eTextHargaobat);
        requestQueue = Volley.newRequestQueue(this);
        dropdownsupp = (Spinner) findViewById(R.id.DropDownSupp);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    JsonObjectRequest jsonORsupplier = new JsonObjectRequest(Request.Method.POST, config.URL_GET_SPSUPP,
            null, response -> {
        try {
            JSONArray jsonArray = response.getJSONArray("supplier");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String nama = jsonObject.optString("nama");
                suppList.add(nama);
                idsupp.add(id);
                suppAdapter = new ArrayAdapter<>(tambah_obatActivity.this,
                        android.R.layout.simple_spinner_item, suppList);
                suppAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdownsupp.setAdapter(suppAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }, error -> {
    });
    requestQueue.add(jsonORsupplier);
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(tambah_obatActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

@Override
public void onClick(View v) {
        if(v == buttonAdd){
        addObat();
        }
}
private void addObat() {
    final String kode = edTextKode.getText().toString().trim();
    final String id_supp = dropdownsupp.getSelectedItem().toString();
    final String nama_obat = edTextNama.getText().toString().trim();
    final String jumlah = edTextjumlah.getText().toString().trim();
    final String harga = edTextharga.getText().toString().trim();

class AddSupp extends AsyncTask<Void, Void, String> {

    ProgressDialog loading;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(tambah_obatActivity.this,"Menambahkan...","Tunggu...",false,false);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        loading.dismiss();
        Intent i = new Intent(getApplicationContext(), ObatActivity.class);
        startActivity(i);
    }

    @Override
    protected String doInBackground(Void... v) {
        HashMap<String, String> params = new HashMap<>();

        params.put(config.KEY_OBAT_KODE,kode);
        params.put(config.KEY_OBAT_NAMA,nama_obat);
        params.put(config.KEY_OBAT_SUPP,id_supp);
        params.put(config.KEY_OBAT_JUMLAH,jumlah);
        params.put(config.KEY_OBAT_HARGA,harga);

        RequestHandler rh = new RequestHandler();
        String res = rh.sendPostRequest(config.URL_ADD_OBAT, params);
        return res;
    }
}
    AddSupp apeng = new AddSupp();
        apeng.execute();


    }
}
