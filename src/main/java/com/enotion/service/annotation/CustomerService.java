package com.enotion.service.annotation;

import com.enotion.model.Customer;
import com.enotion.model.Role;

public interface CustomerService {

    void createCustomerNoTransactional(Customer customer);

    void createCustomerTransactional(Customer customer);

    void createCustomerComplexInsideMandatory(Customer customer, Role role);


    void createCustomerComplexInsideSupport_NoTransOutside(Customer customer, Role role);
    void createCustomerComplexInsideSupport_WithTransOutside(Customer customer, Role role);

    void createCustomerComplexInsideNotSupported_NoTransOutside(Customer customer, Role role);
    void createCustomerComplexInsideNotSupported_WithTransOutside(Customer customer, Role role);

    void createCustomerComplexInsideNevetTrans_NoTransOutside(Customer customer, Role role);
    void createCustomerComplexInsideNevetTrans_WithTransOutside(Customer customer, Role role);

    void createCustomerComplexNewRequired_NoTransOutside(Customer customer, Role roleOne, Role roleTwo);
    void createCustomerComplexNewRequired_WithTransOutside(Customer customer, Role roleOne, Role roleTwo);

    void createCustomerComplexProxyBasedExample(Customer customerOne, Customer customerTwo);
    void createCustomerComplexProxyBasedExampleAllTransactional(Customer customerOne, Customer customerTwo);
    void createSomeCustomerForProxyBasedTest(Customer customer);
    void createSomeCustomerForProxyBasedTestTransactional(Customer customer);

    void cleanDatabase();

}
