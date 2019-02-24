package com.example.zubair.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String UserName = "com.example.zubair.MyApplication.UserName";
    private RecyclerView mAssetRecyclerView;
    private AssetAdaptor mAdaptor;
    TextView userName,userEmail;
    DataBaseController dbh;
    boolean check = false;
    String User_Role;
    public static Activity mActivity;

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);

        String extraUserName = (String) getIntent().getSerializableExtra(UserName);
        userName = (TextView) hView.findViewById(R.id.User_name);
        userEmail = (TextView) hView.findViewById(R.id.User_Email);


        dbh = DataBaseController.get(this);
        Cursor cursor = dbh.getUserInfo(extraUserName);
        if (cursor != null) {
            cursor.moveToFirst();
         userName.setText(cursor.getString(0));
           userEmail.setText(cursor.getString(2));
            User_Role = cursor.getString(5);
            if(User_Role.equalsIgnoreCase("Manager")){
                check=true;
            }
            cursor.close();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


            mAssetRecyclerView = (RecyclerView) findViewById(R.id.assets_recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mAssetRecyclerView.setLayoutManager(mLayoutManager);

            updateUI();
        }
    }

    private  void updateUI(){
        List<Asset> assets = new ArrayList<Asset>();
             assets  = dbh.getAssetsForMain();
        if(assets==null) {
            assets = new ArrayList<Asset>();
            assets.add(new Asset("example", "example", "example", "example", "example", "example", "example", "example"));
        }
        if(mAdaptor==null) {
            mAdaptor = new AssetAdaptor(assets);
            mAssetRecyclerView.setAdapter(mAdaptor);
        }
        else
            mAdaptor.setCrimes(assets);
        mAdaptor.notifyDataSetChanged();
    }

    public static Intent newIntent(Context packageContext, String name){
        Intent intent = new Intent(packageContext,MainActivity.class);
        intent.putExtra(UserName,name);
        return intent;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.addComponent) {
            Intent addComponent=new Intent(getApplicationContext(),AddComponent.class);
            startActivity(addComponent);
        } else if (id == R.id.addCategory) {
            Intent addCategory=new Intent(getApplicationContext(),AddCategory.class);
            startActivity(addCategory);


        } else if (id == R.id.search_component_by_category) {
            Intent SearchByCategory=new Intent(getApplicationContext(),Categories.class);
            startActivity(SearchByCategory);

        }
        else if(id == R.id.User_Setting){
            if(check) {
                Intent intent = new Intent(getApplicationContext(), Manage_Users.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"You donot have Enough Rights..!!",Toast.LENGTH_SHORT).show();
            }
        }
        else if(id==R.id.exit){
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
class AssetHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Asset mAsset;
    private TextView mAsset_Name;
    private File mPhotoFile;
    private ImageView pictureBox;
    private Context mContext;
    private TextView last_update_date, category_name, location_name,quantity ;
    private CheckBox mSolvedCheckBox;
    @Override
    public void onClick(View v){
        Intent intent = ShowComponent.newIntent(mContext,mAsset.getCategory(),mAsset.getId());
        mContext.startActivity(intent);
    }
    public void bindAsset(Asset asset){
        mAsset = asset;
        mAsset_Name.setText(mAsset.getItem_Name());
        last_update_date.setText(mAsset.getDate().toString());
        category_name.setText(mAsset.getCategory());
        location_name.setText(mAsset.getLocation());
        quantity.setText(mAsset.getQuantity());
        mPhotoFile = getPhotoFile(mAsset.getPhoto_file_path());
        updatePhotoView();
        //pictureBox.setImageBitmap();
    }
    public AssetHolder(View itemView){
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
    public void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            //  Toast.makeText(getApplicationContext(),"No image Found",Toast.LENGTH_SHORT).show();
            pictureBox.setImageDrawable(null);
        } else {
           // Toast.makeText(mContext.getApplicationContext(),"image Found but not showing",Toast.LENGTH_SHORT).show();
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),MainActivity.mActivity);
            //   Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());

            pictureBox.setImageBitmap(bitmap);
        }
    }
    public File getPhotoFile(String path){
        File externalFilesDir = mContext.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null)  //just to check k external storage hai k nahi..agar nahi hai tw null return karega.
            return  null;

        return new File(externalFilesDir,path);
    }

}
class AssetAdaptor extends RecyclerView.Adapter<AssetHolder>{
    private List<Asset> mAssets;
    //Constructor
    public AssetAdaptor(List<Asset> assets){
        mAssets = assets;
    }
    //Abstract methods
    @Override
    public AssetHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.asset_list,parent,false);
        return new AssetHolder(view);
    }
    @Override
    public void onBindViewHolder(AssetHolder holder,int position){
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



