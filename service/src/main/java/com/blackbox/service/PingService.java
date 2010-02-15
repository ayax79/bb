package com.blackbox.service;

import org.springframework.stereotype.Service;

import javax.ws.rs.Path;
import javax.ws.rs.GET;

/**
 *
 * @author andrew
 */
@Service
public class PingService implements IPingService {

    public String ping() {
        return "ping";
    }

}
