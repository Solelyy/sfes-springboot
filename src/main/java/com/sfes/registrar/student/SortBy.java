package com.sfes.registrar.student;

public enum SortBy {
    STUDENT_ID("studentId"),
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
