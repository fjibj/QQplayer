import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.text.SimpleDateFormat;

import com.scienjus.smartqq.callback.MessageCallback;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.DiscussMessage;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupMessage;
import com.scienjus.smartqq.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author *
 */
public class WebContent {

    private long groupId = 0;
    private String step = "S";
    private String oa = "";
    private String ob = "";
    private String oc = "";
    private String od = "";
    private String oe = "";
    private String of = "";
    private String og = "";

    /**
     * 读取一个网页全部内容
     */
    public String getOneHtml(final String htmlurl) throws IOException {
        URL url;
        String temp;
        final StringBuffer sb = new StringBuffer();
        try {
            url = new URL(htmlurl);
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));// 读取网页全部内容
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
        } catch (final MalformedURLException me) {
            System.out.println("你输入的URL格式有问题！请仔细输入");
            me.getMessage();
            throw me;
        } catch (final IOException e) {
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }

    /**
     * @param s
     * @return 获得网页标题
     */
    public String getTitle(final String s) {
        String regex;
        String title = "";
        final List<String> list = new ArrayList<String>();
        regex = "<title>.*?</title>";
        final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        for (int i = 0; i < list.size(); i++) {
            title = title + list.get(i);
        }
        return outTag(title);
    }

    /**
     * @param s
     * @return 获得链接
     */
    public List<String> getLink(final String s) {
        String regex;
        final List<String> list = new ArrayList<String>();
        regex = "<a\\s+data-z=([^>h]|h(?!ref\\s))*(?<=[\\s+]?href[\\s+]?=[\\s+]?('|\")?)([^\"|'>]+?(?=\"|'))(.+?)?((?<=>)(.+?)?(?=</a>))";
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group(3));
        }
        return list;
    }

    /**
     * @param s
     * @return 获得脚本代码
     */
    public List<String> getScript(final String s) {
        String regex;
        final List<String> list = new ArrayList<String>();
        regex = "<script.*?</script>";
        final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        return list;
    }

    /**
     * @param s
     * @return 获得CSS
     */
    public List<String> getCSS(final String s) {
        String regex;
        final List<String> list = new ArrayList<String>();
        regex = "<style.*?</style>";
        final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        return list;
    }

    /**
     * @param s
     * @return 去掉标记
     */
    public String outTag(final String s) {
        return s.replaceAll("<.*?>", "");
    }

    /**
     * @param s
     * @return 获取现代消费导报今日读报文章标题及内容
     */
    public HashMap<String, String> getFromXFDB(final String s) {
        final HashMap<String, String> hm = new HashMap<String, String>();
        final StringBuffer sb = new StringBuffer();
        String html = "";
//        System.out.println("\n------------------开始读取网页(" + s + ")--------------------");
        try {
            html = getOneHtml(s);
        } catch (final Exception e) {
            e.getMessage();
        }
//        System.out.println(html);
//        System.out.println("------------------读取网页(" + s + ")结束--------------------\n");
//        System.out.println("------------------分析(" + s + ")结果如下--------------------\n");
        String title = outTag(getTitle(html));
//        title = title.replaceAll("_雅虎知识堂", "");
        // Pattern pa=Pattern.compile("<div
        // class=\"original\">(.*?)((\r\n)*)(.*?)((\r\n)*)(.*?)</div>",Pattern.DOTALL);
        final Pattern pa = Pattern.compile("<div\\s+class=\"rich_media_content\\s+\".*>(.*?)<p><img\\s+data-s=.*/>");
        final Matcher ma = pa.matcher(html);
        while (ma.find()) {
            sb.append(ma.group());
        }
        String temp = sb.toString();
//        System.out.println("temp:" + temp);

        temp = temp.replaceAll("(<br\\s+/>)+?", "\n");
        temp = temp.replaceAll("(<p>)+?", "\n");// 转化换行
        temp = temp.replaceAll("<p><em>.*?</em></p>", "");// 去图片注释
        hm.put("title", title.replaceAll("&nbsp;", " "));
        hm.put("original", outTag(temp).replaceAll("&nbsp;", " "));
        return hm;
    }

    /**
     * @param client
     * @param groupName
     * @param step      当前已发送过的步骤
     * @return
     */
    public boolean getAndSentMessageToQQGroup(SmartQQClient client, String groupName) {
        try {
            System.out.println("step=" + step);
            switch (step) {
                case "S": //开始
                    String url = //"http://weixin.sogou.com/weixin?query=%E7%8E%B0%E4%BB%A3%E6%B6%88%E8%B4%B9%E5%AF%BC%E6%8A%A5+%E4%BB%8A%E6%97%A5%E8%AF%BB%E6%8A%A5&_sug_type_=&sut=7122&_sug_=n&type=2&ie=utf8&sst0=1479306058600&sourceid=inttime_day&interation=&interV=kKIOkrELjboJmLkElbYTkKIKmbELjbkRmLkElbk%3D_1893302304&tsn=1";
// "http://weixin.sogou.com/weixin?query=现代消费导报+今日读报&_sug_type_=&sut=7122&_sug_=n&type=2&ie=utf8&sst0=1479306058600&sourceid=inttime_day&interation=&interV=kKIOkrELjboJmLkElbYTkKIKmbELjbkRmLkElbk%3D_1893302304&tsn=1";
/* "http://weixin.sogou.com/weixin?query=%E7%8E%B0%E4%BB%A3%E6%B6%88%E8%B4%B9%E5%AF%BC%E6%8A%A5+%E4%BB%8A%E6%97%A5%E8%AF%BB%E6%8A%A5&_sug_type_=&_sug_=n&type=2&ie=utf8&sourceid=inttime_day&interation=&interV=kKIOkrELjboJmLkElbYTkKIKmbELjbkRmLkElbk%3D_1893302304&tsn=1";*/
                            "http://weixin.sogou.com/weixin?type=2&ie=utf8&query=%E7%8E%B0%E4%BB%A3%E6%B6%88%E8%B4%B9%E5%AF%BC%E6%8A%A5%20%E6%AF%8F%E6%97%A5%E6%96%B0%E9%97%BB&tsn=1&ft=&et=&interation=null&wxid=oIWsFt-hY7SA_Lbnoc2W44skDvX8&usip=%E7%8E%B0%E4%BB%A3%E6%B6%88%E8%B4%B9%E5%AF%BC%E6%8A%A5&from=tool";

                    String html = getOneHtml(url);
                    System.out.println(html);
                    url = getLink(html).get(0).toString();
                    url = url.replaceAll("&amp;", "&");
                    System.out.println("url:" + url);
                    HashMap<String, String> ma = getFromXFDB(url);
                    String title = ma.get("title").trim();
                    String original = ma.get("original").trim();
                    oa = original.substring(0, original.indexOf("B.【全球风】"));
                    System.out.println("A:" + oa);
                    ob = original.substring(original.indexOf("B.【全球风】"), original.indexOf("C.【财经点】"));
                    System.out.println("B:" + ob);
                    oc = original.substring(original.indexOf("C.【财经点】"), original.indexOf("【教科文】") - 2);
                    System.out.println("C:" + oc);
                    od = original.substring(original.indexOf("【教科文】") - 2, original.indexOf("E.【生活事】"));
                    System.out.println("D:" + od);
                    oe = original.substring(original.indexOf("E.【生活事】"), original.indexOf("F.【健康道】"));
                    System.out.println("E:" + oe);
                    of = original.substring(original.indexOf("F.【健康道】"), original.indexOf("G.【社会窗】"));
                    System.out.println("F:" + of);
                    og = original.substring(original.indexOf("G.【社会窗】"));
                    System.out.println("G:" + og);
                    //根据群名称找群ID
                    List<Group> groups = client.getGroupList();
                    for (Group group : groups) {
                        if (groupName.equalsIgnoreCase(group.getName())) {
                            groupId = group.getId();
                            break;
                        }
                    }
                    System.out.println("groupid=" + groupId);
                    if (groupId == 0) {
                        System.out.println("未找到群！");
                        return false;
                    }
                    client.sendMessageToGroup(groupId, "Hello, everyone! Good morning!");
                    //开始发送A段
                    if (!client.sendMessageToGroup(groupId, title + "\n" + oa)) return false;
                    step = "A";
                case "A": //已发送A，准备从B段开始发送
                    if (!client.sendMessageToGroup(groupId, ob)) return false;
                    step = "B";
                case "B": //已发送B，准备从C段开始发送
                    if (!client.sendMessageToGroup(groupId, oc)) return false;
                    step = "C";
                case "C": //已发送C，准备从D段开始发送
                    if (!client.sendMessageToGroup(groupId, od)) return false;
                    step = "D";
                case "D": //已发送D，准备从E段开始发送
                    if (!client.sendMessageToGroup(groupId, oe)) return false;
                    step = "E";
                case "E": //已发送E，准备从F段开始发送
                    if (!client.sendMessageToGroup(groupId, of)) return false;
                    step = "F";
                case "F": //已发送F，准备从G段开始发送
                    if (!client.sendMessageToGroup(groupId, og)) return false;
                    step = "G";
                default:
                    break;
            }
            if (!client.sendMessageToGroup(groupId, "Bye, everyone! Good morning!")) return false;
            //消息完全发送完之后将各变量值还原
            groupId = 0;
            step = "S";
            oa = "";
            ob = "";
            oc = "";
            od = "";
            oe = "";
            of = "";
            og = "";
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * @param args 测试查询当日现代消费导报今日读报内容
     */
    public static void main(final String args[]) {
        final String groupName = args[0];
        int hour = Integer.parseInt(args[1]);
        int min = Integer.parseInt(args[2]);
        int sec = Integer.parseInt(args[3]);
        int cyclehour = Integer.parseInt(args[4]); //循环周期（小时）
        int isnextday = Integer.parseInt(args[5]); //是否明天（1：明天，0：今天）
        final SmartQQClient client = new SmartQQClient(null);
        final WebContent wc = new WebContent();
        System.out.println("args[0]=" + args[0]);
        System.out.println("args[1]:args[2]:args[3]=" + args[1] + ":" + args[2] + ":" + args[3]);
        Calendar calendar = Calendar.getInstance();
        Date time = new Date();
        calendar.setTime(time);
        if (isnextday == 1) calendar.add(Calendar.DATE, 1);//明天
        calendar.set(Calendar.HOUR_OF_DAY, hour); // 控制时
        calendar.set(Calendar.MINUTE, min);    // 控制分
        calendar.set(Calendar.SECOND, sec);    // 控制秒

        time = calendar.getTime();     // 得出执行任务的时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        System.out.println("启动时间：" + df.format(time) + "!");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int count = 0;
                while (!wc.getAndSentMessageToQQGroup(client, groupName)) {
                    try {
                        System.out.println("执行失败，等待5分钟后重试......");
                        Thread.sleep(300000); //等待5分钟后再查找
                    } catch (InterruptedException e1) {
                    } finally {
                        count++;
                        if (count >= 10) {
                            System.out.println("连续10次获取失败，放弃。。。");
                            client.sendMessageToGroup(wc.groupId, "获取今日新闻失败!");
                            break;
                        }
                    }
                }
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println("今日(" + df.format(cal.getTime()) + ")新闻发送成功!");
            }
        }, time, 1000 * 60 * 60 * cyclehour);// 每隔cyclehour小时执行一次
    }
}