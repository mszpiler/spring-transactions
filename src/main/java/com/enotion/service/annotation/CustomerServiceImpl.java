package com.enotion.service.annotation;

import com.enotion.model.Customer;
import com.enotion.model.Role;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDAO;

    private RoleService roleService;

    /**
     * In this example there is no @Transaction annotation - so this method is not transactional,
     * if you have more than one operation on dataSource then every of then are executed and commited independently!
     * @param customer
     */
    public void createCustomerNoTransactional(Customer customer) {
        this.customerDAO.insertCustomer(customer);
    }

    /**
     * In this example method is transactional (because of @Transactional annotation) so if you have more
     * than one operations on dataSource then all will be rolledback if something is wrong
     * @param customer
     */
    @Transactional
    public void createCustomerTransactional(Customer customer) {
        this.customerDAO.insertCustomer(customer);
    }


    public void createCustomerComplexInsideMandatory(Customer customer, Role role) {
        this.roleService.createRoleTransactionIsMandatory(role);
        this.customerDAO.insertCustomer(customer);
    }

    public void createCustomerComplexInsideSupport_NoTransOutside(Customer customer, Role role) {
        this.roleService.createRoleTransactionSupport(role);
        this.customerDAO.insertCustomer(customer);
    }


    @Transactional
    public void createCustomerComplexInsideSupport_WithTransOutside(Customer customer, Role role) {
        this.roleService.createRoleTransactionSupport(role);
        this.customerDAO.insertCustomer(customer);
    }

    public void createCustomerComplexInsideNotSupported_NoTransOutside(Customer customer, Role role) {
        this.roleService.createRoleTransactionNotSupported(role);
        this.customerDAO.insertCustomer(customer);
    }

    @Transactional
    public void createCustomerComplexInsideNotSupported_WithTransOutside(Customer customer, Role role) {
        this.roleService.createRoleTransactionNotSupported(role);
        this.customerDAO.insertCustomer(customer);
    }

    public void createCustomerComplexInsideNevetTrans_NoTransOutside(Customer customer, Role role) {
        this.roleService.createRoleNeverTransaction(role);
        this.customerDAO.insertCustomer(customer);
    }

    @Transactional
    public void createCustomerComplexInsideNevetTrans_WithTransOutside(Customer customer, Role role) {
        this.roleService.createRoleNeverTransaction(role);
        this.customerDAO.insertCustomer(customer);
    }

    /**
     * Warning! order is changed - first create customer, second create role - for test clarity
     */
    public void createCustomerComplexNewRequired_NoTransOutside(Customer customer, Role roleOne, Role roleTwo) {
        this.customerDAO.insertCustomer(customer);
        this.roleService.createRolesRequiredNewtransaction(roleOne, roleTwo);
    }

    /**
     * Warning! order is changed - first create customer, second create role - for test clarity
     * */
    @Transactional
    public void createCustomerComplexNewRequired_WithTransOutside(Customer customer, Role roleOne, Role roleTwo) {
        this.customerDAO.insertCustomer(customer);
        this.roleService.createRolesRequiredNewtransaction(roleOne, roleTwo);
    }

    /**
     * Interesting example shows that there is no transaction inside createSomeCustomerForProxyBasedTest even if method createCustomerComplexProxyBasedExample is transactional !!!
     * Beacause of proxy based way in Spring AOP
     * @param customerOne
     * @param customerTwo
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createCustomerComplexProxyBasedExample(Customer customerOne, Customer customerTwo) {
        this.customerDAO.insertCustomer(customerOne);
        this.createSomeCustomerForProxyBasedTest(customerTwo);
    }

    public void createSomeCustomerForProxyBasedTest(Customer customer) {
        this.customerDAO.insertCustomer(customer);
    }

    @Transactional
    public void cleanDatabase() {
        this.customerDAO.cleanPerson();
        this.customerDAO.cleanAddress();
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
}
