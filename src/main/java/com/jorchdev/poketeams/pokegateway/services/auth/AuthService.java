package com.jorchdev.poketeams.pokegateway.services.auth;

import com.jorchdev.poketeams.pokegateway.dtos.requests.LoginRequestDto;
import com.jorchdev.poketeams.pokegateway.dtos.requests.RegisterRequestDto;
import com.jorchdev.poketeams.pokegateway.dtos.responses.LoginResponse;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.DuplicateTrainerException;
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
    private final TrainerMapper trainerMapper;
    private final PasswordEncoder passwordEncoder;
    private final TrainerRepository trainerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            TrainerService trainerService,
            TrainerMapper trainerMapper,
            PasswordEncoder passwordEncoder,
            TrainerRepository trainerRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager)
    {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
        this.passwordEncoder = passwordEncoder;
        this.trainerRepository = trainerRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse register(
            RegisterRequestDto request) {

        Trainer trainer = trainerMapper.toEntity(request);

        if (trainerRepository.existsByName(trainer.getName())) {
            throw new DuplicateTrainerException(
                    "Name already in use");
        }

        if (trainerRepository.existsByEmail(trainer.getEmail())) {
            throw new DuplicateTrainerException(
                    "Email already in use");
        }

        trainer.setPassword(
                passwordEncoder.encode(
                        request.password));

        Trainer savedTrainer =
                trainerService.createTrainer(trainer);

        String token =
                jwtService.generateToken(
                        savedTrainer.getEmail());

        return new LoginResponse(
                trainerMapper.toDto(savedTrainer),
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
                        trainer.getEmail());

        return new LoginResponse(
                trainerMapper.toDto(trainer),
                token
        );
    }
}
