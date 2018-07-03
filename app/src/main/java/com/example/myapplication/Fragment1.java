package com.example.myapplication;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;

// Request code for READ_CONTACTS. It can be any number > 0.

public class Fragment1 extends Fragment {
    public Fragment1()
    {
        // required
    }
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ListView list;
    LinearLayout ll;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_fragment1,
                container,false);
        list = (ListView) view.findViewById(R.id.list_view_1);
        ll = (LinearLayout) view.findViewById(R.id.LinearLayout1);
        showContacts();
        return view;
    }

    private void showContacts(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }else {
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();
            ArrayList<String> contacts = new ArrayList<String>();

            Cursor c = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            while (c.moveToNext()) {
                JSONObject sObject = new JSONObject();
                String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                JSONParser parser1 = new JSONParser();
                JSONParser parser2 = new JSONParser();
                try{
                sObject.put("Name", parser1.parse(contactName));
                sObject.put("P.H", parser2.parse(phNumber));
                }catch(Exception e){
                    System.out.println(e);
                }
                arr.put(sObject);


                contacts.add(contactName + " : " + phNumber);

            }
            c.close();

            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    contacts
            );
            list.setAdapter(listViewAdapter);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we cannot display the contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
