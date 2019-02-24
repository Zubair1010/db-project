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
import java.util.UUID;

public class Category_Items extends AppCompatActivity {


    private RecyclerView mcomponentRecyclerView;
    private componentAdaptor mAdaptor;
    DataBaseController dbh;
    private String Category_name;

    public static final String Category_EXTRA = "com.example.zubair.MyApplication.ID";

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category__items);
        dbh = DataBaseController.get(this);

        Category_name = (String) getIntent().getSerializableExtra(Category_EXTRA);

        mcomponentRecyclerView = (RecyclerView) findViewById(R.id.assets_component_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mcomponentRecyclerView.setLayoutManager(mLayoutManager);
        updateUI();

    }
    public static Intent newIntent(Context packageContext, String category){
        Intent intent = new Intent(packageContext,Category_Items.class);
        intent.putExtra(Category_EXTRA,category);
        return intent;
    }
    private  void updateUI(){
        List<Asset> assets = new ArrayList<Asset>();
        assets  = dbh.getAssetsForCategory(this.Category_name);

        if(assets==null) {
            assets = new ArrayList<Asset>();
            assets.add(new Asset("example", "example", "example", "example", "example", "example", "example", "example"));
        }
        if(mAdaptor==null) {
            mAdaptor = new componentAdaptor(assets);
            mcomponentRecyclerView.setAdapter(mAdaptor);
        }
        else
            mAdaptor.setCrimes(assets);
        mAdaptor.notifyDataSetChanged();
    }
}
class componentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Asset mAsset;
    private TextView mAsset_Name;
    private ImageView pictureBox;
    private Context mContext;
    private TextView last_update_date, category_name, location_name,quantity ;
    @Override
    public void onClick(View v){
        Intent intent=ShowComponent.newIntent(mContext,mAsset.getCategory().toString(),mAsset.getId().toString());
        mContext.startActivity(intent);

    }
    public void bindAsset(Asset asset){
        mAsset = asset;
        mAsset_Name.setText(mAsset.getItem_Name());
        last_update_date.setText(mAsset.getDate().toString());
        category_name.setText(mAsset.getCategory());
        location_name.setText(mAsset.getLocation());
        quantity.setText(mAsset.getQuantity());
        //pictureBox.setImageBitmap();
    }
    public componentHolder(View itemView){

        super(itemView);
        itemView.setOnClickListener(this);
        mContext=itemView.getContext();
        mAsset_Name = (TextView) itemView.findViewById(R.id.asset_name);
        location_name = (TextView) itemView.findViewById(R.id.location_name);
        category_name = (TextView) itemView.findViewById(R.id.category_name);
        quantity = (TextView) itemView.findViewById(R.id.quantity_number) ;
        last_update_date = (TextView) itemView.findViewById(R.id.last_update_date);
        pictureBox = (ImageView) itemView.findViewById(R.id.image_view_icon);

    }


}
class componentAdaptor extends RecyclerView.Adapter<componentHolder>{
    private List<Asset> mAssets;
    //Constructor
    public componentAdaptor(List<Asset> assets){
        mAssets = assets;
    }
    //Abstract methods
    @Override
    public componentHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.asset_list,parent,false);
        return new componentHolder(view);
    }
    @Override
    public void onBindViewHolder(componentHolder holder,int position){
        Asset asset = mAssets.get(position);
        holder.bindAsset(asset);
    }
    @Override
    public int getItemCount(){

        return mAssets.size();
    }
    public  void setCrimes(List<Asset> assets){
        mAssets = assets;
    }
}

