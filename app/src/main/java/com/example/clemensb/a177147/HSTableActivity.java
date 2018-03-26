package com.example.clemensb.a177147;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HSTableActivity extends AppCompatActivity {


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("UserHighScore");
    List<User> users;
    List<User> hsList;
    ListView yourHS;
    ListView list;
    String [] hsStringList;
    String [] yourHSList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("TEST");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstable);
        users = new ArrayList<>();
        hsList = new ArrayList<>();

        Button backButton = (Button) findViewById(R.id.hsbackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                finish();
            }
        });

        yourHS = (ListView)findViewById(R.id.hslistView);
        list = (ListView)findViewById(R.id.yourhslistView);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();
                for(DataSnapshot ds : childs){
                    User newUser = new User();
                    newUser.setUserName(ds.getKey());
                    newUser.setScore(ds.getValue(User.class).getScore());
                    //System.out.println(newUser.getUserName() + " : " + newUser.getScore());
                    users.add(newUser);
                }

                System.out.println("Sorted: ");
                sortUsers();

                hsStringList = new String[users.size()];
                int size;
                if(users.size()>= 5){
                    size = 5;
                }else{
                    size = users.size();
                }
                for(int i = 0; i < size; i++){
                    hsStringList[i] = "Rank "  + (i+1) + " : " + users.get(i).getUserName() + " : " + users.get(i).getScore();
                }


                List<String> listElementArray = new ArrayList<String>(Arrays.asList(hsStringList));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HSTableActivity.this, android.R.layout.simple_list_item_1, listElementArray);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sortUsers(){
        Collections.sort(users);
        /*for(int i = 0; i < users.size(); i++){
            System.out.println("Rank "  + (i+1) + " : " + users.get(i).getUserName() + " : " + users.get(i).getScore());
        }*/
    }

}
