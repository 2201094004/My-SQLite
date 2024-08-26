package com.syaviraindahmaryam.mysqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvFavorit;
    FloatingActionButton fabTambah;
    DatabaseHandler databaseHandler;
    List<String> listFavorit;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvFavorit = findViewById(R.id.lv_favorit);
        fabTambah = findViewById(R.id.fab_Tambah);
        databaseHandler = new DatabaseHandler(this);

        fabTambah.setOnClickListener(v -> bukaDialogTambah());

        // Method untuk tampil data
        tampilkanSemuaData();

        // Ketika item pada ListView diklik
        lvFavorit.setOnItemClickListener((adapterView, view, i, l) -> {
            String data = listFavorit.get(i);

            // Kirim data ke dialogUpdate
            bukaDialogUpdate(data);
        });
    }

    private void bukaDialogUpdate(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update atau Hapus Menu Favorit");
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.from_update, null);

        builder.setView(dialogView);

        EditText etNama = dialogView.findViewById(R.id.et_nama);
        Button btnUpdate = dialogView.findViewById(R.id.button_update);
        Button btnDelete = dialogView.findViewById(R.id.button_delete);

        etNama.setText(data);

        AlertDialog dialog = builder.create();

        btnUpdate.setOnClickListener(v -> {
            if (etNama.getText().toString().trim().isEmpty()) {
                etNama.setError("Nama Harus Diisi");
                return;
            }
            updateData(data, etNama.getText().toString());
            dialog.dismiss();

            tampilkanSemuaData();
        });

        btnDelete.setOnClickListener(v -> {
            hapusData(data);
            dialog.dismiss();

            tampilkanSemuaData();
        });

        dialog.show();
    }

    private void tampilkanSemuaData() {
        // Dapatkan semua data
        listFavorit = databaseHandler.tampilSemua();

        // Siapkan adapter
        adapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                listFavorit
        );

        // Hubungkan adapter dengan ListView
        lvFavorit.setAdapter(adapter);
    }

    private void bukaDialogTambah() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Menu Favorit");
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.from_tambah, null);

        builder.setView(dialogView);

        EditText etNama = dialogView.findViewById(R.id.et_nama);
        Button btnSimpan = dialogView.findViewById(R.id.button_tambah);

        AlertDialog dialog = builder.create();
        btnSimpan.setOnClickListener(v -> {
            if (etNama.getText().toString().trim().isEmpty()) {
                etNama.setError("Nama Harus Diisi");
                return;
            }
            simpanData(etNama.getText().toString());
            dialog.dismiss();

            tampilkanSemuaData();
        });

        dialog.show();
    }

    private void simpanData(String nama) {
        databaseHandler.simpan(nama);
    }

    private void updateData(String oldName, String newName) {
        databaseHandler.update(oldName, newName);
    }

    private void hapusData(String nama) {
        databaseHandler.delete(nama);
    }
}
