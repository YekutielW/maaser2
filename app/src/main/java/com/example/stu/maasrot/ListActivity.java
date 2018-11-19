package com.example.stu.maasrot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements MyAdapter.OnItemClick{

    static ArrayList<Action> actions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final MyAdapter myAdapter = new MyAdapter(this);
        myAdapter.senClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(myAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));



        DBManager.getDbManager(null).getHistory(new OnThreadFinish() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onFinish(ArrayList<Action> arrayList) {
                actions = arrayList;
                myAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
    }
}
