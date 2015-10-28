package moviechat.fulai.com.moviechat.utils;

import android.app.Activity;
import android.os.Environment;

/**
 * Created by fulai on 2015/10/17.
 */
public class Util {
    private static Util util;
    public static int flag = 0;

    private Util() {

    }

    public static Util getInstance() {
        if (util == null) {
            util = new Util();
        }
        return util;
    }

    /**
     *
     * @return
     */
    public boolean hasSDCard() {
        boolean b = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            b = true;
        }
        return b;
    }

    /**
     *
     * @return
     */
    public String getExtPath() {
        String path = "";
        if (hasSDCard()) {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        return path;
    }

    /**
     *
     * @param mActivity
     * @return
     */
    public String getPackagePath(Activity mActivity) {
        return mActivity.getFilesDir().toString();
    }

    /**
     *
     * @param url
     * @return
     */
    public String getImageName(String url) {
        String imageName = "";
        if (url != null) {
            imageName = url.substring(url.lastIndexOf("/") + 1);
        }
        return imageName;
    }
}
