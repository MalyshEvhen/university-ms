package ua.foxstudent.service.exceptions;

public class CourseAlreadyExistsException extends RuntimeException {

    public CourseAlreadyExistsException(String message) {
        super(message);
    }

    public CourseAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourseAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
