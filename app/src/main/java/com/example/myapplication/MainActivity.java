package com.example.myapplication;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        pager = (ViewPager)findViewById(R.id.pager);
        Button btn_first = (Button)findViewById(R.id.btn_first);
        Button btn_second = (Button)findViewById(R.id.btn_second);
        Button btn_third = (Button)findViewById(R.id.btn_third);

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        View.OnClickListener movePageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                pager.setCurrentItem(tag);
            }
        };
        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);
        btn_third.setOnClickListener(movePageListener);
        btn_third.setTag(2);
        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //startActivityForResult(intent, 100);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(FragmentManager fm )
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
                case 2:
                    return new Fragment3();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // total page count
            return 3;
        }
    }
    public class ActivityResultEvent {

        private int requestCode;
        private int resultCode;
        private Intent data;

        public ActivityResultEvent(int requestCode, int resultCode, Intent data) {
            this.requestCode = requestCode;
            this.resultCode = resultCode;
            this.data = data;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public Intent getData() {
            return data;
        }

        public void setData(Intent data) {
            this.data = data;
        }
    }

}
