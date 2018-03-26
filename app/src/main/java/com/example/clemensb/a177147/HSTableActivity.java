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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();
                for(DataSnapshot ds : childs){
                    User newUser = new User();
                    newUser.setUserName(ds.getKey());
                    newUser.setScore(ds.getValue(User.class).getScore());
                    //System.out.println(newUser.getUserName() + " : " + newUser.getScore());
                    users.add(newUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println("Sorted: ");
        sortUsers();

        hsStringList = new String[users.size()];
        for(int i = 0; i<users.size(); i++){
            hsStringList[i] = i + ":  "  + users.get(i).toString();
        }
        System.out.println(hsStringList);

        List<String> listElementArray = new ArrayList<String>(Arrays.asList(hsStringList));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HSTableActivity.this, android.R.layout.simple_list_item_1, listElementArray);

        /*for(int i = 0; i < 10; i++){
            hsList.add(users.get(i));
        }*/
        //ListAdapter adapter = new ArrayAdapter<User>(HSTableActivity.this, android.R.layout.simple_list_item_1, users);
        list.setAdapter(adapter);
    }

    public void sortUsers(){
        Collections.sort(users);
    }

}
