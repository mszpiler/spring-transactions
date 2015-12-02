package com.enotion.service.annotation;

import com.enotion.model.Customer;
import com.enotion.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-config.xml"})
public class CustomerServiceImplTest {

    @Autowired
    ApplicationContext context;

    private Customer customerWithTooLongCity;
    private Customer okCustomer;
    private Role okRole;
    private Role wrongRole;

    private CustomerService customerService;
    private RoleService roleService;
    private CustomerDAO customerDAO;
    private RoleDAO roleDAO;


    @Before
    public void setup() {
        customerWithTooLongCity = new Customer();
        customerWithTooLongCity.setFirstName("John");
        customerWithTooLongCity.setLastName("Foo");
        customerWithTooLongCity.setCity("Some City");
        customerWithTooLongCity.setStreet("More than 15 character then will be rollback");

        okCustomer = new Customer();
        okCustomer.setFirstName("John");
        okCustomer.setLastName("Foo");
        okCustomer.setCity("Some City");
        okCustomer.setStreet("Sezam");

        okRole = new Role();
        okRole.setName("ADMIN");

        wrongRole = new Role();
        wrongRole.setName("Very long name which makes exception during insertion this row to database.");

        customerService = (CustomerService) context.getBean("customerService");
        roleService = (RoleService) context.getBean("roleService");
        customerDAO = (CustomerDAO) context.getBean("customerDao");
        roleDAO = (RoleDAO) context.getBean("roleDao");

    }

    /**
     * In this example method is not transactional
     *
     * @throws Exception
     */
    @Test
    public void shouldRollbackOnlyAddress() throws Exception {
        try {
            //customer is wrong - it is going to be exception
            customerService.createCustomerNoTransactional(customerWithTooLongCity);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            assertEquals(new Integer(0), addressVol);
            assertEquals(new Integer(1), personVol);
        }
    }

    /**
     * In this case method is transactional
     *
     * @throws Exception
     */
    @Test
    public void shouldRollbackPersonAndAddress() throws Exception {
//        customerService.cleanDatabase();
        try {
            //customer is wrong - it is going to be exception
            customerService.createCustomerTransactional(customerWithTooLongCity);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            assertEquals(new Integer(0), addressVol);
            assertEquals(new Integer(0), personVol);
        }
    }


    /**
     * In this case inside method createCustomerComplexInsideMandatory is execution method from other service and transaction is mandatory for this execution
     *
     * @throws Exception
     */
    @Test(expected = IllegalTransactionStateException.class)
    public void shouldThrowExceptionMandatoryTransaction() throws Exception {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        customerService.createCustomerComplexInsideMandatory(okCustomer, okRole);
    }

