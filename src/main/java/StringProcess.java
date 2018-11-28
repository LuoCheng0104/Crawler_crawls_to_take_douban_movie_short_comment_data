import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StringProcess{
    private String web="https://movie.douban.com/j/search_subjects?type=movie&tag=热门&sort=recommend&page_limit=20&page_start="; //抓取热门
    private String web1="https://movie.douban.com/j/search_subjects?type=movie&tag=%E6%9C%80%E6%96%B0&page_limit=20&page_start=";//抓取最新
    private String web2="https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%BB%8F%E5%85%B8&sort=time&page_limit=20&page_start=";//抓取经典
    private String path="D:\\IDEA\\IDEAproject\\webcrawl2\\data\\movie_index";
    /**
     * 获取的HTML解析出电影信息
     * @param str
     * @param path
     */
public static void movielist(String str,String path) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path), true));
    String[] str1=str.split("},\\{");
    str1[0]=str1[0].replace("{\"subjects\":[{","");
    String line;
    for(int i=0;i<str1.length;i++){
        String[] str2=str1[i].split(",");
        line=str2[6].split(":")[1]+"\t"+str2[2].split(":")[1]+"\t"+str2[0].split(":")[1];
        line=line.replace("\"","");
        writer.append(line);
        writer.newLine();
        writer.flush();
    }
}
    /**
     * 三个栏目分别抓取，每个最多抓50；
     * @throws IOException
     */
    public  void getAndsave() throws IOException {
        System.out.println("开始最新抓取");
        for (int i=0;i<50;i++) {
            System.out.print(".");
            String html= HttpUnitCrawl.getPage(this.web+ String.valueOf(20 * i));
            if(html.contains("{\"subjects\":[]}")) {
                System.out.println("结束热门抓取");
                break;
            }
            StringProcess.movielist(html, this.path);
        }
        System.out.println("开始最新抓取");
        for (int i=0;i<50;i++) {
            System.out.print(".");
            String html= HttpUnitCrawl.getPage(this.web1+ String.valueOf(20 * i));
            if(html.contains("{\"subjects\":[]}")) {
                System.out.println("结束最新抓取");
                break;
            }
            StringProcess.movielist(html, this.path);
        }
        System.out.println("开始经典抓取");
        for (int i=0;i<50;i++) {
            System.out.print(".");
            String html= HttpUnitCrawl.getPage(this.web2+ String.valueOf(20 * i));
            if(html.contains("{\"subjects\":[]}")) {
                System.out.println("结束经典抓取");
                break;
            }
            StringProcess.movielist(html, this.path);
        }
    }
}
