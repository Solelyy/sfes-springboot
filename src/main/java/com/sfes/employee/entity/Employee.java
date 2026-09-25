package com.sfes.employee.entity;

import com.sfes.common.classes.BaseEntity;
import com.sfes.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "employee_seq",
            sequenceName = "employee_seq",
            initialValue = 2,
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    private Long id;

    @Column(name = "employee_id", nullable = false, unique = true, length = 30)
    private String employeeId;

    @Column(name = "first_name", nullable = false, length = 100)
    private  String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
