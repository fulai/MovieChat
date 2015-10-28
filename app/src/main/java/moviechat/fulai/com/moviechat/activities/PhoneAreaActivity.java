package moviechat.fulai.com.moviechat.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import moviechat.fulai.com.moviechat.Common.BaseActivity;
import moviechat.fulai.com.moviechat.R;
import moviechat.fulai.com.moviechat.bean.NetMovie;
import moviechat.fulai.com.moviechat.bean.NetPhone;
import moviechat.fulai.com.moviechat.utils.Constant;

public class PhoneAreaActivity extends BaseActivity {
    private static final String TAG = "PhoneAreaActivity";
    private EditText etNum;
    private TextView tvArea;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_area);
        initViews();
        addEvents();
    }

    @Override
    protected void getDataSuccess(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                final JSONObject jb = new JSONObject(json);
                int result = jb.getInt("error_code");
                if (result == -1) {
                    tvArea.setText(jb.getString("message"));
                } else if (result == 0) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<NetPhone>() {
                    }.getType();
                    NetPhone netPhone = gson.fromJson(json, type);
                    final StringBuffer sb = new StringBuffer();
                    if (!netPhone.data.province.equals(netPhone.data.city)) {
                        sb.append(netPhone.data.province + ",");
                    }
                    sb.append(netPhone.data.city + ",");
                    sb.append(netPhone.data.operator);
                    tvArea.setText(sb.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            tvArea.setText("没有查到数据");
        }
    }

    @Override
    protected void getDataFailed(String strJson) {
        Toast.makeText(this, strJson, Toast.LENGTH_SHORT).show();
    }

    private void addEvents() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNum = etNum.getText().toString().trim();
                if (TextUtils.isEmpty(strNum)) {
                    return;
                }
                searchArea(strNum);
            }


        });
    }

    private void searchArea(final String phoneNum) {
        if (phoneNum.length() != 11) {
            //Toast.makeText(this, "此号码不是合法的手机号!", Toast.LENGTH_SHORT).show();
            tvArea.setText("[" + phoneNum + "]此号码不是合法的手机号!");
            return;
        }
        String strUrl = Constant.PHONE_API;
        //name=your_value&page=your_value&num=your_value
        strUrl = strUrl + "phone=" + phoneNum;
        Log.i(TAG, "Url===" + strUrl);
        getDataByNet(strUrl, Constant.APIX_KEY_PHONE);
    }

    private void initViews() {
        etNum = (EditText) findViewById(R.id.et_num);
        tvArea = (TextView) findViewById(R.id.tv_area);
        btnSearch = (Button) findViewById(R.id.btn_search);
    }


}
