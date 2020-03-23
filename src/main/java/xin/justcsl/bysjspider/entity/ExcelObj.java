package xin.justcsl.bysjspider.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author nullptr
 * @date 2020/2/21 20:59
 */
@Data
@Builder
public class ExcelObj {

    @ExcelProperty(value = "学院", index = 0)
    private String college;
    @ExcelProperty(value = "专业", index = 1)
    private String department;
    @ExcelProperty(value = "学号", index = 2)
    private String number;

    /**
     * sname 理应命名为 sName，
     * 但是 easyExcel 有个小 bug，如果变量名带两个大写字母不能正常导出的 excel 文件
     * 下面 pname 原因相同
     */
    @ExcelProperty(value = "姓名", index = 3)
    private String sname;
    @ExcelProperty(value = "性别", index = 4)
    private String sex;
    @ExcelProperty(value = "课题名", index = 5)
    private String pname;
    @ExcelProperty(value = "课题类型", index = 6)
    private String type;
    @ExcelProperty(value = "课题性质", index = 7)
    private String property;
    @ExcelProperty(value = "指导教师", index = 8)
    private String teacher;
}
