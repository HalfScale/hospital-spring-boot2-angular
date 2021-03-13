package com.springboot.hospital.controller;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.dto.RefreshTokenRequest;
import com.springboot.hospital.entity.AuthenticationResponse;
import com.springboot.hospital.entity.LoginRequest;
import com.springboot.hospital.entity.RegistrationForm;
import com.springboot.hospital.entity.Response;
import com.springboot.hospital.entity.User;
import com.springboot.hospital.handler.OnRegistrationCompleteEvent;
import com.springboot.hospital.service.RefreshTokenService;
import com.springboot.hospital.service.UserService;
import com.springboot.hospital.util.Utils;
import com.springboot.hospital.validator.HospitalValidationSequence;


@RestController
public class UserController {
	
	
}
