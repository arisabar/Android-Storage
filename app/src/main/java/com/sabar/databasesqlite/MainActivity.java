package com.sabar.databasesqlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sabar.databasesqlite.sql.sql;

public class MainActivity extends AppCompatActivity {

    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    sql dbHelper;
    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma = this;
        dbHelper = new sql(this);
        RefreshList();
    }

    public void RefreshList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM buku", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        ListView01 = (ListView) findViewById(R.id.ListView01);
        ListView01.setAdapter(new ArrayAdapter<Object>(this,
                android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selection = daftar[position];
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilih ?");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent i = new Intent(getApplicationContext(), Edit.class);
                                i.putExtra("judul_buku", selection);
                                startActivity(i);
                                break;
                            case 1:
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.execSQL("delete from buku where judul_buku ='" + selection + "'");
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            this.menu = menu;
            menu.add(0, 1, 0, "Tambah").setIcon(android.R.drawable.btn_plus);
            menu.add(0, 2, 0,
                    "Refresh").setIcon(android.R.drawable.ic_menu_rotate);
            menu.add(0, 3, 0,
                    "Exit").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case 1:
                    Intent i = new Intent(MainActivity.this, Add.class);
                    startActivity(i);
                    return true;
                case 2:
                    RefreshList();
                    return true;
                case 3:
                    finish();
                    return true;
            }
            return false;
    }
}
