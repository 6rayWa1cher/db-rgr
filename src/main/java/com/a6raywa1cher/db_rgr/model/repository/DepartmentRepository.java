package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.model.Department;

public class DepartmentRepository extends CrudRepository<Department> {
    public DepartmentRepository() {
        super(Department.class);
    }
}
