package com.example.myapplication;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ImagePopup extends Fragment implements OnClickListener{
    public ImagePopup()
    {
        // required
    }
    private final int imgWidth = 320;
    private final int imgHeight = 372;
    String imgPath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgPath = getArguments().getString("filename");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.image_popup,
                container,false);
        return view;
        //getActivity().setContentView(R.layout.image_popup);

        /** 전송메시지 */
        //Intent i = getActivity().getIntent();
        //Bundle extras = i.getExtras();
        //String imgPath = getArguments().getString("filename");
        //String imgPath = extras.getString("filename");



        /** 완성된 이미지 보여주기  */
        //BitmapFactory.Options bfo = new BitmapFactory.Options();
        //bfo.inSampleSize = 2;
        //ImageView iv = (ImageView)view.findViewById(R.id.imageView);
        //Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);
        //Bitmap resized = Bitmap.createScaledBitmap(bm, imgWidth, imgHeight, true);
        //iv.setImageBitmap(resized);

        /** 리스트로 가기 버튼 */
        //Button btn = (Button)view.findViewById(R.id.btn_back);
        //btn.setOnClickListener(this);
       // return view;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back:
                this.onDestroy();
                break;
        }
    }







    }




    /*public void openFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.pager, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/

