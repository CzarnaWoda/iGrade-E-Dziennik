package me.igrade.user.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.igrade.security.jwt.TokenService;
import me.igrade.security.provider.AccountAuthenticationProvider;
import me.igrade.user.dto.UserDto;
import me.igrade.user.mapper.UserMapper;
import me.igrade.user.model.User;
import me.igrade.user.requests.ChangePasswordRequest;
import me.igrade.user.requests.StudentRegisterRequest;
import me.igrade.user.requests.UserLoginRequest;
import me.igrade.user.response.NotificationType;
import me.igrade.user.service.UserService;
import me.igrade.utils.DateUtil;
import me.igrade.utils.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final DateUtil dateUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountAuthenticationProvider authenticationProvider;
    private final TokenService tokenService;



    @GetMapping("/me")
    public ResponseEntity<HttpResponse> me(Authentication authentication){
        Optional<User> optionalUser = userService.getUserByEmail(authentication.getName());

        return optionalUser.<ResponseEntity<HttpResponse>>map(user -> ResponseEntity.status(OK).body(HttpResponse.builder()
                .statusCode(OK.value())
                .httpStatus(OK)
                .data(Map.of("user", userMapper.mapUserToUserDto(user))).build())).orElseGet(() -> ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .httpStatus(BAD_REQUEST)
                .build()));
    }
    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        try {
            final Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.email(), userLoginRequest.password())
            );

            final Optional<User> optionalUser = userService.getUserByEmail(userLoginRequest.email());

            if(optionalUser.isPresent()){
                final String token = tokenService.generateToken(authentication, userLoginRequest.remember());

                return ResponseEntity.status(OK).body(HttpResponse.builder()
                        .timeStamp(dateUtil.getNowDate())
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .message(token)
                        .data(Map.of("user",userMapper.mapUserToUserDto(optionalUser.get())))
                        .build());

            }else{
                return ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                        .timeStamp(dateUtil.getNowDate())
                        .httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .message("Wrong login or password")
                        .build());
            }
        }catch (AuthenticationException e){
            return ResponseEntity.status(UNAUTHORIZED).body(HttpResponse
                    .builder()
                    .message("Wrong login or password")
                    .timeStamp(dateUtil.getNowDate())
                    .statusCode(UNAUTHORIZED.value())
                    .httpStatus(UNAUTHORIZED)
                    .data(Map.of("AuthenticationException",e.getMessage()))
                    .developerMessage("TEST 1").build());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<HttpResponse> getUserByEmail(@Valid @PathVariable String email) {
        final Optional<User> optionalUser = userService.getUserByEmail(email);

        if(optionalUser.isPresent()){
            final UserDto userDto = userMapper.mapUserToUserDto(optionalUser.get());


            return ResponseEntity.status(HttpStatus.OK).body(HttpResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .message("User has been found")
                    .data(Map.of("user", userDto))
                    .build());
        }else{
            return ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                    .httpStatus(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .message("User has not been found")
                    .timeStamp(dateUtil.getNowDate()).build());
        }

    }

    /**
     *
     * @param classId - class assigned to student (we can be sure that only students have assigned classId)
     * @return only students selected by classId! Teacher doesn't have set classId !!
     */
    @GetMapping("/student/class/{classId}")
    public ResponseEntity<HttpResponse> getStudentsByClassId(@PathVariable long classId){
        return ResponseEntity.status(OK).body(HttpResponse.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .data(Map.of("users", userService.getStudentByClassId(classId).stream().map(userMapper::mapUserToUserDto).toList())).build());
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserDto> getUserById(@Valid @PathVariable long userId){
        final Optional<User> user = userService.getUserById(userId);

        return user.map(value -> ResponseEntity.status(OK).body(userMapper.mapUserToUserDto(value))).orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(null));
    }
    @PostMapping("/student/register")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "registerFallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> registerStudent(@RequestBody @Valid StudentRegisterRequest userRegisterRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return CompletableFuture.supplyAsync(() ->ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                    .httpStatus(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .message(bindingResult.getAllErrors().get(0).getDefaultMessage()).build()));
        }
        if(userService.existUserByEmail(userRegisterRequest.email())){
            return CompletableFuture.supplyAsync(() ->ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                    .httpStatus(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .message("User already exist!")
                    .developerMessage("USer with this email exist in repository")
                    .timeStamp(dateUtil.getNowDate()).build()));
        }else{
            final User user = userService.createStudent(userRegisterRequest);

            if(user == null){
                return CompletableFuture.supplyAsync(() ->ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                        .httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .message("Something went wrong!")
                        .developerMessage("User were not created")
                        .timeStamp(dateUtil.getNowDate()).build()));
            }

            return CompletableFuture.supplyAsync(() -> ResponseEntity.status(OK).body(HttpResponse.builder()
                    .httpStatus(OK)
                    .statusCode(OK.value())
                    .message("Student has been created")
                    .data(Map.of("user", userMapper.mapUserToUserDto(user))).build()));
        }

    }

    @PostMapping("/changePassword")
    public ResponseEntity<HttpResponse> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, BindingResult bindingResult, Authentication authentication){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                    .httpStatus(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .message(bindingResult.getAllErrors().get(0).getDefaultMessage()).build());
        }

        final User user = userService.getUserByEmail(authentication.getName()).get();
        if(!passwordEncoder.encode(changePasswordRequest.accountPassword()).equals(user.getPassword())){
            return ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                    .httpStatus(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .message("Wrong password!")
                    .build());
        }

        if(changePasswordRequest.password().equals(changePasswordRequest.repeatPassword())){
            user.setPassword(passwordEncoder.encode(changePasswordRequest.password()));

            userService.updateUser(user);

            return ResponseEntity.status(OK).body(HttpResponse.builder()
                    .message("Password has been changed")
                    .timeStamp(dateUtil.getNowDate())
                    .httpStatus(OK)
                    .statusCode(OK.value())
                    .developerMessage("Password update for user").build());
        }else{
            return ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                    .httpStatus(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .message("Data is not correct")
                    .developerMessage("Passwords don't match!")
                    .timeStamp(dateUtil.getNowDate()).build());
        }
    }

    @GetMapping("/student/grades")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> getStudentGrades(Authentication authentication){

        final Optional<User> user = userService.getUserByEmail(authentication.getName());


        return CompletableFuture.supplyAsync(() -> user.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .message("User grades")
                .data(Map.of("grades", value.isStudent() ? userService.getStudentGrades(value.getId()) : userService.getTeacherGrades(value.getId())))
                .build())).orElseGet(() -> ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder().httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .message("Student doesn't have any grades").build())));
    }

    @GetMapping("/student/notes-stat")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> getStudentNotesStat(Authentication authentication){
        final Optional<User> student = userService.getUserByEmail(authentication.getName());
        return CompletableFuture.supplyAsync(() -> student.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .message("User notes stat")
                .data(Map.of("stats", userService.getStudentNoteStat(value.getId())))
                .build())).orElseGet(() -> ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder().httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .message("USer doesn't have any notes").build())));
    }
    @GetMapping("/student/notes")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> getStudentNotes(Authentication authentication){
        final Optional<User> student = userService.getUserByEmail(authentication.getName());

        return CompletableFuture.supplyAsync(() -> student.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .message("USer notes")
                .data(Map.of("notes", value.isStudent() ? userService.getStudentNotes(value.getId()) : userService.getTeacherNotes(value.getId())))
                .build())).orElseGet(() -> ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder().httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .message("User doesn't have any notes").build())));

    }
    @GetMapping("/student/class")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> getStudentClass(Authentication authentication){
        final Optional<User> student = userService.getUserByEmail(authentication.getName());
        return CompletableFuture.supplyAsync(() -> student.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                        .message("User class")
                        .statusCode(OK.value())
                        .httpStatus(OK)
                        .data(Map.of("class", userService.getStudentClass(value.getClassId()))).build()))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(HttpResponse.builder()
                        .message("User class has not be found")
                        .httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value()).build())));
    }

    @GetMapping("/notifications")
    @CircuitBreaker(name = "notifications", fallbackMethod = "notificationFallBackMethod")
    @TimeLimiter(name = "notifications")
    @Retry(name = "notifications")
    public CompletableFuture<ResponseEntity<HttpResponse>> getUserNotifications(Authentication authentication){
        final Optional<User> user = userService.getUserByEmail(authentication.getName());

        return CompletableFuture.supplyAsync(() -> user.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                        .message("User notifications")
                        .statusCode(OK.value())
                        .httpStatus(OK)
                        .data(Map.of("notifications", userService.getUserNotifications(value.getId()))).build()))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(HttpResponse.builder()
                        .message("User notifications has not be found")
                        .httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value()).build())));
    }
    @PostMapping("/notifications/checked/{notificationType}")
    public ResponseEntity<HttpResponse> markAsCheckedUserNotifications(Authentication authentication, @PathVariable String notificationType){
        if(NotificationType.isNotValidNotificationType(notificationType)){
            return ResponseEntity.status(NO_CONTENT).body(HttpResponse.builder()
                    .message("This type of notifications doesn't exist!")
                    .statusCode(NO_CONTENT.value())
                    .httpStatus(NO_CONTENT)
                    .build());
        }
        final Optional<User> user = userService.getUserByEmail(authentication.getName());

        user.ifPresent(s -> userService.markAsCheckedNotificationsByNotificationType(NotificationType.valueOf(notificationType), s.getId()));

        return ResponseEntity.status(OK).body(HttpResponse.builder()
                .message("Notifications of type " + notificationType + " has been marked as checked")
                .statusCode(OK.value())
                .httpStatus(OK)
                .build());
    }

    @GetMapping("/teacher/grades")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> getTeacherGrades(Authentication authentication){
        final Optional<User> optionalUser = userService.getUserByEmail(authentication.getName());

        return CompletableFuture.supplyAsync(() -> optionalUser.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .message("Teacher grades")
                .data(Map.of("grades", userService.getTeacherGrades(value.getId()))).build())).orElseGet(() -> ResponseEntity.status(BAD_REQUEST)
                .body(HttpResponse.builder().httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .message("Teacher doesn't have any grades").build())));
    }
    @GetMapping("/teacher/notes")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> getTeacherNotes(Authentication authentication){
        final Optional<User> optionalUser = userService.getUserByEmail(authentication.getName());

        return CompletableFuture.supplyAsync(() -> optionalUser.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .message("Teacher notes")
                .data(Map.of("notes", userService.getTeacherNotes(value.getId()))).build())).orElseGet(() -> ResponseEntity.status(BAD_REQUEST)
                .body(HttpResponse.builder().httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .message("Teacher doesn't have any notes").build())));
    }
    @GetMapping("/teacher/subjects")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> getSubjects(Authentication authentication){
        final Optional<User> optionalUser = userService.getUserByEmail(authentication.getName());

        return CompletableFuture.supplyAsync(() ->optionalUser.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .message("Teacher subjects")
                .data(Map.of("subjects", userService.getTeacherSubjects(value.getId()))).build())).orElseGet(() -> ResponseEntity.status(BAD_REQUEST)
                .body(HttpResponse.builder().httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .message("Teacher doesn't have any subjects").build())));
    }

    @GetMapping("/teacher/classes")
    @CircuitBreaker(name = "teacherStudentData", fallbackMethod = "subjectFallBackMethod")
    @TimeLimiter(name = "teacherStudentData")
    @Retry(name = "teacherStudentData")
    public CompletableFuture<ResponseEntity<HttpResponse>> getClasses(Authentication authentication){
        final Optional<User> optionalUser = userService.getUserByEmail(authentication.getName());

        final boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase("ADMIN_READ"));
        return CompletableFuture.supplyAsync(() -> optionalUser.<ResponseEntity<HttpResponse>>map(value -> ResponseEntity.status(OK).body(HttpResponse.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .message("Teacher classes")
                .data(Map.of("classes", isAdmin ? userService.getAllClasses() : userService.getTeacherClasses(value.getId()))).build())).orElseGet(() -> ResponseEntity.status(BAD_REQUEST)
                .body(HttpResponse.builder().httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .message("Teacher doesn't have any classes").build())));
    }
    public CompletableFuture<ResponseEntity<HttpResponse>> fallBackMethod(Authentication authentication, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                .httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .message("Oops! Aktualnie nie możemy pobrać informacji na temat użytkowników!")
                .build()));
    }

    //fallback method using for handle while student can't register because there is no data access from TeacherStudentData-Service
    public CompletableFuture<ResponseEntity<HttpResponse>> registerFallBackMethod(StudentRegisterRequest registerRequest, BindingResult bindingResult, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(NOT_FOUND).body(HttpResponse.builder().httpStatus(BAD_REQUEST)
                .statusCode(NOT_FOUND.value())
                .message("Aktualnie nie możemy sprawdzić informacji na temat klas!")
                .build()));
    }
    //fallback method using for handle while Notification-service is not available
    public CompletableFuture<ResponseEntity<HttpResponse>> notificationFallBackMethod(Authentication authentication, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                .httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .message("Oops! Aktualnie nie możemy pobrać informacji na temat powiadomień!")
                .build()));
    }


}
