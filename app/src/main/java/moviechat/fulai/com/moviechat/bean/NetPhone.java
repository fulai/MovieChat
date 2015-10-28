package moviechat.fulai.com.moviechat.bean;

/**
 * Created by fulai on 2015/10/19.
 */
public class NetPhone {
    public String message;
    public int error_code;
    public Data data;

    public static class Data{
        public String province;
        public String operator;
        public String telephone;
        public String city;
    }

}
