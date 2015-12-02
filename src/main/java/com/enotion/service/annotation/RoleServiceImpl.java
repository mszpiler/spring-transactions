package com.enotion.service.annotation;

import com.enotion.model.Role;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class RoleServiceImpl implements RoleService {

    private RoleDAO roleDAO;

    public void createRoleTransactionIsMandatory(Role role) {
        roleDAO.insertRole(role);
    }

    public void createRoleTransactionSupport(Role role) {
        roleDAO.insertRole(role);
    }

    public void createRoleTransactionNotSupported(Role role) {
        roleDAO.insertRole(role);
    }

    public void createRoleNeverTransaction(Role role) {
        roleDAO.insertRole(role);
    }

    public void createRolesRequiredNewtransaction(Role roleOne, Role roleTwo) {
        roleDAO.insertRole(roleOne);
        roleDAO.insertRole(roleTwo);
    }


    @Transactional
    public void cleanDatabase() {
        roleDAO.cleanRoles();
    }

    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }
}
