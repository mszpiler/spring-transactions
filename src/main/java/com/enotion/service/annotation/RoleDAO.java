package com.enotion.service.annotation;

import com.enotion.model.Role;

interface RoleDAO {
    void insertRole(Role role);

    void cleanRoles();

    Integer getRolesVol();
}
