package com.example.chatapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.User.UserListAdapter;
import com.example.chatapplication.User.UserObject;
import com.example.chatapplication.Utils.CountryToPhonePrefix;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserLayoutManager;
    private ArrayList<UserObject> userList, contactList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        userList = new ArrayList<>();
        contactList = new ArrayList<>();

        Button mCreate = findViewById(R.id.create);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat();
            }
        });

        initializeRecyclerView();
        getContactList();
    }


    private void createChat(){

        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        DatabaseReference chatInfoDb = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("user");

        HashMap newChatMap = new HashMap();
        newChatMap.put("id", key);
        newChatMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);

        Boolean validChat = false;
        for(UserObject mUser : userList){
            if(mUser.getSelected()){
                validChat = true;
                newChatMap.put("users/" + mUser.getUid(), true);
                userDb.child(mUser.getUid()).child("chat").child(key).setValue(true);
            }
        }

        if(validChat){
            chatInfoDb.updateChildren(newChatMap);
            userDb.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
        }

    }

    public void getContactList(){
        String ISOprefix = getCountryISO();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while(phones.moveToNext()){
            String  name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String  phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace(")", "");
            phone = phone.replace("(", "");

            if(!String.valueOf(phone.charAt(0)).equals("+"))
                phone = ISOprefix + phone;

            UserObject mContact = new UserObject("",name, phone);
            contactList.add(mContact);
            getUserDetails(mContact);

        }
    }

    private void getUserDetails(UserObject mContact) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String phone="", name = "";
                    for(DataSnapshot childSnapshot:dataSnapshot.getChildren()) {

                        if(childSnapshot.child("phone").getValue()!=null)
                            phone = childSnapshot.child("phone").getValue().toString();

                        if(childSnapshot.child("name").getValue()!=null)
                            name = childSnapshot.child("name").getValue().toString();

                        UserObject mUser = new UserObject(childSnapshot.getKey(), name, phone);

                        if(name.equals(phone))
                        {
                            for(UserObject mContactIterator: contactList)
                            {
                                if(mContactIterator.getPhone().equals(mUser.getPhone()))
                                {
                                    mUser.setName(mContactIterator.getName());
                                }

                            }

                        }
                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();
                        return;

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }



    private String getCountryISO(){
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(telephonyManager.getNetworkCountryIso()!=null)
            if(!telephonyManager.getNetworkCountryIso().toString().equals(""))
                iso = telephonyManager.getNetworkCountryIso().toString();
        return CountryToPhonePrefix.getPhone(iso);

    }
    private void initializeRecyclerView() {
        mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserLayoutManager  = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mUserList.setLayoutManager(mUserLayoutManager);
        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);

    }
}
