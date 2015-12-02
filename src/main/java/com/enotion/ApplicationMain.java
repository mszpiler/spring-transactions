package com.enotion;

import com.enotion.model.Customer;
import com.enotion.service.annotation.CustomerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ApplicationMain {
    public static void main(String[] args) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("/spring-config.xml");
        CustomerService customerService = (CustomerService) ctx.getBean("customerService");

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Foo");
        customer.setCity("Some City");
        customer.setStreet("More than 15 character then will be rollback");
        customerService.createCustomerNoTransactional(customer);

        customerService.createCustomerTransactional(customer);

//
//        fooService.execWithRollback();
//        fooService.execWithourRollback();
    }
}
