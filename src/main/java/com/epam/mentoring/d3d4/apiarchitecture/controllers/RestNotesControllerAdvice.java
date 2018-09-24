package com.epam.mentoring.d3d4.apiarchitecture.controllers;

import com.epam.mentoring.d3d4.apiarchitecture.exception.ResourceDoesNotExistException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Aliaksandr Makavetski
 */
@ControllerAdvice
public class RestNotesControllerAdvice {


    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(IllegalArgumentException ex,
                                               HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler({ResourceDoesNotExistException.class, EmptyResultDataAccessException.class})
    public void handleResourceDoesNotExistException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(),
                "The resource '" + request.getRequestURI() + "' does not exist");
    }
}