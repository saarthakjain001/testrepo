package com.example.sessionservice.service;

import java.util.Map;
import java.util.Random;

import com.example.sessionservice.domain.Sessions;
import com.example.sessionservice.domain.Sessions.statusValues;
import com.example.sessionservice.repository.SessionRepository;
import com.example.sessionservice.service.exceptions.SessionIdInvalidException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sesrepository;

    SessionServiceImpl(SessionRepository sesrepository) {
        this.sesrepository = sesrepository;
    }

    //TODO 20-7-2020 Improve the random id generation algorithm to ensure that the generated sessionId is unique  
    @Override
    public Sessions generateSession(Map<String, String> details) {
        if (sesrepository.findByUsernameAndStatus(details.get("username"), statusValues.ACTIVE) == null) {
            Random rand = new Random();

            Sessions newSession = new Sessions(details.get("username"), rand.nextLong());
            sesrepository.save(newSession);
            return newSession;
        } else {
            Sessions temp = sesrepository.findByUsernameAndStatus(details.get("username"), statusValues.ACTIVE);
            temp.setStatus(statusValues.INACTIVE);
            sesrepository.save(temp);
            Random rand = new Random();

            Sessions newSession = new Sessions(details.get("username"), rand.nextLong());
            sesrepository.save(newSession);
            return newSession;
        }
    }

    //TODO 27/7/2020 Use a no argument constructor for throwing the SessionIdInvalidException
    @Override
    public Sessions endSession(Long sessionId) {
        // TODO Auto-generated method stub
        Sessions session = sesrepository.getOne(sessionId);
        if (session == null)
            throw new SessionIdInvalidException(String.format("The session Id = %s is invalid", sessionId));
        if (session.getStatus().equals(statusValues.INACTIVE))
            throw new SessionIdInvalidException(String.format("The session Id = %s is invalid", sessionId));
        session.setStatus(statusValues.INACTIVE);
        sesrepository.save(session);
        return session;
    }

    //TODO 15-7-2020 Use java streams to optimise the code
    @Override
    public Sessions validateSession(Long sessionId) {
        if (sesrepository.findByIdAndStatus(sessionId, statusValues.ACTIVE) == null)
            return null;
        Sessions session = sesrepository.findByIdAndStatus(sessionId, statusValues.ACTIVE);
        if (session.getEndTime() < System.currentTimeMillis()) {
            session.setStatus(statusValues.INACTIVE);
            sesrepository.save(session);
            return null;
        }
        return sesrepository.getOne(sessionId);
    }

}