package com.vidhya.supplyconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Long roleId;

    private Long buyerId;

    private Long supplierId;

    public UserRequest() {
    }

    public @NotBlank String getName() {
        return name;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public @NotNull Long getRoleId() {
        return roleId;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public void setRoleId(@NotNull Long roleId) {
        this.roleId = roleId;
    }
}