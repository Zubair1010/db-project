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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Manage_Users extends AppCompatActivity {
    private RecyclerView mManage_User_RecyclerView;
    private UserAdaptor mAdaptor;
    DataBaseController dbh;
    Button AddUser;

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__users);
        dbh = DataBaseController.get(getApplicationContext());
        mManage_User_RecyclerView = (RecyclerView) findViewById(R.id.manage_user_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mManage_User_RecyclerView.setLayoutManager(mLayoutManager);
        updateUI();
        AddUser = (Button) findViewById(R.id.manage_user_add_user_btn);
        AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Signup.newIntent(getApplicationContext(),true);
                startActivity(intent);
                finish();
            }
        });


    }
    public  void updateUI(){
        List<User> users = new ArrayList<User>();
        users  = dbh.UsersInfo();

        if(mAdaptor==null) {
            mAdaptor = new UserAdaptor(users);
            mManage_User_RecyclerView.setAdapter(mAdaptor);
        }
        else
            mAdaptor.setUsers(users);
        mAdaptor.notifyDataSetChanged();
    }
}
class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private User User;
    private TextView User_Name,User_Password,User_Email;
    private Context mContext;
    DataBaseController dbh;
    Button edit,delete;
    @Override
    public void onClick(View v){


    }
    public void bindUser(User user){
        User = user;
        User_Name.setText(User.getUserName());
        User_Email.setText(User.getUserEmail());
        User_Password.setText(User.getUserPassword());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=UserDetails.newIntent(mContext,User.getUserName());
                mContext.startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=dbh.DeleteUser(User_Name.getText().toString());
                if(check) {
                    Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_LONG).show();

                }
                else
                    Toast.makeText(mContext,"Can't Delete",Toast.LENGTH_LONG).show();
            }
        });

    }
    public UserHolder(View itemView){

        super(itemView);
        itemView.setOnClickListener(this);
        mContext=itemView.getContext();
        dbh = DataBaseController.get(mContext);
        User_Name = (TextView) itemView.findViewById(R.id.manage_user_username);
        User_Email = (TextView) itemView.findViewById(R.id.manage_user_email);
        User_Password = (TextView) itemView.findViewById(R.id.manage_user_password);
        edit= (Button) itemView.findViewById(R.id.manage_user_edit_btn);
        delete= (Button) itemView.findViewById(R.id.manage_user_delete_btn);

    }


}
class UserAdaptor extends RecyclerView.Adapter<UserHolder>{
    private List<User> mUsers;
    //Constructor
    public UserAdaptor(List<User> users){
        mUsers = users;
    }
    //Abstract methods
    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_holder,parent,false);
        return new UserHolder(view);
    }
    @Override
    public void onBindViewHolder(UserHolder holder,int position){
        User user = mUsers.get(position);
        holder.bindUser(user);
    }
    @Override
    public int getItemCount(){

        return mUsers.size();
    }
    public  void setUsers(List<User> users){
        mUsers = users;
    }
}


