package com.example.apotekmedikafarma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Transaksi_Pembelian_Activity extends AppCompatActivity implements ListView.OnItemClickListener{
    private ListView listView;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pembelian);
        listView = (ListView) findViewById(R.id.listViewPembelian);
        listView.setOnItemClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getJSON();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Transaksi_Pembelian_Activity.this, Tambah_trans_Activity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Transaksi_Pembelian_Activity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showPembelian(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(config.TAG_ID);
                String code_obat = jo.getString(config.TAG_BELI_CODE);
                String nama_supp = jo.getString(config.TAG_BELI_SUPP);
                String harga = jo.getString(config.TAG_BELI_HARGA);
                String tanggal = jo.getString(config.TAG_BELI_TANGGAL);
                String jumlah = jo.getString(config.TAG_BELI_JUMLAH);
                String subtotal = jo.getString(config.TAG_BELI_SUB);

                HashMap<String,String> pembelian = new HashMap<>();
                pembelian.put(config.TAG_ID,id);
                pembelian.put(config.TAG_BELI_CODE,code_obat);
                pembelian.put(config.TAG_BELI_SUPP,nama_supp);
                pembelian.put(config.TAG_BELI_HARGA,harga);
                pembelian.put(config.TAG_BELI_TANGGAL,tanggal);
                pembelian.put(config.TAG_BELI_JUMLAH,jumlah);
                pembelian.put(config.TAG_BELI_SUB,subtotal);
                list.add(pembelian);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                Transaksi_Pembelian_Activity.this, list, R.layout.activity_list_pembelian,
                new String[]{config.TAG_ID,config.TAG_BELI_CODE,config.TAG_BELI_TANGGAL,config.TAG_BELI_SUB},
                new int[]{R.id.id, R.id.code_obat,R.id.tanggal,R.id.subtotal});

        listView.setAdapter(adapter);
    }
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Transaksi_Pembelian_Activity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showPembelian();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(config.URL_GET_ALLBELI);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Detail_Pembelian_Activity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String beliId = map.get(config.TAG_ID);
        intent.putExtra(config.BELI_ID,beliId);
        startActivity(intent);
    }
}