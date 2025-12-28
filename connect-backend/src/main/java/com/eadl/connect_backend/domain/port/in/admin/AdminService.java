package com.eadl.connect_backend.domain.port.in.admin;

import com.eadl.connect_backend.domain.model.user.Admin;

public interface AdminService {

    Admin createAdmin(Admin admin);

    void suspendUser(Long userId);
    
    void activateUser(Long userId);
}
