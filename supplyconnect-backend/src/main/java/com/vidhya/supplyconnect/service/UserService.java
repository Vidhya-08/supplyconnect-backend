package com.vidhya.supplyconnect.service;

import com.vidhya.supplyconnect.dto.UserRequest;
import com.vidhya.supplyconnect.dto.UserResponse;
import com.vidhya.supplyconnect.entity.Buyer;
import com.vidhya.supplyconnect.entity.Role;
import com.vidhya.supplyconnect.entity.Supplier;
import com.vidhya.supplyconnect.entity.User;
import com.vidhya.supplyconnect.repository.BuyerRepository;
import com.vidhya.supplyconnect.repository.RoleRepository;
import com.vidhya.supplyconnect.repository.SupplierRepository;
import com.vidhya.supplyconnect.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository, BuyerRepository buyerRepository, SupplierRepository supplierRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.buyerRepository = buyerRepository;
        this.supplierRepository = supplierRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse saveUser(UserRequest request) {

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        String roleName = role.getRoleName();

        if ("BUYER".equals(roleName)) {

            if (request.getBuyerId() == null || request.getSupplierId() != null) {
                throw new RuntimeException(
                        "BUYER must have buyerId and must not have supplierId"
                );
            }

        } else if ("SUPPLIER".equals(roleName)) {

            if (request.getSupplierId() == null || request.getBuyerId() != null) {
                throw new RuntimeException(
                        "SUPPLIER must have supplierId and must not have buyerId"
                );
            }

        } else if ("ADMIN".equals(roleName)) {

            if (request.getBuyerId() != null || request.getSupplierId() != null) {
                throw new RuntimeException(
                        "ADMIN must not be associated with a buyer or supplier"
                );
            }
        }

        if (request.getBuyerId() != null) {

            Buyer buyer = buyerRepository.findById(request.getBuyerId())
                    .orElseThrow(() -> new RuntimeException("Buyer not found"));

            user.setBuyer(buyer);
        }

        // 5. Set Supplier if supplierId is provided
        if (request.getSupplierId() != null) {

            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));

            user.setSupplier(supplier);
        }

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().getRoleName()
        );
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().getRoleName()
                ))
                .toList();
    }
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }
}