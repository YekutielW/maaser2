package com.example.stu.maasrot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MyAdapter extends RecyclerView.Adapter{
    Context context;
    private OnItemClick itemClickListener;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = View.inflate(context, R.layout.recycler_row, null);
        return new MyHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder)holder).updateRow(position);
    }

    @Override
    public int getItemCount() {
        return ListActivity.actions.size();
    }

    void senClickListener(OnItemClick onItemClick){
        this.itemClickListener = onItemClick;
    }
    public interface OnItemClick{
        void onItemClick(View view, int position);
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView TextViewName, sum, totalSum, date;
        ImageView imageView;
        int position;

        public MyHolder(View itemView) {
            super(itemView);
            TextViewName = itemView.findViewById(R.id.TextViewName);
            sum = itemView.findViewById(R.id.sum);
            totalSum = itemView.findViewById(R.id.totalSum);
            imageView = itemView.findViewById(R.id.image);
            date = itemView.findViewById(R.id.date);
        }

        public void updateRow(int position){
            this.position = position;
            date.setText(ListActivity.actions.get(position).date);
            TextViewName.setText(ListActivity.actions.get(position).actionName);
            totalSum.setText("" + ListActivity.actions.get(position).totalSum);

            if (ListActivity.actions.get(position).plus){
            sum.setText("" + ListActivity.actions.get(position).actionSum);
                imageView.setImageResource(R.drawable.plus);
            }else {
            sum.setText("" + ListActivity.actions.get(position).actionSum + "-");
                imageView.setImageResource(R.drawable.minus);
            }
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(context, "kkkk", Toast.LENGTH_SHORT).show();
//            if (itemClickListener != null) itemClickListener.onItemClick(view, position);
        }
    }
}
