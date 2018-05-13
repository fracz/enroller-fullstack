package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

//	@RequestMapping(value = "", method = RequestMethod.GET)
//	public ResponseEntity<?> getParticipants() {
//		Collection<Participant> participants = participantService.getAll();
//		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
//	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity(
					"Unable to create. A participant with login " + participant.getLogin() + " already exist.",
					HttpStatus.CONFLICT);
		}
		participantService.create(participant);

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody Participant credentials) throws ServletException {

		String jwtToken = "";

		if (credentials.getLogin() == null || credentials.getPassword() == null) {
			return new ResponseEntity("Please fill in username and password", HttpStatus.FORBIDDEN);
		}

		String login = credentials.getLogin();
		String password = credentials.getPassword();

		Participant user = participantService.findByLogin(login);

		if (user == null) {
			return new ResponseEntity("User login not found", HttpStatus.FORBIDDEN);
		}

		String userPassword = user.getPassword();
		System.out.print(userPassword);

		if (!password.equals(userPassword)) {
			return new ResponseEntity("Invalid login. Please check your name and password", HttpStatus.UNAUTHORIZED);
		}

		jwtToken = Jwts.builder().setSubject(login).claim("roles", "user").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey").compact();

		return new ResponseEntity(jwtToken, HttpStatus.OK);
	}
}
