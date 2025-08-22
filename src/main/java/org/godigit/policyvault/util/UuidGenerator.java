package org.godigit.policyvault.util;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.regex.Pattern;

public final class UuidGenerator {

    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
    );

    private UuidGenerator() {}

    public static UUID random() {
        return UUID.randomUUID();
    }

    public static boolean isValid(String value) {
        return value != null && UUID_PATTERN.matcher(value).matches();
    }

    public static UUID fromString(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid UUID: " + value);
        }
        return UUID.fromString(value);
    }

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID fromBytes(byte[] bytes) {
        if (bytes == null || bytes.length != 16) {
            throw new IllegalArgumentException("UUID byte array must be 16 bytes");
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long most = bb.getLong();
        long least = bb.getLong();
        return new UUID(most, least);
    }
}