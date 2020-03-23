package xin.justcsl.bysjspider.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author nullptr
 * @date 2020/2/16 17:31
 */
@Data
@Builder
public class Project {
    /**
     * 学生学号
     */
    private String studentId;
    /**
     * 课题名
     */
    private String name;
    /**
     * 课题类型
     */
    private String type;
    /**
     * 课题性质
     */
    private String property;
    /**
     * 教师名
     */
    private String teacher;

    /**
     * 根据 List 创建对象，传入的 List 必须是 {@code <CustomHtmlUtil.getData()>} 生成的
     */
    public static Project buildeByList(String studentId, List<String> data) {
        String name = data.get(0);
        String type = data.get(1);
        String property = data.get(2);
        String teacher = data.get(3);
        return Project.builder().studentId(studentId).name(name).type(type).property(property).teacher(teacher).build();
    }
}
