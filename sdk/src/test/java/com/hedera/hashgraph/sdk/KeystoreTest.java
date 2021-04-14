package com.hedera.hashgraph.sdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class KeystoreTest {
    private static final String TEST_KEY_STR = "302e020100300506032b657004220420db484b828e64b2d8f12ce3c0a0e93a0b8cce7af1bb8f39c97732394482538e10";
    private static final String PASSPHRASE = "asdf1234";

    @Test
    @DisplayName("Keystore.fromStream returns correct key")
    void keystoreFromStream() throws IOException {
        // keystore file generated by hedera-sdk-js from `testKeyStr` and `passphrase`
        // NOT USED ANYWHERE
        InputStream inputStream = KeystoreTest.class.getResourceAsStream("/test-keystore.bin");
        Keystore keystore = Keystore.fromStream(inputStream, PASSPHRASE);

        PrivateKey privateKey = keystore.getEd25519();
        Assertions.assertEquals(privateKey.toString(), TEST_KEY_STR);
    }

    @Test
    @DisplayName("Keystore.toStream produces decodable value")
    void keystoreToStream() throws IOException {
        PrivateKey privateKey = PrivateKey.fromString(TEST_KEY_STR);
        Keystore keystore = new Keystore(privateKey);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        keystore.export(outputStream, PASSPHRASE);

        Keystore keystore2 = Keystore.fromStream(new ByteArrayInputStream(outputStream.toByteArray()), PASSPHRASE);
        PrivateKey privateKey2 = keystore2.getEd25519();

        Assertions.assertEquals(privateKey2.toString(), TEST_KEY_STR);
    }
}
