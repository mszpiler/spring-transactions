package com.enotion.service.annotation;

import com.enotion.model.Role;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RoleService {

    /**
     * Before execution of this method must be created transaction
     * @param role
     */
    @Transactional(propagation = Propagation.MANDATORY)
    void createRoleTransactionIsMandatory(Role role);

    @Transactional(propagation = Propagation.SUPPORTS)
    void createRoleTransactionSupport(Role role);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void createRoleTransactionNotSupported(Role role);

    @Transactional(propagation = Propagation.NEVER)
    void createRoleNeverTransaction(Role role);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void createRolesRequiredNewtransaction(Role roleOne, Role roleTwo);



    void cleanDatabase();
}
