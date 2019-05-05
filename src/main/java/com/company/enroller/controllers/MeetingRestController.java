package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findByID(id);
	    if (meeting == null) 
	    {
	    	return new ResponseEntity(HttpStatus.NOT_FOUND);
	    }
	    return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	 }
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting)
	{
		 Meeting foundMeeting = meetingService.findByID(meeting.getId());
		 if (foundMeeting != null) 
		 {
			 return new ResponseEntity("Unable to create the meeting. A meeting with Id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
		 }
	     meetingService.add(meeting);
	     return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
	 @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id)
	{
		 Meeting meeting = meetingService.findByID(id);
	     if (meeting == null) 
	     {
	    	 return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	     meetingService.delete(meeting);
	     return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	 
	 @RequestMapping(value = "{id}", method = RequestMethod.PUT)
	 public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting incomingMeeting)
	 {
		 Meeting meeting = meetingService.findByID(id);
	     if (meeting == null) 
	     {
	    	 return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	     meeting.setDate(incomingMeeting.getDate());
	     meeting.setDescription(incomingMeeting.getDescription());
	     meeting.setTitle(incomingMeeting.getTitle());
	     meetingService.update(meeting);
	     return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	 }
	 
	 
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") Long id, @RequestBody Participant participant) {
		Meeting meeting = meetingService.findByID(id);
		if (meeting == null) {
			return new ResponseEntity("Unable to update. A meeting with id " + id + " does not exist.", HttpStatus.NOT_FOUND);
		} 
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant == null) {
			return new ResponseEntity("Unable to update. A participant with login " + participant.getLogin() + " does not exist.", HttpStatus.NOT_FOUND);
		} 
		meetingService.addParticipantToTheMeeting(id, foundParticipant);	
		return new ResponseEntity<Participant>(foundParticipant, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeParticipant(@PathVariable("id") Long id, @RequestBody Participant participant) {
		Meeting meeting = meetingService.findByID(id);
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (meeting == null || participant == null) {
			return new ResponseEntity("Unable to delete", HttpStatus.NOT_FOUND);
		} 
		meetingService.removeParticipant(id, foundParticipant);	
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipantFromMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findByID(id);
		Collection<Participant> participantList = meeting.getParticipants();
		Collection<String> userNames = new HashSet<>();
		for (Participant part:participantList) {
			userNames.add(part.getLogin());
		}
		return new ResponseEntity<Collection<Participant>>(participantList, HttpStatus.OK);
	}
}
