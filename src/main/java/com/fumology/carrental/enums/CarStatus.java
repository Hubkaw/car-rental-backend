package com.fumology.carrental.enums;

public enum CarStatus {
    AVAILABLE,
    RENTED,
    OVERDUE,
    RETURNED,
    SCHEDULED;

    public int getOrder(){
        return switch (this) {
            case AVAILABLE -> 4;
            case RETURNED -> 3;
            case SCHEDULED -> 2;
            case RENTED -> 1;
            case OVERDUE -> 0;
        };
    }
}
