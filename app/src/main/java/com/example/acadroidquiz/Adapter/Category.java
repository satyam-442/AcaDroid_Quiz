package com.example.acadroidquiz.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.acadroidquiz.CategoriesActivity;
import com.example.acadroidquiz.Modal.CategoryModel;
import com.example.acadroidquiz.R;
import com.example.acadroidquiz.SetsActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Category extends RecyclerView.Adapter<Category.viewHolder> {

    private List<CategoryModel> categoryList;

    public Category(List<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(categoryList.get(position).getUrll(),categoryList.get(position).getNamee(), position);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView title;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
        }

        private void setData(String url, final String title, final int position){
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent categories = new Intent(itemView.getContext(), SetsActivity.class);
                    categories.putExtra("title", title);
                    categories.putExtra("position", position);
                    imageView.getContext().startActivity(categories);
                }
            });
        }

    }

}
