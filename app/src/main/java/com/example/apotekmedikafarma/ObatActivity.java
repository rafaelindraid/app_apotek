package com.example.apotekmedikafarma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ObatActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private ListView listView;

    private String JSON_STRING;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obat);

        listView = (ListView) findViewById(R.id.listViewObat);
        listView.setOnItemClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getJSON();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObatActivity.this, tambah_obatActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ObatActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showObat() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(config.TAG_ID);
                String nama = jo.getString(config.TAG_NAMA_OBAT);
                String kode = jo.getString(config.TAG_KODE_OBAT);
                String jumlah_obat = jo.getString("jumlah_obat");

                HashMap<String, String> obat = new HashMap<>();
                obat.put(config.TAG_ID, id);
                obat.put(config.TAG_NAMA_OBAT, nama);
                obat.put(config.TAG_KODE_OBAT, kode);
                obat.put(jumlah_obat, jumlah_obat);
                list.add(obat);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ObatActivity.this, list, R.layout.activity_list_obat,
                new String[]{config.TAG_ID, config.TAG_NAMA_OBAT, config.TAG_KODE_OBAT, config.TAG_JUMLAH_OBAT},
                new int[]{R.id.id,  R.id.kodeobat,R.id.namaobat,R.id.jumlahobat});

        listView.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ObatActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showObat();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(config.URL_GET_ALLOBAT);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Detail_obat_Activity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String obatId = map.get(config.TAG_ID);
        intent.putExtra(config.OBAT_ID, obatId);
        startActivity(intent);
    }
}
