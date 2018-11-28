import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUnitCrawl {

    /**
     * 返回网址的代码
     * @param web
     * @return
     */
    public static String getPage(String web){
        // StringBuffer content=new StringBuffer();
        // Document document= Jsoup.parse(page);
        // Elements elements=document.getAllElements();
        // int len=elements.size();
        //  System.out.println(content);
        StringBuffer content=new StringBuffer();
        try {
            URL url=new URL(web);
           // HttpClient con=(HttpClient)url.openConnection();
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");

            BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String line;
            while((line=reader.readLine())!=null){
                content.append(line+"\n");
            }
          // System.out.println(content);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    /**
     * 程序抓取热门电影的ID,然后用ID来抓取评论信息,暂时没用
     * @param str
     * @param path 存储路径
     * @throws FailingHttpStatusCodeException
     * @throws MalformedURLException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void testUserHttpUnit(String str,String path) throws FailingHttpStatusCodeException,
            MalformedURLException, IOException, InterruptedException {
        /** HtmlUnit请求web页面 */
        URL url=new URL(str);
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
        wc.getOptions().setCssEnabled(false); // 禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(100000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.getOptions().setDoNotTrackEnabled(false);
        HtmlPage page = wc.getPage(url);
        Thread.sleep(10000);//等待使得网页完全加载
        DomNodeList<DomElement> links = page.getElementsByTagName("a");
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path), true));
        String line;
        for (DomElement link : links) {
            line=link.getAttribute("href")+"\t"+link.asText();
            if(line.contains("热门")){
                writer.append(line);
                writer.newLine();
                writer.flush();
            }
        }
        writer.close();
    }
}
