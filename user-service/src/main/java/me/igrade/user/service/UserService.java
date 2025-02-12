package me.igrade.user.service;

import me.igrade.user.model.User;
import me.igrade.user.requests.StudentRegisterRequest;
import me.igrade.user.requests.TeacherRegisterRequest;
import me.igrade.user.response.*;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserByEmail(String email);

    ClassDto getStudentClass(long classId);

    Optional<User> getUserById(long userId);

    List<User> getUsersByFirstName(String firstName);

    List<User> getUsersByLastName(String lastName);

    User createStudent(StudentRegisterRequest request);

    GradeDto[] getStudentGrades(long id);

    StatDto getStudentNoteStat(long id);

    NoteDto[] getStudentNotes(long id);

    NotificationDto[] getUserNotifications(long userId);

    void markAsCheckedNotificationsByNotificationType(NotificationType notificationType, long notificationReceiverId);

    List<User> getStudentByClassId(long classId);

    boolean existUserByEmail(String email);

    boolean createTeacher(TeacherRegisterRequest teacherRegisterRequest);

    GradeTeacherDto[] getTeacherGrades(long id);

    NoteTeacherDto[] getTeacherNotes(long id);

    SubjectDTO[] getTeacherSubjects(long id);

    ClassTeacherDto[] getTeacherClasses(long id);

    ClassTeacherDto[] getAllClasses();

    void updateUser(User user);
}
