package com.a6raywa1cher.db_rgr.model;


import com.a6raywa1cher.db_rgr.lib.Column;
import com.a6raywa1cher.db_rgr.lib.Entity;
import lombok.Data;

@Data
@Entity("department")
public class Department {
    @Column(value = "title", pk = true)
    private String title;

    @Column(value = "telephone_number")
    private String telephoneNumber;

    @Column(value = "address")
    private String address;
}
