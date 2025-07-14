package ssu.cromi.teamit.exception;

public class InvalidEnumValueException extends RuntimeException {
    public InvalidEnumValueException(String field, String value) {
        super(String.format("Invalid %s value: %s", field, value));
    }
}
