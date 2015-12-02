package com.enotion.service.annotation;

import com.enotion.model.Role;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class RoleDAOImpl implements RoleDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRole(Role role) {
        String queryPerson = "insert into role(name) values (?)";
        JdbcTemplate template = new JdbcTemplate(dataSource);

        template.update(queryPerson, new Object[]{role.getName()});
        System.out.println("Inserted into ROLE Table Successfully!");
    }

    public void cleanRoles() {
        String queryPerson = "delete from role";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update(queryPerson, new Object[]{});
    }

    public Integer getRolesVol() {
        String sql = "SELECT count(*) FROM ROLE";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        Integer vol = (Integer) template.queryForObject(
                sql, new Object[] {}, Integer.class);

        return vol;
    }
}
