package com.majorproject2k19.MajorProject2k19.service;

import com.majorproject2k19.MajorProject2k19.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        ticketRepository.findAll().forEach(tickets::add);
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public Ticket getTicket(String id)
    {
        Optional<Ticket> byId = ticketRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        else
            return null;
    }
    public void deleteTicket(String id)
    {
        ticketRepository.deleteById(id);
    }

}
