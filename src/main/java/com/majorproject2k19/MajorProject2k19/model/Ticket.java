package com.majorproject2k19.MajorProject2k19.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    private String id;
    private String customerid, subject, departmentId, description, datecreated;

    public Ticket() {

    }

    public Ticket(String id, String customerid, String subject, String departmentId, String description, String datecreated) {
//        super();
        this.id = id;
        this.customerid = customerid;
        this.subject = subject;
        this.departmentId = departmentId;
        this.description = description;
        this.datecreated = datecreated;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getDescription() {
        return description;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public String getCustomerid() {
        return customerid;
    }
}
//{
//        "id": "101",
//        "customerId": "415",
//        "subject": "Unsupported Media Type",
//        "department": "Content type 'multipart/form-data;boundary=----WebKitFormBoundaryuxXAGRnzlfFjXl39;charset=UTF-8' not supported",
//        "description": "/smartindia/tickets",
//        "dateCreated":"27-2-2019"
//        }