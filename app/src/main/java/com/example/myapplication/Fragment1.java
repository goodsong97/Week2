package com.example.myapplication;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.RelativeLayout;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.myapplication.C_Adapter;
import com.example.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Fragment1 extends Fragment {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private int REQ_CODE_SELECT_IMAGE = 1;
    C_Adapter adapter = new C_Adapter();
    C_Adapter sync_adapter = new C_Adapter();
    ListView Contact_lv;
    int checkcheck;

    public Fragment1()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());

                    Drawable icon = new BitmapDrawable(image_bitmap);
                    adapter.modifyicon(checkcheck, icon);
                    adapter.notifyDataSetChanged();
                }
                catch (FileNotFoundException e) { e.printStackTrace(); }
                catch (IOException e) {	e.printStackTrace();}
                catch (Exception e)	{e.printStackTrace();}
            }
            Toast.makeText(getContext(),"Modified!",Toast.LENGTH_SHORT).show();
        }
        if (resultCode == Activity.RESULT_CANCELED) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ListView,adapter initialize

        final RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_fragment1, container, false);

        Contact_lv = (ListView) layout.findViewById(R.id.contact);
        Contact_lv.setAdapter(adapter);

        //Load Button
        Button load_but = (Button)layout.findViewById(R.id.load);
        View.OnClickListener load_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 3);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                }

                else
                {
                    //Load contacts whenever clicking
                    Cursor c = getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            null, null, null);

                    //initialize adapter
                    adapter.remove_all();
                    while(c.moveToNext())
                    {
                        Drawable icon = getResources().getDrawable(
                                R.drawable.user);
                        String image_uri = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        if(image_uri !=null) {
                            Bitmap img;
                            try {
                                img = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                                        Uri.parse(image_uri));
                                icon = new BitmapDrawable(img);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        String contactName = c
                                .getString(c
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phNumber = c
                                .getString(c
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        adapter.addItem(icon,contactName,phNumber);
                    }
                    sync_adapter = adapter;
                    c.close();
                    Contact_lv.setAdapter(adapter);
                    Toast.makeText(getContext(),"loaded",Toast.LENGTH_SHORT).show();
                }
            }
        };

        load_but.setOnClickListener(load_listener);

        //Add,Delete,Modify button
        Button add_but = (Button) layout.findViewById(R.id.add);
        Button del_but = (Button) layout.findViewById(R.id.delete);
        Button mod_but = (Button) layout.findViewById(R.id.modify);
        Button get_img_but = (Button) layout.findViewById(R.id.get_user_img);
        Button sync_but = (Button) layout.findViewById(R.id.sync);


        add_but.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                adapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user),"Name","Phone number");
                Contact_lv.setAdapter(adapter);
                Toast.makeText(getContext(),"added",Toast.LENGTH_SHORT).show();
            }
        });

        del_but.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                int count, checked;
                count = adapter.getCount();
                if(count>0){
                    checked = Contact_lv.getCheckedItemPosition();

                    if(checked>-1 && checked <count){
                        adapter.removeItem(checked);
                        Contact_lv.clearChoices();
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getContext(),"deleted",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mod_but.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                final int count, checked;
                count = adapter.getCount();
                if(count>0){
                    checked = Contact_lv.getCheckedItemPosition();

                    if(checked > -1&&checked < count){
                        switch(v.getId()){
                            case R.id.modify :
                                ListViewItem l = (ListViewItem) adapter.getItem(checked);
                                Drawable i = l.getIconDrawable();
                                String n = l.getName();
                                String p = l.getName();

                                final Modify_dialog dialog = new Modify_dialog(getContext(),i,n,p);
                                dialog.show();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        Drawable icon = dialog.getResult().getIconDrawable();
                                        String Name = dialog.getResult().getName();
                                        String Ph_num = dialog.getResult().getPh_num();
                                        adapter.modifyItem(checked,icon,Name,Ph_num);
                                        Contact_lv.clearChoices();
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                        }

                    }
                }
            }
        });


        get_img_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count,checked;
                count = adapter.getCount();
                if(count>0){
                    checked = Contact_lv.getCheckedItemPosition();
                    if(checked> -1&&checked < count) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        checkcheck = checked;
                        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    }
                    Toast.makeText(getContext(),"Get user image",Toast.LENGTH_SHORT).show();
                }
            }});

        sync_but.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //GET PERMISSION
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, 4);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 3);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                }
                else {

                    //Remove all contacts
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    while (cursor.moveToNext()) {
                        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        contentResolver.delete(uri, null, null);
                    }


                    //ADD ALL CONTACTS

                    ArrayList<ListViewItem> ar = adapter.getListViewItemList();
                    int cnt=0;
                    int sz =ar.size();


                    while (cnt < sz) {
                        BitmapDrawable draw = (BitmapDrawable) ar.get(cnt).getIconDrawable();
                        Bitmap bitmap = draw.getBitmap();
                        ContactAdd(ar.get(cnt).getName(), ar.get(cnt).getPh_num(), bitmap);
                        cnt++;
                    }

                    Toast.makeText(getContext(), "Synchronized", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return layout;
    }

    public void ContactAdd(final String n,final String p,final Bitmap img){
        new Thread() {
            @Override
            public void run() {
                ArrayList<ContentProviderOperation> list = new ArrayList<>();
                try {
                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                    .build()
                    );

                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, n)   //이름

                                    .build()
                    );

                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, p)           //전화번호
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)   //번호타입(Type_Mobile : 모바일)

                                    .build()
                    );
                    Bitmap imgg = readImageWithSampling(getRealPathFromURI(getContext(),getImageUri(getContext(),img)),100,120);
                    byte[] photo = bitmapToByteArray(imgg);
                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo)
                                    .withValue(ContactsContract.Data.IS_PRIMARY,1)
                                    .build()
                    );

                    getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);  //주소록추가
                    list.clear();   //리스트 초기화
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e("읭","읭");
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                    Log.e("ㅇ헝","ㅇ헝");
                }
            }
        }.start();
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public byte[] bitmapToByteArray( Bitmap $bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        $bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }
    public Bitmap readImageWithSampling(String imagePath, int targetWidth, int targetHeight) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);

        int photoWidth  = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        if (targetHeight <= 0) {
            targetHeight = (targetWidth * photoHeight) / photoWidth;
        }

        // Determine how much to scale down the image
        int scaleFactor = 1;
        if (photoWidth > targetWidth) {
            scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}