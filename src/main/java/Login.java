import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
/**
 * @author:
 * @email:
 */
public class Login{
    private static HttpClient httpClient=new DefaultHttpClient();
    private static String[] UserAgent={"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803",
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0"
    };//待设置的UserAgent,防止被网站封禁

    /**
     * 主登录入口,里面根据需要设置账号和密码
     */
    public static void loginDouban(){
        String login_src="https://accounts.douban.com/login";
        //输入用户名及密码
        String form_email="";
        String form_password="";
        //获取验证码
        String captcha_id=getImgID();
        String login="登录";
        String captcha_solution="";
        //输入验证码
        System.out.println("请输入验证码：");
        BufferedReader buff=new BufferedReader(new InputStreamReader(System.in));
        try {
            captcha_solution=buff.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("form_email", form_email));
        list.add(new BasicNameValuePair("form_password", form_password));
        list.add(new BasicNameValuePair("captcha-solution", captcha_solution));
        list.add(new BasicNameValuePair("captcha-id", captcha_id));
        list.add(new BasicNameValuePair("login", login));
        HttpPost httpPost = new HttpPost(login_src);
        try {
            //向后台请求数据,登陆网站
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse response=httpClient.execute(httpPost);
            HttpEntity entity=response.getEntity();
            String result=EntityUtils.toString(entity,"utf-8");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * 获取验证码图片“token”值
     * @return token
     */
    private static String getImgID(){
        //Json的地址[数据中包含验证码的地址]
        String src="https://www.douban.com/j/misc/captcha";
        HttpGet httpGet=new HttpGet(src);
        String token="";
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity=response.getEntity();
            //将json数据转化为map，对应的是key，value的形式。不理解json数据的，请看我前面的关于json解析的博客
            String content=EntityUtils.toString(entity,"utf-8");
            Map<String,String> mapList=getResultList(content);
            token=mapList.get("token");
            //获取验证码的地址
            String url="https:"+mapList.get("url");
            //下载验证码并存储到本地
            downImg(url);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return token;
    }
    /**
     * 用JSON 把数据格式化，并生成迭代器，放入Map中返回
     * @param content 请求验证码时服务器返回的数据
     * @return Map集合
     */
    public static Map<String,String> getResultList(String content){
        Map<String,String> maplist=new HashMap<String,String>();
        try {
            org.json.JSONObject jo=new org.json.JSONObject(content.replaceAll(",\\\"r\\\":false", ""));
            Iterator it = jo.keys();
            String key="";
            String value="";
            while(it.hasNext()){
                key=(String) it.next();
                value=jo.getString(key);
                maplist.put(key, value);
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return maplist;
    }
    /**
     * 此方法是下载验证码图片到本地
     * @param src  给个验证图片完整的地址
     * @throws IOException
     */
    private static void downImg(String src) throws IOException{
        File fileDir=new File("D:\\IDEA\\IDEAproject\\webcrawl2\\data\\yzm\\");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        //图片下载保存地址
        File file=new File("D:\\IDEA\\IDEAproject\\webcrawl2\\data\\yzm\\yzm.png");
        if(file.exists()){
            file.delete();
        }
        InputStream input = null;
        FileOutputStream out= null;
        HttpGet httpGet=new HttpGet(src);
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            input = entity.getContent();
            int i=-1;
            byte[] byt=new byte[1024];
            out=new FileOutputStream(file);
            while((i=input.read(byt))!=-1){
                out.write(byt);
            }
            System.out.println("图片下载成功！");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }
    //登陆后，便可以输入一个或者多个url，进行请求
    public static String gethtml(String redirectLocation) {
        HttpGet httpget = new HttpGet(redirectLocation);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = "";
        try {
            httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,UserAgent[(int)(Math.random()*5)]);//设置多个UserAgent
            responseBody = httpClient.execute(httpget, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = null;
        } finally {
            httpget.abort();
//            httpClient.getConnectionManager().shutdown();
        }
        return responseBody;
    }

    //一个实例，只请求了一个url

    /**
     *y用于测试登录和获取页面信息
     * @param args
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void main(String[] args)  {
        loginDouban();
        String redir="https://movie.douban.com/subject/26752088/comments?start=200&limit=20&sort=new_score&status=P";
//      System.out.println(gethtml(redir));
        String page=gethtml(redir);
        Document document= Jsoup.parse(page);
        Elements elements=document.select("span[class=comment-info],span[class=short]");
        String line;
        int len=elements.size();
        //System.out.println(len);
        for(int i=0;i<len;i+=2){
            line=elements.get(i).text()+" "+elements.get(i).toString().split("> <")[3].split("title=\"")[1].replace("\"></span","")+
                    " "+elements.get(i+1).text();
            System.out.println(line);
        }
    }
}
