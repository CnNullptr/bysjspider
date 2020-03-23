package xin.justcsl.bysjspider;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import xin.justcsl.bysjspider.entity.ExcelObj;
import xin.justcsl.bysjspider.entity.Project;
import xin.justcsl.bysjspider.util.CustomHtmlUtil;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@SpringBootApplication
public class BysjSpiderApplication implements CommandLineRunner {

    private MyRepository myRepository;
    private Spider spider;
    private CustomHtmlUtil htmlUtil;

    public static void main(String[] args) {
        SpringApplication.run(BysjSpiderApplication.class, args);
    }

    @Autowired
    public void setMyRepository(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    @Autowired
    public void setSpider(Spider spider) {
        this.spider = spider;
    }

    @Autowired
    public void setHtmlUtil(CustomHtmlUtil htmlUtil) {
        this.htmlUtil = htmlUtil;
    }

    @Bean
    public CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    @Override
    public void run(String... args) throws Exception {
//        webSpider();
        exportExcel();
    }

    /**
     * 从毕设系统爬取数据并保存在数据库
     */
    public void webSpider() {
        //先取到所有班级
        List<String> classIdList = myRepository.selectAllClassId();
        //以班为单位查询
        classIdList.forEach(classId -> {
            List<String> studentIdList = myRepository.selectStudentIdByClassId(classId);
            //遍历查询每个学生
            studentIdList.forEach((id) -> {
                //登陆返回 html
                String loginPage = spider.login(id, id);
                if (loginPage != null) {
                    //获取 sid，不为空说明登陆成功
                    String sid = htmlUtil.getSid(loginPage);
                    if (sid != null) {
                        //查看学生选题页面
                        String selectionPage = spider.studentSelection(sid);
                        if (selectionPage != null) {
                            //从学生选题中提取所有的 project 并保存
                            List<List<String>> projects = htmlUtil.getData(selectionPage);
                            if (projects != null) {
                                projects.forEach((project) ->
                                        myRepository.saveProject(Project.buildeByList(id, project)));
                            }
                        }
                    }
                }
            });
            log.info(classId + "班\t完成");
        });
    }

    /**
     * 导出Excel方便数据分析
     */
    public void exportExcel() throws FileNotFoundException {
        String fileName = ResourceUtils.getURL("classpath:").getPath() + "result.xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, ExcelObj.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();
        //以班为单位
        List<String> classIdList = myRepository.selectAllClassId();
        classIdList.forEach(classId -> {
            List<ExcelObj> rows = myRepository.selectDataByClassId(classId);
            excelWriter.write(rows, writeSheet);
        });
        excelWriter.finish();
    }
}