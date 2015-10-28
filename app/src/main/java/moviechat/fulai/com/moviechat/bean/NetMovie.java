package moviechat.fulai.com.moviechat.bean;

import java.util.List;

/**
 * Created by fulai on 2015/10/17.
 */
public class NetMovie {
    public Data data;
    public String message;
    public int error_code;


    public static class Data {
        public List<Movie> movie;
        public int num;

    }


    public static class Movie {
        //电影详情
        public String slot;
        //评分
        public String rating;
        //时长
        public String runtime;
        //电影名
        public String name;
        //图片
        public String img;
        //打分人数
        public String rating_count;
        //国家
        public String country;
        //上映时间
        public String release_date;
        //演员
        public List<String> actor;
        //导演
        public String director;
        //别名
        public String alias;
        //编剧
        public List<String> writers;
        //类型
        public String type;
    }

}
