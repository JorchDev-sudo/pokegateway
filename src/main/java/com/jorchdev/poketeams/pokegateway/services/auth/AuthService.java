package com.jorchdev.poketeams.pokegateway.services.auth;

import com.jorchdev.poketeams.pokegateway.dtos.requests.LoginRequestDto;
import com.jorchdev.poketeams.pokegateway.dtos.requests.RegisterRequestDto;
import com.jorchdev.poketeams.pokegateway.dtos.responses.LoginResponse;
import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponse;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.exceptions.BadCredentialsException;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerNotFoundException;
import com.jorchdev.poketeams.pokegateway.mappers.TrainerMapper;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import com.jorchdev.poketeams.pokegateway.services.TrainerService;
import com.jorchdev.poketeams.pokegateway.services.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final TrainerService trainerService;
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthService(
            TrainerService trainerService,
            TrainerRepository trainerRepository,
            TrainerMapper trainerMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager)
    {
        this.trainerService = trainerService;
        this.trainerRepository = trainerRepository;
        this.trainerMapper = trainerMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse register(
            RegisterRequestDto request) {

        Trainer trainer = trainerMapper.toEntity(request);

        if (trainerRepository.existsByName(trainer.getName())) {
            throw new BadCredentialsException(
                    "Name already in use");
        }

        if (trainerRepository.existsByEmail(trainer.getEmail())) {
            throw new BadCredentialsException(
                    "Email already in use");
        }

        trainer.setPassword(
                passwordEncoder.encode(
                        request.password));

        TrainerResponse savedTrainer =
                trainerService.createTrainer(trainer);

        String token =
                jwtService.generateToken(savedTrainer.id());

        return new LoginResponse(
                savedTrainer,
                token);
    }

    public LoginResponse login(
            LoginRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email,
                        request.password
                )
        );

        Trainer trainer =
                trainerRepository.findByEmail(
                                request.email)
                        .orElseThrow(() ->
                                new TrainerNotFoundException(
                                        "Trainer not found"));

        String token =
                jwtService.generateToken(
                        trainer.getId());

        return new LoginResponse(
                trainerMapper.toDto(trainer),
                token
        );
    }
}
