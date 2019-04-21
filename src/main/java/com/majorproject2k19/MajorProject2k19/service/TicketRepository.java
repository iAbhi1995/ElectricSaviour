package com.majorproject2k19.MajorProject2k19.service;

import com.majorproject2k19.MajorProject2k19.model.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, String> {
}
