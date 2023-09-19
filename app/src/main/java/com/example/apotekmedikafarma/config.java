package com.example.apotekmedikafarma;

public class config {

    public static final String URL_LOGIN="http://10.0.2.2:8080/medika/login.php";


    public static final String URL_ADD_SUPPLIER="http://10.0.2.2:8080/medika/tambahSup.php";
    public static final String URL_GET_ALLSUPPLIER = "http://10.0.2.2:8080/medika/tampilSemuaSup.php";
    public static final String URL_GET_SUPPLIER = "http://10.0.2.2:8080/medika/tampilSup.php?id=";
    public static final String URL_UPDATE_SUPPLIER = "http://10.0.2.2:8080/medika/updateSup.php";
    public static final String URL_DELETE_SUPPLIER = "http://10.0.2.2:8080/medika/hapusSup.php?id=";

    public static final String URL_ADD_OBAT     = "http://10.0.2.2:8080/medika/tambahObat.php";
    public static final String URL_GET_ALLOBAT  = "http://10.0.2.2:8080/medika/tampilSemuaObat.php";
    public static final String URL_GET_OBAT     = "http://10.0.2.2:8080/medika/tampilObat.php?id=";
    public static final String URL_UPDATE_OBAT  = "http://10.0.2.2:8080/medika/updateObat.php";
    public static final String URL_DELETE_OBAT  = "http://10.0.2.2:8080/medika/hapusObat.php?id=";

    public static final String URL_ADD_USER         = "http://10.0.2.2:8080/medika/tambahUsr.php";
    public static final String URL_GET_ALLUSER      = "http://10.0.2.2:8080/medika/tampilSemuaUsr.php";
    public static final String URL_GET_USER         = "http://10.0.2.2:8080/medika/tampilUsr.php?id=";
    public static final String URL_UPDATE_USER      = "http://10.0.2.2:8080/medika/updateUsr.php";
    public static final String URL_DELETE_USER      = "http://10.0.2.2:8080/medika/hapusUsr.php?id=";

    public static final String URL_GET_SPSUPP           = "http://10.0.2.2:8080/medika/getSpinsupp.php";
    public static final String URL_GET_SPINOBAT         = "http://10.0.2.2:8080/medika/getSpinobat.php";

    public static final String URL_ADD_BELI         = "http://10.0.2.2:8080/medika/tambahBeli.php";
    public static final String URL_GET_ALLBELI      = "http://10.0.2.2:8080/medika/tampilSemuaBeli.php";
    public static final String URL_GET_BELI         = "http://10.0.2.2:8080/medika/tampilBeli.php?id=";
    public static final String URL_UPDATE_BELI      = "http://10.0.2.2:8080/medika/updateBeli.php";
    public static final String URL_DELETE_BELI      = "http://10.0.2.2:8080/medika/hapusBeli.php?id=";


    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_OBAT_ID = "id";
    public static final String KEY_OBAT_KODE = "code_obat";
    public static final String KEY_OBAT_NAMA = "nama_obat";
    public static final String KEY_OBAT_SUPP = "nama_supp";
    public static final String KEY_OBAT_JUMLAH = "jumlah_obat";
    public static final String KEY_OBAT_HARGA = "harga_obat";


    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_KODE_OBAT = "code_obat";
    public static final String TAG_NAMA_OBAT = "nama_obat";
    public static final String TAG_NAMA_SUPP = "nama_supp";
    public static final String TAG_JUMLAH_OBAT = "jumlah";
    public static final String TAG_HARGA_OBAT = "harga_obat";


    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_SUPP_ID = "id";
    public static final String KEY_SUPP_NAMA = "nama";
    public static final String KEY_SUPP_ALAMAT = "alamat";

    //JSON Tags
    public static final String TAG_IDSUPP = "id";
    public static final String TAG_NAMASUPP = "nama";
    public static final String TAG_ALMTSUPP = "alamat";
    //tabel Pelanggan Selesai


    //tabel Pengguna
    public static final String KEY_PENG_ID = "id";
    public static final String KEY_PENG_NAMA = "nama";
    public static final String KEY_PENG_UNAME = "username";
    public static final String KEY_PENG_PASS = "password";
    public static final String KEY_PENG_ROLE = "role";
    //JSON Tags
    public static final String TAG_NAMA = "nama";
    public static final String TAG_UNAME = "username";
    public static final String TAG_PASS = "password";
    public static final String TAG_ROLE = "role";

    public static final String KEY_BELI_ID = "id";
    public static final String KEY_BELI_CODE = "nama_obat";
    public static final String KEY_BELI_SUPP = "nama_supp";
    public static final String KEY_BELI_HARGA = "harga";
    public static final String KEY_BELI_TANGGAL = "tanggal";
    public static final String KEY_BELI_JUMLAH = "jumlah";
    public static final String KEY_BELI_SUB = "subtotal";

    public static final String TAG_BELI_CODE = "nama_obat";
    public static final String TAG_BELI_SUPP = "nama_supp";
    public static final String TAG_BELI_HARGA = "harga";
    public static final String TAG_BELI_TANGGAL = "tanggal";
    public static final String TAG_BELI_JUMLAH = "jumlah";
    public static final String TAG_BELI_SUB = "subtotal";

    public static final String PENG_ID = "peng_id";
    public static final String SUPP_ID = "supp_id";
    public static final String OBAT_ID = "obat_id";
    public static final String BELI_ID = "beli_id";

}

