package com.majorproject2k19.MajorProject2k19.controller;

import com.majorproject2k19.MajorProject2k19.model.Ticket;
import com.majorproject2k19.MajorProject2k19.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    @Autowired
    private TicketService service;

    @RequestMapping(value = "/smartindia/tickets", method = RequestMethod.GET)
    public List<Ticket> getAlltickets() {
        return service.getAllTickets();
    }

    @RequestMapping(value = "/smartindia/tickets", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTicket(@RequestBody Ticket ticket) {
        System.out.println("in the post " + ticket.getDatecreated() + ticket.getDepartmentId() + " " + ticket.getId() + " " + ticket.getSubject() + " " + ticket.getCustomerid() + " " + ticket.getDescription());
        service.addTicket(ticket);
    }

    @RequestMapping(value = "/smartindia/tickets/{id}", method = RequestMethod.DELETE)
    public void deleteTicket(@PathVariable String id) {
        service.deleteTicket(id);
    }

    @RequestMapping(value = "/smartindia/tickets/{id}", method = RequestMethod.GET)
    public Ticket getTicket(@PathVariable String id) {
        return service.getTicket(id);
    }
}