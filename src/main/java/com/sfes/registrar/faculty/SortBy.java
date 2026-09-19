package com.sfes.registrar.faculty;

public enum SortBy {
    EMPLOYEE_ID("employeeId"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    CREATED_AT("createdAt"),
    STATUS("status");

    private final String property;

    SortBy(String property){
        this.property = property;
    }

    public String getProperty(){
        return property;
    }

}
