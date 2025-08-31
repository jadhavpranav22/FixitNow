package com.household_repair.entity;

public enum BookingStatus {
    PENDING,
    PENDING_ADMIN_APPROVAL,
    ASSIGNED_TO_CONTRACTOR,
    ACCEPTED_BY_CONTRACTOR,
    REJECTED_BY_CONTRACTOR,
    COMPLETED
}
