package ssu.cromi.teamit.util;

import ssu.cromi.teamit.exception.InvalidEnumValueException;

public class EnumValidator {

    /** 문자열 → Enum 변환, 실패 시 InvalidEnumValueException 발생 */
    public static <T extends Enum<T>> T parseEnum(Class<T> enumClass, String value, String fieldName) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (Exception ex) {
            throw new InvalidEnumValueException(fieldName, value);
        }
    }
}
