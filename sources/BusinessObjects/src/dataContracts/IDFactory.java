package dataContracts;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class IDFactory implements IIDFactory
{
    private final static UUID EMPTY = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final static Charset UTF8 = Charset.forName("utf-8");
    private ThreadLocal<MessageDigest> md5Container = new ThreadLocal<MessageDigest>();

    @Override
    public UUID getDeterministicID(String input) {
        byte[] bytes = input == null ? new byte[0] : input.getBytes(UTF8);
        try {
            MessageDigest md5 = md5Container.get();
            if (md5 == null) {
                md5 = MessageDigest.getInstance("MD5");
                md5Container.set(md5);
            }
            byte[] hash = md5.digest(bytes);
            return UUID.nameUUIDFromBytes(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Create MD5 MessageDigest is unsuccess: ", e);
        }
    }

    @Override
    public UUID create() {
        return UUID.randomUUID();
    }

    @Override
    public UUID getEmpty() {
        return EMPTY;
    }
}
