package me.igrade.user.service.impl;


import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import me.igrade.events.NotificationMarkEvent;
import me.igrade.user.model.User;
import me.igrade.user.model.UserRole;
import me.igrade.user.repository.UserRepository;
import me.igrade.user.requests.TeacherRegisterRequest;
import me.igrade.user.response.*;
import me.igrade.user.requests.StudentRegisterRequest;
import me.igrade.user.service.UserService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserRoleServiceImpl userRoleService;
    private final WebClient.Builder webClientBuilder;

    private final PasswordEncoder encoder;

    private final KafkaTemplate<String, NotificationMarkEvent> kafkaTemplate;


    @Override
    public Optional<User> getUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
    @Override
    public ClassDto getStudentClass(long classId){
        return webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/class/id/" + classId)
                .retrieve()
                .bodyToMono(ClassDto.class)
                .block();
    }

    @Override
    public Optional<User> getUserById(long userId){
        return userRepository.findUserById(userId);
    }

    @Override
    public List<User> getUsersByFirstName(String firstName){
        return userRepository.findUsersByFirstName(firstName);
    }
    @Override
    public List<User> getUsersByLastName(String lastName){
        return userRepository.findUsersByLastName(lastName);
    }
    @Override
    public User createStudent(StudentRegisterRequest request){
        final ClassDto classDto = webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/class/code/" + request.classCode())
                .retrieve()
                .bodyToMono(ClassDto.class)
                .block();
        if(classDto == null){
            return null;
        }
        final UserRole studentRole = userRoleService.getUserRoleByName("STUDENT");
        if(studentRole == null){
            return null;
        }

        final User user = new User(request.firstName(),request.lastName(),true,encoder.encode(request.password()),request.email(),classDto.getId(),studentRole);

        userRepository.save(user);
        return user;
    }
    @Override
    public GradeDto[] getStudentGrades(long id) {
        return webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/grade/student/" + id)
                .retrieve()
                .bodyToMono(GradeDto[].class)
                .block();
    }
    @Override
    public StatDto getStudentNoteStat(long id){
        return webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/note/stats/" + id)
                .retrieve()
                .bodyToMono(StatDto.class)
                .block();
    }
    @Override
    public NoteDto[] getStudentNotes(long id) {
        return webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/note/student/" + id)
                .retrieve()
                .bodyToMono(NoteDto[].class)
                .block();
    }

    //TODO change notification system
    @Override
    public NotificationDto[] getUserNotifications(long userId){
        return webClientBuilder.build().get()
                .uri("http://notification-service/api/v1/notification/all/" + userId)
                .retrieve()
                .bodyToMono(NotificationDto[].class)
                .block();
    }

    @Override
    public void markAsCheckedNotificationsByNotificationType(NotificationType notificationType, long notificationReceiverId){
        kafkaTemplate.send("notificationMarkTopic", new NotificationMarkEvent(notificationType, notificationReceiverId));
    }

    @Override
    public List<User> getStudentByClassId(long classId) {
        return userRepository.findUsersByClassId(classId);
    }

    @Override
    public boolean existUserByEmail(@Email String email){
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean createTeacher(TeacherRegisterRequest teacherRegisterRequest){
        final UserRole teacherRole = userRoleService.getUserRoleByName("TEACHER");
        if(teacherRole == null){
            return false;
        }
        final User user = new User(teacherRegisterRequest.firstName(), teacherRegisterRequest.lastName(), true, encoder.encode(teacherRegisterRequest.password()), teacherRegisterRequest.email(), null, teacherRole);

        userRepository.save(user);

        return true;
    }

    @Override
    public GradeTeacherDto[] getTeacherGrades(long id){
        return webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/grade/teacher/" + id)
                .retrieve()
                .bodyToMono(GradeTeacherDto[].class)
                .block();
    }
    @Override
    public NoteTeacherDto[] getTeacherNotes(long id){
        return webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/note/teacher/" + id)
                .retrieve()
                .bodyToMono(NoteTeacherDto[].class)
                .block();
    }
    @Override
    public SubjectDTO[] getTeacherSubjects(long id){
        return webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/subject/teacher/" + id)
                .retrieve()
                .bodyToMono(SubjectDTO[].class)
                .block();
    }

    @Override
    public ClassTeacherDto[] getTeacherClasses(long id) {
        return webClientBuilder.build().get()
                .uri("http://teacherStudentData-service/api/v1/class/teacher/" + id)
                .retrieve()
                .bodyToMono(ClassTeacherDto[].class)
                .block();
    }

    @Override
    public ClassTeacherDto[] getAllClasses() {
        return webClientBuilder.build().get().uri("http://teacherStudentData-service/api/v1/class/all")
                .retrieve()
                .bodyToMono(ClassTeacherDto[].class)
                .block();
    }
    @Override
    public void updateUser(User user){
        userRepository.save(user);
    }
}
