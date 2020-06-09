package com.ippon.employee.domain;


import com.fasterxml.jackson.annotation.JsonView;
import com.ippon.employee.domain.view.View;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @JsonView({View.Admin.class})
    private Long id;

    @NotNull
    @JsonView({View.User.class, View.Admin.class})
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @JsonView({View.User.class, View.Admin.class})
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonView(View.Admin.class)
    @Column(name = "salary", precision = 21, scale = 2)
    private BigDecimal salary;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Employee salary(BigDecimal salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", salary=" + getSalary() +
            "}";
    }
}
