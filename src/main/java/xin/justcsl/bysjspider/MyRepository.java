package xin.justcsl.bysjspider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xin.justcsl.bysjspider.entity.ExcelObj;
import xin.justcsl.bysjspider.entity.Project;

import java.util.List;

/**
 * 全部的数据库交互代码，因为比较简单放在一个类中
 *
 * @author nullptr
 * @date 2020/2/16 17:26
 */
@Repository
public class MyRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> selectAllClassId() {
        String sql = "SELECT id FROM class;";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * 分页查询
     */
    public List<String> selectStudentIdByClassId(String classId) {
        String sql = "SELECT id FROM student WHERE class_id = ?;";
        return jdbcTemplate.queryForList(sql, String.class, classId);
    }

    /**
     * 存 Project
     */
    public void saveProject(Project project) {
        String sql = "INSERT INTO project(student_id, name, type, property, teacher) VALUE (?,?,?,?,?);";
        jdbcTemplate.update(sql, project.getStudentId(), project.getName(), project.getType(), project.getProperty(), project.getTeacher());
    }

    /**
     * 读数据
     */
    public List<ExcelObj> selectDataByClassId(String classId) {
        String sql = "select c.college,\n" +
                "       c.name as department,\n" +
                "       s.id number,\n" +
                "       s.name as sName,\n" +
                "       s.sex,\n" +
                "       p.name as pName,\n" +
                "       p.property,\n" +
                "       p.type,\n" +
                "       p.teacher\n" +
                "from class c,\n" +
                "     student s,\n" +
                "     project p\n" +
                "where c.id = ?" +
                "  and c.id = s.class_id\n" +
                "  and s.id = p.student_id;";
        return jdbcTemplate.query(sql, (rs, i) -> ExcelObj.builder()
                .college(rs.getString("college"))
                .department(rs.getString("department")).number(rs.getString("number"))
                .sname(rs.getString("sName")).sex(rs.getString("sex"))
                .pname(rs.getString("pName")).property(rs.getString("property"))
                .type(rs.getString("type")).teacher(rs.getString("teacher")).build(), classId);
    }
}
