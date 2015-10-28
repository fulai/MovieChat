package moviechat.fulai.com.moviechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import moviechat.fulai.com.moviechat.R;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private GridView gvList;
    private MyAdapter adapter;
    private static String[] names = {"电影", "手机归属地", "天气预报"};
    private static int[] intPic = {R.mipmap.movie_pic, R.mipmap.phone_pic, R.mipmap.weather};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    private void addEvents() {
        gvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:// 电影
                        intent = new Intent(MainActivity.this, MovieActivity.class);
                        startActivity(intent);
                        break;
                    case 1:// 手机归属地
                        intent = new Intent(MainActivity.this, PhoneAreaActivity.class);
                        startActivity(intent);
                        break;
                    case 2:// 天气预报
                        intent = new Intent(MainActivity.this, WeatherActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void initViews() {
        gvList = (GridView) findViewById(R.id.gv_list);
        adapter = new MyAdapter();
        gvList.setAdapter(adapter);
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = View.inflate(MainActivity.this, R.layout.list_item_grid, null);
            ImageView iv = (ImageView) inflate.findViewById(R.id.iv_list_item);
            TextView tv = (TextView) inflate.findViewById(R.id.tv_list_title);
            tv.setText(names[position]);
            iv.setImageResource(intPic[position]);
            return inflate;
        }
    }


}
