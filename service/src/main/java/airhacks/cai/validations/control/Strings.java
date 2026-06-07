package airhacks.cai.validations.control;

import jakarta.ws.rs.BadRequestException;

public interface Strings {

    static void requireNonBlank(String subject, String value) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(subject + " must not be blank");
        }
    }
}
