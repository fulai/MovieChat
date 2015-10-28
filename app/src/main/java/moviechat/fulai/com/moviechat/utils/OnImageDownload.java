package moviechat.fulai.com.moviechat.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by fulai on 2015/10/17.
 */
public interface OnImageDownload {
    void onDownloadSucc(Bitmap bitmap,String c_url,ImageView imageView);
}
