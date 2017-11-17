package com.mycompany.container.pojos;

import java.util.List;

public class Company {
    private int id;
    private List<Department> departments;

    public Company(int id, List<Department> departments) {
        this.id = id;
        this.departments = departments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
