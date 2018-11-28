import java.io.BufferedReader;
import java.io.IOException;

public class DouBan {

    public static void main(String[] args) throws IOException, InterruptedException {
        Login lo=new Login();
        lo.loginDouban();//账号登录
        /**获取热门电影及ID*/
        //StringProcess sp=new StringProcess();
       // sp.getAndsave();
        /**获取电影短评数据*/
        BufferedReader br=Comment.creatReader("D:\\IDEA\\IDEAproject\\webcrawl2\\data\\movie_index");
        String movieID=null;
        while((movieID=Comment.mergeUrl(br))!=null){
            String web="https://movie.douban.com/subject/"+movieID+"/comments?start=0&limit=20&sort=new_score&status=P";
            System.out.println("正在获取ID："+movieID);
        for(int i=0;i<50;i++){
            System.out.print(".");
            String web1=web.replace("start=0","start="+String.valueOf(i*20));
            Thread.sleep((long)(Math.random()*2000));
            Comment.getCommentAndSave(lo,web1,"D:\\IDEA\\IDEAproject\\webcrawl2\\data\\comments",movieID);
            }
            Thread.sleep(30000);
            System.out.println();
        }
    }
}
