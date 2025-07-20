package ssu.cromi.teamit.exception;

public class InvalidEnumValueException extends RuntimeException {

    public InvalidEnumValueException(String message) {
        super(message);
    }

    public InvalidEnumValueException(String fieldName, String value) {
        super(String.format("Invalid value for '%s': '%s'", fieldName, value));
    }
}
