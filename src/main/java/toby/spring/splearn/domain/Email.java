package toby.spring.splearn.domain;

import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public record Email(String address) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$\"");

    public Email {
        requireNonNull(address);

        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("Invalid email address: " + address);
        }
    }
}