    /**
     * In this example there isn't transaction during execution createCustomerComplexInsideSupport_NoTransOutside, exception is thrown during customer creation
     */
    @Test
    public void shouldCreateRoleandPersonNoAddress() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        try {
            //customer is wrong - it is going to be exception, first roles are created
            customerService.createCustomerComplexInsideSupport_NoTransOutside(customerWithTooLongCity, okRole);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            Integer roleVol = roleDAO.getRolesVol();

            //records created by insertCustomer method
            assertEquals(new Integer(0), addressVol);
            assertEquals(new Integer(1), personVol);

            //record created by createRoleTransactionSupport method
            assertEquals(new Integer(1), roleVol);
        }
    }

    /**
     * In this example there is transaction during execution createCustomerComplexInsideSupport_Trans, exception is thrown during customer creation
     */
    @Test
    public void shouldRollbackAllCreationRoleAddressAndPerson() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        try {
            //customer is wrong - it is going to be exception - first roles are created
            customerService.createCustomerComplexInsideSupport_WithTransOutside(customerWithTooLongCity, okRole);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            Integer roleVol = roleDAO.getRolesVol();

            //records created by insertCustomer method
            assertEquals(new Integer(0), addressVol);
            assertEquals(new Integer(0), personVol);

            //record created by createRoleTransactionSupport method
            assertEquals(new Integer(0), roleVol);
        }
    }

    /**
     * Exception is thrown during customer creation, no transation and no support transaction inside role creation
     */
    @Test
    public void shouldCreatePersonAndRolesButNoAddress() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        try {
            //customer is wrong - it is going to be exception
            customerService.createCustomerComplexInsideNotSupported_NoTransOutside(customerWithTooLongCity, okRole);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            Integer roleVol = roleDAO.getRolesVol();

            //records created by insertCustomer method - no transaction so executed independently but in address insertion no because of exception - in this case is rollback
            assertEquals(new Integer(0), addressVol);
            assertEquals(new Integer(1), personVol);

            //record created by createRoleTransactionNotSupported method - no transaction so commited
            assertEquals(new Integer(1), roleVol);
        }
    }

    /**
     * Exception is thrown during customer creation, no transation and no support transaction inside role creation
     */
    @Test
    public void shouldCreatePersonAndRolesButNoAddress2() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        try {
            //customer is wrong - it is going to be exception
            customerService.createCustomerComplexInsideNotSupported_WithTransOutside(customerWithTooLongCity, okRole);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            Integer roleVol = roleDAO.getRolesVol();

            //records created by insertCustomer method - transactional so rollback all inserts in this method, rollback because of problem during inserting address
            assertEquals(new Integer(0), addressVol);
            assertEquals(new Integer(0), personVol);

            //record created by createRoleTransactionNotSupported method - transaction is not supported even if outside method is transactional - so in this example role record is commited
            assertEquals(new Integer(1), roleVol);
        }
    }

    @Test
    public void testNeverTransactionalShouldPassThrough() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        try {

            //customer is wrong - it is going to be exception
            customerService.createCustomerComplexInsideNevetTrans_NoTransOutside(customerWithTooLongCity, okRole);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            Integer roleVol = roleDAO.getRolesVol();

            //records created by insertCustomer method - no transactional so address is created and commited and then exception and rollback on person
            assertEquals(new Integer(0), addressVol);
            assertEquals(new Integer(1), personVol);

            //record created by createRoleNeverTransaction method - no transactional so role is commited - as a first line of execution inside customerService.createCustomerComplexInsideNevetTrans_NoTransOutside method
            assertEquals(new Integer(1), roleVol);
        }
    }

    /**
     * In this case exception because outside were created transaction and inside transaction is not allowed
     */
    @Test(expected = IllegalTransactionStateException.class)
    public void testNeverTransactionalShouldThrowException() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        //customer is wrong - it is going to be exception
        customerService.createCustomerComplexInsideNevetTrans_WithTransOutside(customerWithTooLongCity, okRole);
    }

    /**
     * In this case
     */
    @Test
    @Transactional
    public void shouldCreateNewTransactionForCreateRoles() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        try {
            //customer is OK, but wrongRole has too long name
            customerService.createCustomerComplexNewRequired_NoTransOutside(okCustomer, okRole, wrongRole);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            Integer roleVol = roleDAO.getRolesVol();

            //address and person are commited independently (because of no transactional) but commited
            assertEquals(new Integer(1), addressVol);
            assertEquals(new Integer(1), personVol);

            //here second role was to long and exception was thrown but bacause of transaction required (and created) all steps in this method are rolledback
            assertEquals(new Integer(0), roleVol);
        }
    }

    /**
     * Despite no transactional outside result is the same as shouldCreateNewTransactionForCreateRoles()
     */
    @Test
    @Transactional
    public void shouldCreateNewTransactionForCreateRoles2() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        try {
            //customer is OK, but wrongRole has too long name
            customerService.createCustomerComplexNewRequired_WithTransOutside(okCustomer, okRole, wrongRole);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            Integer roleVol = roleDAO.getRolesVol();

            //address and person are commited not independently (because of transactional) - result is identical like in shouldCreateNewTransactionForCreateRoles() test
            assertEquals(new Integer(1), addressVol);
            assertEquals(new Integer(1), personVol);

            //here second role was to long and exception was thrown but bacause of transaction required (and created) all steps in this method are rolledback
            assertEquals(new Integer(0), roleVol);
        }
    }

    @Test
    @Transactional
    public void shouldNotCreateAddressInSecondCustomer() {
        customerService.cleanDatabase();
        roleService.cleanDatabase();
        try {
            //first customer is ok, second has wrong data too long address name so there will be exception
            customerService.createCustomerComplexProxyBasedExample(okCustomer, customerWithTooLongCity);
        } catch (Exception e) {
            Integer addressVol = customerDAO.getAddressVol();
            Integer personVol = customerDAO.getPersonVol();
            System.out.println("Address vol ="+addressVol+" personVol="+personVol);
            //first person from okCustomer, second person from customerWithTooLongCity
            assertEquals(new Integer(2), personVol);
            //first address from okCustomer, second not exists because of exception and rollback in this step
            assertEquals(new Integer(1), addressVol);

            /**
             * This assertion shows that in second customer creation there is no transactional because person was created - only addres was rolledback
             */

        }

    }

}