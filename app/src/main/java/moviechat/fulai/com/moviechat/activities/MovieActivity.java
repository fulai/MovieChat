package moviechat.fulai.com.moviechat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import moviechat.fulai.com.moviechat.Common.BaseActivity;
import moviechat.fulai.com.moviechat.R;
import moviechat.fulai.com.moviechat.bean.NetMovie;
import moviechat.fulai.com.moviechat.utils.Constant;
import moviechat.fulai.com.moviechat.utils.ImageDownloader;
import moviechat.fulai.com.moviechat.utils.OnImageDownload;


public class MovieActivity extends BaseActivity {
    private static final String TAG = "MovieActivity";
    private EditText etSearchContent;
    private ImageView ivDelete;
    private ImageView ivSearch;
    private ListView lvContent;
    private TextView tvTip;
    private ListDataAdapter listDataAdapter;
    private InputMethodManager imm;
    private ProgressDialog progressDialog;

    private int page = 1;
    private int num = 10000;

    /**
     * 加载从网络获取的数据
     */
    private void addData(NetMovie netMovie) {
        if (netMovie != null) {
            String strTip = "共搜索到" + netMovie.data.num + "相关结果";
            tvTip.setText(strTip);
            listDataAdapter = new ListDataAdapter(this, netMovie.data.movie);
            lvContent.setAdapter(listDataAdapter);

        } else {
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        initViews();
        SetEvents();
    }

    @Override
    protected void getDataSuccess(String json) {
        try {
            if (!TextUtils.isEmpty(json)) {
                JSONObject jb = new JSONObject(json);
                String result = jb.getString("message");
                if (result.equals("success")) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<NetMovie>() {
                    }.getType();
                    NetMovie netMovie = gson.fromJson(json, type);
                    progressDialog.cancel();
                    addData(netMovie);
                } else {
                    Toast.makeText(MovieActivity.this, "没有找到影片", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void getDataFailed(String strJson) {
        Toast.makeText(this, strJson, Toast.LENGTH_SHORT).show();
    }

    private void SetEvents() {
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchContent.setText("");
                ivDelete.setVisibility(View.INVISIBLE);
                imm.showSoftInputFromInputMethod(etSearchContent.getWindowToken(), 0);
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = etSearchContent.getText().toString().trim();
                if (TextUtils.isEmpty(movieName)) {
                    return;
                }

                imm.hideSoftInputFromWindow(etSearchContent.getWindowToken(), 0); //隐藏
                setDataNetParam(movieName);
            }
        });
        etSearchContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ivDelete.getVisibility() != View.VISIBLE) {
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setDataNetParam(String movieName) {
        progressDialog = ProgressDialog.show(this, "提示", "加载中...");
        String strUrl = Constant.MOVIE_API;
        //name=your_value&page=your_value&num=your_value
        strUrl = strUrl + "name=" + movieName + "&page=" + page + "&num=" + num;
        Log.i(TAG, "Url===" + strUrl);
        getDataByNet(strUrl, Constant.APIX_KEY_MOVIE);
    }

    private void initViews() {
        etSearchContent = (EditText) findViewById(R.id.et_search_content);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        lvContent = (ListView) findViewById(R.id.lv_content);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private class ListDataAdapter extends BaseAdapter {
        private Context mContext;
        private List<NetMovie.Movie> movies;
        private ImageDownloader mDownloader;

        public ListDataAdapter(Context mContext, List<NetMovie.Movie> movies) {
            this.mContext = mContext;
            this.movies = movies;
        }

        @Override
        public int getCount() {
            return movies.size();
        }

        @Override
        public Object getItem(int position) {
            return movies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NetMovie.Movie movie = movies.get(position);
            Holder mHolder;
            if (convertView == null) {
                mHolder = new Holder();
                convertView = View.inflate(mContext, R.layout.list_item, null);
                mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                mHolder.tvActor = (TextView) convertView.findViewById(R.id.tv_actor);
                mHolder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
                mHolder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                convertView.setTag(mHolder);
            } else {
                mHolder = (Holder) convertView.getTag();
            }
            mHolder.tvName.setText(movie.name);
            mHolder.tvActor.setText(movie.actor.toString());
            mHolder.tvType.setText(movie.type);
            //这句代码的作用是为了解决convertView被重用的时候，图片预设的问题
            mHolder.ivPic.setImageResource(R.mipmap.ic_launcher);
            if (!TextUtils.isEmpty(movie.img)) {
                final String url = Constant.MOVIE_PIC_URL + movie.img;
                mHolder.ivPic.setTag(url);

                if (mDownloader == null) {
                    mDownloader = new ImageDownloader();
                }
                Log.i(TAG, "picUrl===" + url);
                if (mDownloader != null) {
                    //异步下载图片
                    mDownloader.imageDownload(url, mHolder.ivPic, "/gg", MovieActivity.this, new OnImageDownload() {
                        @Override
                        public void onDownloadSucc(Bitmap bitmap,
                                                   String c_url, ImageView mimageView) {
                            ImageView imageView = (ImageView) lvContent.findViewWithTag(c_url);
                            Log.i(TAG, "onDownloadSucc===" + c_url);
                            if (imageView != null) {
                                Log.i(TAG, "imageView===");
                                imageView.setImageBitmap(bitmap);
                                imageView.setTag("");
                            }
                        }
                    });
                }
            }
            return convertView;
        }
    }

    private class Holder {
        ImageView ivPic;
        TextView tvName;
        TextView tvActor;
        TextView tvType;
    }


}
