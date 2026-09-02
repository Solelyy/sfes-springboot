package com.sfes.user.enums;

public enum Status {
    PENDING,
    ACTIVE,
    INACTIVE,
    REMOVED;

    public boolean canTransitionTo(Status newStatus){
        return switch (this) {
            case PENDING, INACTIVE -> newStatus == ACTIVE || newStatus == REMOVED;

            case ACTIVE -> newStatus == INACTIVE || newStatus == REMOVED;

            case REMOVED -> false;
        };
    }
}
