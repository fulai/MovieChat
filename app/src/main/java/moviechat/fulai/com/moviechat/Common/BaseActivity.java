package moviechat.fulai.com.moviechat.Common;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import moviechat.fulai.com.moviechat.R;
import moviechat.fulai.com.moviechat.utils.Constant;

public abstract class BaseActivity extends ActionBarActivity {

    protected Handler baseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                String strJson = (String) msg.obj;
                if (msg.what == 1) {
                    getDataSuccess(strJson);
                } else if (msg.what == 2) {
                    getDataFailed(strJson);
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base);
    }

    protected abstract void getDataSuccess(String strJson);

    protected abstract void getDataFailed(String strJson);

    protected void getDataByNet(final String strUrl, final String apixKey) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("content-type", "application/json")
                        .addHeader("apix-key", apixKey)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Message msg = Message.obtain();
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        msg.what = 1;
                        msg.obj = json;
                        baseHandler.sendMessage(msg);
                    } else {
                        msg.what = 2;
                        msg.obj = "获取数据失败";
                        baseHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
