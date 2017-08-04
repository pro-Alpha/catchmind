package com.catchmind.catchmind;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TabFragment1 extends Fragment {

    public Cursor cursor;
    public MyDatabaseOpenHelper db;
    public SharedPreferences mPref;
    public SharedPreferences.Editor editor;
    public ListViewAdapter myListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab_fragment_1, container, false);

        final ArrayList<ListViewItem> ListData = new ArrayList<ListViewItem>();
        ArrayList<ListViewItem> FListData = new ArrayList<ListViewItem>();


        String myId = getArguments().getString("userId");
        String myNickname = getArguments().getString("nickname");
        String myProfile = getArguments().getString("profile");
        String myMessage = getArguments().getString("message");


        ListViewItem myItem = new ListViewItem(myId,myNickname,myProfile,myMessage);

        db = new MyDatabaseOpenHelper(getContext(),"catchMind",null,1);
        Cursor cursor = db.getList();

        while(cursor.moveToNext()) {

            ListViewItem addItem = new ListViewItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
            ListData.add(addItem);
            if(cursor.getInt(4) == 1){
                FListData.add(addItem);
            }

            Log.d("커서야", cursor.getString(0)+"#####"+cursor.getString(1) + "" +cursor.getString(2));
        }


        ListView lv = (ListView) rootView.findViewById(R.id.list);

        myListAdapter = (new ListViewAdapter(getActivity().getApplicationContext(),myItem,FListData,ListData));

        lv.setAdapter(myListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String userId =(String) view.getTag(R.id.userId);
                String nickname =(String) view.getTag(R.id.nickname);
                String profile =(String) view.getTag(R.id.profile);

                if(userId.equals("")){
                    return;
                }

                Toast.makeText(getActivity().getApplicationContext(),""+position+"###"+userId,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(),ProfileActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("userId",userId);
                intent.putExtra("nickname",nickname);
                intent.putExtra("profile",profile);
                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        myListAdapter.notifyDataSetChanged();
    }
}
