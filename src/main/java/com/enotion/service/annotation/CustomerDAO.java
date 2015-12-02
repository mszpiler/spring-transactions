package com.enotion.service.annotation;

import com.enotion.model.Customer;
import org.springframework.transaction.annotation.Transactional;

interface CustomerDAO {

    void insertCustomer(Customer customer);

    void cleanPerson();

    void cleanAddress();

    int getPersonVol();

    int getAddressVol();
}
