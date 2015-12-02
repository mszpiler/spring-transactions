package com.enotion.service.annotation;

import com.enotion.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;

public class CustomerDAOImpl implements CustomerDAO {

    private DataSource dataSource;

    public void insertCustomer(Customer customer) {
        String queryPerson = "insert into person(first_name, last_name) values (?, ?)";
        String queryAddress = "insert into address(city, street) values (?, ?)";

        JdbcTemplate template = new JdbcTemplate(dataSource);

        template.update(queryPerson, new Object[]{customer.getFirstName(), customer.getLastName()});
        System.out.println("Inserted into PERSON Table Successfully!");

        template.update(queryAddress, new Object[]{customer.getCity(), customer.getStreet()});
        System.out.println("Inserted into ADDRESS Table Successfully!");
    }

    public void cleanPerson() {
        String queryPerson = "delete from person";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update(queryPerson, new Object[]{});
    }

    public void cleanAddress() {
        String queryPerson = "delete from address";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update(queryPerson, new Object[]{});
    }

    public int getPersonVol() {
        String sql = "SELECT count(*) FROM PERSON";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        Integer vol = (Integer) template.queryForObject(
                sql, new Object[]{}, Integer.class);
        return vol;
    }

    public int getAddressVol() {
        String sql = "SELECT count(*) FROM ADDRESS";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        Integer vol = template.queryForObject(
                sql, new Object[]{}, Integer.class);
        return vol;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
