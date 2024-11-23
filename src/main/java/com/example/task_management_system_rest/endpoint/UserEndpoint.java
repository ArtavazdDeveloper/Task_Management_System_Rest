package com.example.task_management_system_rest.endpoint;

import com.example.task_management_system_rest.dto.userDTO.*;
import com.example.task_management_system_rest.entity.Role;
import com.example.task_management_system_rest.entity.User;
import com.example.task_management_system_rest.mapper.UserMapper;
import com.example.task_management_system_rest.security.CurrentUser;
import com.example.task_management_system_rest.service.UserService;
import com.example.task_management_system_rest.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserEndpoint {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil tokenUtil;

    @PostMapping()
    public ResponseEntity<CreateUserResponseDTO> register(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        Optional<User> getUserByEmail = userService.getUserByEmail(createUserRequestDTO.getEmail());
        if (getUserByEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        }
        User user = userMapper.mapToUser(createUserRequestDTO);
        user.setPassword(passwordEncoder.encode(createUserRequestDTO.getPassword()));
        user.setRole(Role.USER);

        User save = userService.save(user);
        return ResponseEntity.ok(userMapper.mapToDto(save));
    }

    @PostMapping("/auth")
    public ResponseEntity<UserAuthResponseDTO> login(@RequestBody LoginUserResponseDTO loginUserResponseDto) {
        Optional<User> getUserByEmail = userService.getUserByEmail(loginUserResponseDto.getUsername());
        if (getUserByEmail.isPresent() &&
                passwordEncoder.matches(loginUserResponseDto.getPassword(), getUserByEmail.get().getPassword())) {
            String token = tokenUtil.generateToken(loginUserResponseDto.getUsername());
            return ResponseEntity.ok(new UserAuthResponseDTO(token));


        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateUserResponseDTO> getUserById(@PathVariable int id) {
        Optional<User> getUserById = userService.getUserById(id);
        return getUserById.map(user -> ResponseEntity.ok(userMapper.mapToDto(user))).orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());

    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<CreateUserResponseDTO> deleteUserById(@PathVariable int id,
                                                                @AuthenticationPrincipal CurrentUser currentUser) {

        Optional<User> getUserById = userService.getUserById(id);
        if (getUserById.isPresent() && currentUser.getUser().getRole().name().equals(Role.ADMIN.name())) {
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.OK).build();

        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    @ResponseBody
    @PutMapping()
    public ResponseEntity<CreateUserResponseDTO> updateSelfData(@RequestBody UpdateUserRequestDTO updateUserRequestDTO,
                                                                @AuthenticationPrincipal CurrentUser currentUser) {

        if (currentUser.getUser().getEmail().equals(updateUserRequestDTO.getEmail())) {
            userService.updateUserData(userMapper.mapToUser(updateUserRequestDTO));
            return ResponseEntity.status(HttpStatus.OK).build();

        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }
}
