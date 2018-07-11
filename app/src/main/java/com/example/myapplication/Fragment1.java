package com.example.myapplication;
import android.Manifest;
import android.app.Activity;
import android.widget.AdapterView.OnItemClickListener;
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
import android.os.AsyncTask;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.RelativeLayout;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import static android.app.Activity.RESULT_OK;

public class Fragment1 extends Fragment {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private int REQ_CODE_SELECT_IMAGE = 1;
    ListView Contact_lv;
    private ListAdapter mAdapter;
    public TextView pbContack;
    public static String PBCONTACT;
    public static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_CREATE=0;

    int checkcheck;
    ListView list;
    MyAdapter adapter;
    ArrayList<MyData> arrData = new ArrayList<MyData>();

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

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                    int position = data.getIntExtra("position",0);
                    String name = data.getStringExtra("contact_name");
                    String PH = data.getStringExtra("PH");
                    Bitmap bit = (Bitmap) data.getExtras().get("image");
                    String email = data.getStringExtra("email");
                int pos= data.getIntExtra("pos",0);
                String modified = data.getStringExtra("modified");

                MyData Data = new MyData(new BitmapDrawable(getResources(), bit),name,PH,email,bit);
                Data.modifydata(pos,modified);


                    ((MyData)adapter.getItem(position)).modify(Data.getImage(),Data.getName(),Data.getTel(),Data.getEmail(),Data.getBitmap());
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(),"Modified!",Toast.LENGTH_SHORT).show();
        }
        if (resultCode == Activity.RESULT_CANCELED) {

        }
    }}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ListView,adapter initialize
        View view = inflater.inflate(R.layout.fragment_fragment1,
                container,false);
        list = (ListView)view.findViewById(R.id.contact);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 3);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else
        {
            setData();


             }


        TextView del_but = (TextView) view.findViewById(R.id.delbtn);
        Button backup = (Button) view.findViewById(R.id.BACKUP);



        /*Contact_lv = (ListView) layout.findViewById(R.id.contact);
        Contact_lv.setAdapter(adapter);*/



        //Add,Delete,Modify button
        Button add_but = (Button) view.findViewById(R.id.add);

       // Button mod_but = (Button) view.findViewById(R.id.modify);
       // Button get_img_but = (Button) view.findViewById(R.id.get_user_img);
       // Button sync_but = (Button) view.findViewById(R.id.sync);


        add_but.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                adapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user),"Name","Phone number");
                list.setAdapter(adapter);
                Toast.makeText(getContext(),"added",Toast.LENGTH_SHORT).show();
            }
        });
        backup.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                new Task("Eee").execute("http://52.231.67.72:8080/load");
                Toast.makeText(getContext(),"backup",Toast.LENGTH_SHORT).show();
            }
        });


        /*mod_but.setOnClickListener(new Button.OnClickListener(){
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
        });*/
        return view;
    }


    private void setData(){
        Stack<String> strData = new Stack<>();
        JSONObject wrapObject = new JSONObject();
        Bitmap bitmap;

        Cursor c = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        //initialize adapter
        JSONArray jsonArray = new JSONArray();
        while(c.moveToNext())
        {
            Drawable icon = getResources().getDrawable(
                    R.drawable.user);
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.user);
            String image_uri = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            if(image_uri !=null) {
                Bitmap img;
                try {
                    img = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                            Uri.parse(image_uri));
                    icon = new BitmapDrawable(img);
                    bitmap = img;
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
            Cursor emailCursor =
                    getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Email.TYPE},
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID+"='"+String.valueOf(contactName)+"'",
                            null, null);
            String email;
            while(emailCursor.moveToNext()) {
                email = emailCursor.getString(
                        emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        strData.add(email);
                                  }
            if(strData.size()>0){
                email = strData.pop();}
            else {email=null;}

            MyData data = new MyData(icon, contactName, phNumber, email, bitmap);
            try{JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("contact", contactName);
                jsonObject.accumulate("PH", phNumber);
                jsonObject.accumulate("email", email);
                jsonArray.put(jsonObject);
                wrapObject.put("list",jsonArray);
            }catch(Exception e){e.getStackTrace();}
            arrData.add(data);

        }
        c.close();
       // Contact_lv.setAdapter(adapter);
        new JSONTask((wrapObject.toString())).execute("http://52.231.67.72:8080/insert");
        Toast.makeText(getContext(),"loaded",Toast.LENGTH_SHORT).show();

        adapter = new MyAdapter(getActivity(), arrData, new BtnClickListener(){
            @Override
            public void onBtnClick(int position) {
                // TODO Auto-generated method stub
                // Call your function which creates and shows the dialog here
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
            }
        });

        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(getActivity(), contact.class);
                MyData data = arrData.get(position);
                Bitmap bit = data.getBitmap();
                Bitmap resized = Bitmap.createScaledBitmap(bit, 150, 150, true);
                i.putExtra("position",position);
                i.putExtra("contact_name", data.getName());
                i.putExtra("PH", data.getTel());
                i.putExtra("image", resized);
                i.putExtra("email", data.getEmail());
                startActivityForResult(i,1);
            }
        });

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
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
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
    public class JSONTask extends AsyncTask<String, String, String> {

        String dataObject;
        String result = "";

        public JSONTask(String s){
            this.dataObject = s;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject wrapObject = new JSONObject(dataObject);
                JSONArray jsonArray = new JSONArray(wrapObject.getString("list"));
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    //for(int i = 0; i < jsonArray.length(); i++){
                      //JSONObject jObject = jsonArray.getJSONObject(0);
                        writer.write(jsonArray.toString());
                        writer.flush();
                        writer.close();//버퍼를 받아줌
                    //}

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    result = buffer.toString();
                    Toast.makeText(getContext(),"loaded",Toast.LENGTH_SHORT).show();

                    //return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
        }
    }
    public class Task extends AsyncTask<String, String, String> {

        String dataObject;
        String result = "";
        public Task(String s){
            this.dataObject = s;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    //for(int i = 0; i < jsonArray.length(); i++){
                    //JSONObject jObject = jsonArray.getJSONObject(0);
                    writer.write(dataObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌
                    //}

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    result = buffer.toString();
                    //return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            //adapter.clear();
            //arrData.clear();
            //result 파싱해서 name, phone number, email 을 각각 for문 동안
               //MyData newdata = new MyData(icon, name, phone number, email, bitmap);
               //arrData.add(newdata);
               //adapter.modifyto(arrData);
            //adapter.notifyDataSetChanged();

            Toast.makeText(getContext(),result, Toast.LENGTH_SHORT).show();
        }
    }
}
