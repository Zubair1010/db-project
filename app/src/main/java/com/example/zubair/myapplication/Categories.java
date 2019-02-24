package com.example.zubair.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {

    private RecyclerView mCategoryRecyclerView;
    private CategoryAdaptor mAdaptor;
    DataBaseController dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        mCategoryRecyclerView = (RecyclerView) findViewById(R.id.categories_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mCategoryRecyclerView.setLayoutManager(mLayoutManager);
        dbh = DataBaseController.get(this);
        mCategoryRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Category_Items.class);
                startActivity(intent);
            }
        });
        updateUI();
    }

    private  void updateUI(){
        List<String> categories= new ArrayList<String>();
        categories  = dbh.getCategories();
        if(mAdaptor==null) {
            mAdaptor = new CategoryAdaptor(categories);
            mCategoryRecyclerView.setAdapter(mAdaptor);
        }
        else
            mAdaptor.setCategories(categories);
        mAdaptor.notifyDataSetChanged();
    }
}
class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView category_list_name;
    private Context mContext;
    @Override
    public void onClick(View v){

       Intent intent= Category_Items.newIntent(mContext,category_list_name.getText().toString());
        mContext.startActivity(intent);

    }
    public void bindCategory(String Category){
        category_list_name.setText(Category);
    }
    public CategoryHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        mContext=itemView.getContext();
        category_list_name = (TextView) itemView.findViewById(R.id.category_list_text_view);
    }
}
class CategoryAdaptor extends RecyclerView.Adapter<CategoryHolder>{
    private List<String> Categories;
    //Constructor
    public CategoryAdaptor(List<String> categories){
        Categories = categories;
    }
    //Abstract methods
    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.categories_list,parent,false);
        return new CategoryHolder(view);
    }
    @Override
    public void onBindViewHolder(CategoryHolder holder,int position){
        String category = Categories.get(position);
        holder.bindCategory(category);
    }
    @Override
    public int getItemCount(){

        return Categories.size();
    }
    public  void setCategories(List<String> categories){
        Categories = categories;
    }
}

