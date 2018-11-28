import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;

public class Comment {
   // String web="https://movie.douban.com/subject/+/comments?start=0&limit=20&sort=new_score&status=P";
   // String movieId;

    /**
     * 创建BufferedReader用于读取电影的ID文件
     * @param path 电影的ID文件地址
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedReader creatReader(String path) throws FileNotFoundException {
        File file=new File(path);
        FileReader fr=new FileReader(file);
        BufferedReader br=new BufferedReader(fr);
        return br;
    }

    /**
     *  从电影ID文件中提取电影ID
     * @param br
     * @return
     * @throws IOException
     */
    public static String mergeUrl(BufferedReader br) throws IOException {
        String movieId="";
        String line=br.readLine();
        if(line!=null){
           movieId=line.split("\t")[0].trim();
           return movieId;
        }else return null;
    }

    /**
     * 抓取短评然后保存
     * @param lo  登录对象
     * @param web  获取短评的网址
     * @param savepath  存储路径
     * @param movieID  //电影ID
     * @throws IOException
     */
    public static void getCommentAndSave(Login lo,String web,String savepath,String movieID) throws IOException {
        //String page=HttpUnitCrawl.getPage(web);
        String page=lo.gethtml(web);
        BufferedWriter bw=new BufferedWriter(new FileWriter(new File(savepath),true));
        Document document= Jsoup.parse(page);
        Elements elements=document.select("span[class=comment-info],span[class=short]");
        String line;
        int len=elements.size();
        //System.out.println(len);
        for(int i=0;i<len;i+=2){
            line=movieID+" "+elements.get(i).text()+" "+elements.get(i).toString().split("> <")[3].split("title=\"")[1].replace("\"></span","")+
                    " "+elements.get(i+1).text();
            bw.append(line);
            bw.newLine();
            bw.flush();
      }
    }
}
