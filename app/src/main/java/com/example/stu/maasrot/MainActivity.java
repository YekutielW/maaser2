package com.example.stu.maasrot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText name , sum;
    private TextView textView;
    private CheckBox checkBox;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.mainImage);



        DBManager.getDbManager(this).createTables(new OnThreadFinish() {
            @Override
            public void onFinish() {
                setTextView();
            }

            @Override
            public void onFinish(ArrayList arrayList) {

            }
        });


        findViewById(R.id.newAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });


        findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
    }


    public void createDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View dialogView = View.inflate(this, R.layout.row, null);
        builder.setView(dialogView);
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        name = dialogView.findViewById(R.id.dialog_action_name);
        sum = dialogView.findViewById(R.id.dialog_txt_sum);
        checkBox = dialogView.findViewById(R.id.checkbox);


        builder.setTitle(R.string.new_action).setIcon(R.drawable.money_icon).setView(dialogView).setCancelable(false);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final AlertDialog dialog = builder.create();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sum.getText().toString().isEmpty()){
                    sum.setError(getString(R.string.enter_sum));
                }else if (name.getText().toString().isEmpty()){
                    name.setError(getString(R.string.enter_name));
                }else {
                saveAction();
                dialog.dismiss();
                }
            }
        });
        dialog.show();



    }
    public void saveAction(){
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Action action = new Action(name.getText().toString(), Integer.valueOf(sum.getText().toString()), checkBox.isChecked(), date);
        action.setTotalSum();
        DBManager.getDbManager(null).insertAction(action, new OnThreadFinish() {
            @Override
            public void onFinish() {
                setTextView();
            }

            @Override
            public void onFinish(ArrayList arrayList) {

            }
        });
    }

    public  void setTextView(){
        int totalSum =  DBManager.getDbManager(null).getLastAction();
        textView.setText("" + totalSum);
        if (totalSum > 0){
            imageView.setImageResource(R.drawable.plus);
        }else if (totalSum < 0){
            imageView.setImageResource(R.drawable.minus);
        }
    }
}




