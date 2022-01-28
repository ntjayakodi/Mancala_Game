package com.nipuna.bol.mancalabackend.handler;

import com.nipuna.bol.mancalabackend.domain.dto.MancalaGameExceptionResponseDTO;
import com.nipuna.bol.mancalabackend.exception.MancalaGameNotFoundException;
import com.nipuna.bol.mancalabackend.exception.MancalaGameOverException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPitIdException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPlayerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MancalaGameNotFoundException.class)
    public ResponseEntity<MancalaGameExceptionResponseDTO> handleMancalaGameNotFoundException(MancalaGameNotFoundException exception) {
        return new ResponseEntity<>(new MancalaGameExceptionResponseDTO(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MancalaInvalidPitIdException.class)
    public ResponseEntity<MancalaGameExceptionResponseDTO> handleInValidPitIdException(MancalaInvalidPitIdException exception) {
        return new ResponseEntity<>(new MancalaGameExceptionResponseDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MancalaGameOverException.class)
    public ResponseEntity<MancalaGameExceptionResponseDTO> handleGameOverException(MancalaGameOverException exception) {
        return new ResponseEntity<>(new MancalaGameExceptionResponseDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MancalaInvalidPlayerException.class)
    public ResponseEntity<MancalaGameExceptionResponseDTO> handleInvalidPlayerException(MancalaInvalidPlayerException exception) {
        return new ResponseEntity<>(new MancalaGameExceptionResponseDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}

