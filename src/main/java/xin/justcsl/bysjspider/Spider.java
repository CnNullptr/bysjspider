package xin.justcsl.bysjspider;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xin.justcsl.bysjspider.util.CustomHttpUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author nullptr
 * @date 2020/2/16 20:09
 */
@Slf4j
@Component
public class Spider {

    private static final String BYSJ = "http://bysj.just.edu.cn";

    private CloseableHttpClient httpClient;
    private CustomHttpUtil customHttpUtil;

    @Autowired
    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Autowired
    public void setCustomHttpUtil(CustomHttpUtil customHttpUtil) {
        this.customHttpUtil = customHttpUtil;
    }

    /**
     * 模拟登陆后返回 HTML 文本，返回 null 说明出现问题
     */
    public String login(String username, String password) {
        HttpPost post = new HttpPost(BYSJ + "/index.aspx");
        //header
        post.addHeader("User-Agent", CustomHttpUtil.USER_AGENT);
        post.addHeader("Content-Type", CustomHttpUtil.CONTENT_TYPE);
        //post 的 body
        String reqBody = "__VIEWSTATE=" + CustomHttpUtil.VIEWSTATE +
                "&__VIEWSTATEGENERATOR=" + CustomHttpUtil.VIEWSTATEGENERATOR +
                "&__EVENTVALIDATION=" + CustomHttpUtil.EVENTVALIDATION +
                "&UserId=" + username +
                "&Pwd=" + password +
                "&LoginButton.x=0&LoginButton.y=0";

        post.setEntity(new StringEntity(reqBody, StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            //返回成功
            if (response.getStatusLine().getStatusCode() == 200) {
                //登陆是否成功由 HtmlUtil 判断
                return customHttpUtil.responseBody(response, "gb2312");
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 访问 “学生选题” 后返回 HTML，返回 null 说明出现问题
     */
    public String studentSelection(String sid) {
        HttpGet get = new HttpGet(BYSJ + "/Student/SelReport.aspx?sid=" + sid + "&screen=" + CustomHttpUtil.SCREEN);
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                return customHttpUtil.responseBody(response, "gb2312");
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
}
