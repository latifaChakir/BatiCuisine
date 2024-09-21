package exceptions;

public class ProjectValidationException extends RuntimeException {
    public ProjectValidationException(String message) {
      super(message);
    }
}
