package xin.justcsl.bysjspider.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author nullptr
 * @date 2020/2/17 2:27
 */
@Component
public class CustomHtmlUtil {

    /**
     * 从 {@code <Spider.login()>} 返回的 Html 中获取 sid
     */
    public String getSid(String html) {
        Document doc = Jsoup.parse(html);
        //最后一个 script 标签的内容中有 sid
        String text = doc.getElementsByTag("script").last().data();
        //截取出 sid
        int start = text.lastIndexOf("sid=");
        //没有 sid 说明登陆失败
        if (start == -1) {
            return null;
        }
        int end = text.lastIndexOf("&screen");
        return text.substring(start + 4, end);
    }

    /**
     * 从 {@code <Spider.getData()>} 返回的 Html 中获取数据
     */
    public List<List<String>> getData(String html) {
        List<List<String>> result = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        //最后一个table 标签
        Element lastTable = doc.getElementsByTag("table").last();
        //最后一个 tables标签中所有的 tr
        Elements trs = lastTable.getElementsByTag("tr");
        //没选课题的情况
        if (trs.last().getElementsByTag("td").get(1).text().isEmpty()) {
            return null;
        }
        //一个课题的情况
        if (trs.size() == 2) {
            List<String> col = new ArrayList<>(4);
            Element tr = trs.last();
            tr.children().forEach((td) -> col.add(td.text()));
            result.add(col);
        }//多个课题的情况
        else {
            //遍历 trs， 第一个 tr 是表头跳过
            for (int i = 1; i < trs.size(); i++) {
                List<String> col = new ArrayList<>(4);
                Elements tds = trs.get(i).children();
                for (int j = 1; j < 5; j++) {
                    col.add(tds.get(j).text());
                }
                result.add(col);
            }
        }
        return result;
    }
}
