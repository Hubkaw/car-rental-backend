package com.fumology.carrental.enums;

public enum UserRole {
    USER,
    ADMIN;

    public String getScope() {
        return "SCOPE_" + this.name();
    }
}
