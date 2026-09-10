package com.vidhya.supplyconnect.service;

import com.vidhya.supplyconnect.dto.PurchaseOrderResponse;
import com.vidhya.supplyconnect.entity.PurchaseOrder;
import com.vidhya.supplyconnect.entity.Role;
import com.vidhya.supplyconnect.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
