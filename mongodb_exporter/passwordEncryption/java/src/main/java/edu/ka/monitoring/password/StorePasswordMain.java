package edu.ka.monitoring.password;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;

public class StorePasswordMain {
    private static void checkArgs(String[] args) {
        if(args.length != 5) {
            throw new IllegalArgumentException("Usage: StorePasswordMain <full path to keystore> <keystore password> <password password> <key alias> <password to store>");
        }
    }

    private static void writePasswordToKeyStore(String pathToKeyStore, String keyStorePassword, String passwordPassword, String alias, String password)
            throws Exception {

        KeyStore keyStore = null;

        if (Files.exists(Paths.get(pathToKeyStore))) {
            keyStore = KeyUtils.loadKeyStoreFromFile(pathToKeyStore, keyStorePassword.toCharArray());
        } else {
            keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(null, keyStorePassword.toCharArray());
        }

        KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(passwordPassword.toCharArray());

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
        SecretKey generatedSecret =
                factory.generateSecret(new PBEKeySpec(
                        password.toCharArray(),
                        "password salt".getBytes(),
                        13
                ));

        keyStore.setEntry(alias, new KeyStore.SecretKeyEntry(
                generatedSecret), keyStorePP);

        FileOutputStream outputStream = new FileOutputStream(new File(pathToKeyStore));
        keyStore.store(outputStream, keyStorePassword.toCharArray());
    }

    public static void main(String[] args) throws Exception{

        checkArgs(args);

        String pathToKeyStore = args[0];
        String keystorePassword = args[1];
        String passwordPassword = args[2];
        String passwordAlias = args[3];
        String passwordToStore = args[4];

        writePasswordToKeyStore(pathToKeyStore, keystorePassword, passwordPassword, passwordAlias, passwordToStore);
    }
}
