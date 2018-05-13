package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings(@RequestParam(value = "title", defaultValue = "") String title,
			@RequestParam(value = "description", defaultValue = "") String description) {

		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting, UriComponentsBuilder ucBuilder) {
		if (meetingService.alreadyExist(meeting)) {
			return new ResponseEntity(new Error("Unable to create. A meeting with title " + meeting.getTitle()
					+ " and date " + meeting.getDate() + " already exist."), HttpStatus.CONFLICT);
		}
		meetingService.create(meeting);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/meeting/{id}").buildAndExpand(meeting.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {

		Meeting currentMeeting = meetingService.findById(id);

		if (currentMeeting == null) {
			return new ResponseEntity(new Error("Unable to update. Meeting with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentMeeting.setTitle(meeting.getTitle());
		currentMeeting.setDescription(meeting.getDescription());
		currentMeeting.setDate(meeting.getDate());

		meetingService.update(currentMeeting);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> joinMeeting(@PathVariable("id") long id) {

		Meeting currentMeeting = meetingService.findById(id);
		if (currentMeeting == null) {
			return new ResponseEntity(new Error("Unable to add participant. Meeting with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(currentMeeting.getParticipants(), HttpStatus.OK);
	}

	@RequestMapping(value = "{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> joinMeeting(@PathVariable("id") long id, @RequestBody Map<String, String> json) {

		Meeting currentMeeting = meetingService.findById(id);
		if (currentMeeting == null) {
			return new ResponseEntity("Unable to add participant. Meeting with id " + id + " not found",
					HttpStatus.NOT_FOUND);
		}

		String login = json.get("login");
		if (login == null) {
			return new ResponseEntity("Unable to find participant login in the request body", HttpStatus.BAD_REQUEST);
		}

		Participant participantToAdd = participantService.findByLogin(login);
		// Participant currentUser =
		// userService.findByLogin(claims.get("sub").toString());

		if (participantToAdd == null) {
			return new ResponseEntity(
					new Error("Unable to add participant. Participant with login " + login + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentMeeting.addParticipant(participantToAdd);
		meetingService.update(currentMeeting);

		return new ResponseEntity<Meeting>(currentMeeting, HttpStatus.OK);
	}

}