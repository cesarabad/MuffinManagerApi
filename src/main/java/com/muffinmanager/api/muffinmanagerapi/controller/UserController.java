package com.muffinmanager.api.muffinmanagerapi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginResponse;
import com.muffinmanager.api.muffinmanagerapi.model.User.RegisterRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.UserEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.GroupEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.stats.UserStatsEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.AvailableUserPermissionsDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UpdateUserDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserDetailedDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.auth.IUserService;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    private static final String BASE_URL = "/user";
    private static final String GROUP_TOPIC = "/topic/group";
    @Autowired
    private IUserService userService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            if (request.getDni() == null || request.getPassword() == null ||
                request.getDni().isEmpty() || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);

        } catch (InternalAuthenticationServiceException iasEx) {
            return ResponseEntity.noContent().build();
        }catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponse> refreshToken() {
        try {
            String token = jwtFilter.getToken();
            if (token == null || token.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            LoginResponse response = userService.refreshToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        try {
            if (request.getDni() == null || request.getPassword() == null || 
                request.getName() == null || request.getSecondName() == null){
                return ResponseEntity.badRequest().body(null);
            }
            LoginResponse response = userService.register(request);
            if (response == null) {
                return ResponseEntity.noContent().build();
            } else {
                messagingTemplate.convertAndSend("/topic" + BASE_URL, response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<LoginResponse> updateProfile(@RequestBody UpdateUserDto updatedUserDto) {
        
        try {
            UserEntity userEntity = userRepository.findById(updatedUserDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            UserEntity userFromToken = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken()))
                .orElseThrow(() -> new RuntimeException("Invalid token"));

            LoginResponse response = userService.update(updatedUserDto);
            messagingTemplate.convertAndSend("/topic" + BASE_URL, updatedUserDto.getId());
            messagingTemplate.convertAndSend("/topic" + BASE_URL + "/" + updatedUserDto.getId(), response != null);
            messagingTemplate.convertAndSend("/topic" + BASE_URL + "/update/" + updatedUserDto.getId() , response != null ? response : "updated");
            return ResponseEntity.ok(userEntity.getId() == userFromToken.getId() ? response : null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/all") 
    public ResponseEntity<List<UserSafeDto>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<UserStatsEntity> getUserStats(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(userService.getUserStats(userId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/detailed/{id}")
    public ResponseEntity<UserDetailedDto> getDetailedUserById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userService.getDetailedUserById(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @PostMapping("/toggleDisabled/{id}")
    public ResponseEntity<Void> toggleDisableUser(@PathVariable int id) {
        try {
            userService.toggleDisableUser(id);
            messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
            messagingTemplate.convertAndSend("/topic" + BASE_URL + "/" + id, id);
            messagingTemplate.convertAndSend("/topic" + BASE_URL + "/update/" + id , "deleted");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/availablePermissions")
    public ResponseEntity<AvailableUserPermissionsDto> getAvailableUserPermissions() {
        try {
            return ResponseEntity.ok(userService.getAvailableUserPermissions());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/saveGroup")
    public ResponseEntity<GroupEntity> saveGroup(@RequestBody GroupEntity groupEntity) {
        try {
            GroupEntity savedGroup = userService.save(groupEntity);
            if (savedGroup != null) {
                messagingTemplate.convertAndSend(GROUP_TOPIC, savedGroup);
            } else {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(savedGroup);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/deleteGroup/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable int id) {
        try {
            userService.deleteGroup(id);
            messagingTemplate.convertAndSend(GROUP_TOPIC, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
            messagingTemplate.convertAndSend("/topic" + BASE_URL + "/" + id, id);
            messagingTemplate.convertAndSend("/topic" + BASE_URL + "/update/" + id , "deleted");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
