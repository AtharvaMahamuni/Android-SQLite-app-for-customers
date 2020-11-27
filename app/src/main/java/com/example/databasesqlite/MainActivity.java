package com.example.databasesqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //reference to the buttons and other controls on the layout
    Button btn_viewAll, btn_add;
    EditText et_name, et_age;
    Switch switch_active;
    ListView lv_customerList;

    ArrayAdapter<CustomerModel> customerModelArrayAdapter;
    List<CustomerModel> everyone;
    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning actual values to the variables.
        btn_viewAll = findViewById(R.id.btn_viewAll);
        btn_add = findViewById(R.id.btn_add);
        et_name = findViewById(R.id.editText_name);
        et_age = findViewById(R.id.editText_age);
        switch_active = findViewById(R.id.activeCustomer);
        lv_customerList = findViewById(R.id.listView);

        ShowCustomerListView(databaseHelper);

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                ShowCustomerListView(databaseHelper);

//                Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerModel customerModel;
                try {
                    customerModel = new CustomerModel(-1, et_name.getText().toString(), Integer.parseInt(et_age.getText().toString()), switch_active.isChecked());
                    //Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1, "ERROR", 0, false);
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                boolean success = databaseHelper.addOne(customerModel);

                if(success == true)
                    Toast.makeText(MainActivity.this, "data added successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                ShowCustomerListView(databaseHelper);
            }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                databaseHelper.deleteOne(clickedCustomer);
                ShowCustomerListView(databaseHelper);
                Toast.makeText(MainActivity.this, "Deleted "+clickedCustomer.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ShowCustomerListView(DatabaseHelper databaseHelper2) {
        customerModelArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, databaseHelper2.getEveryone());
        lv_customerList.setAdapter(customerModelArrayAdapter);
    }
}