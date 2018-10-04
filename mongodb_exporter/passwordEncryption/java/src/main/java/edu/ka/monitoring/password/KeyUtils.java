package edu.ka.monitoring.password;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore;

public class KeyUtils {
    public static FileInputStream getFileInputStreamFromArg(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        return new FileInputStream(file);
    }

    public static FileOutputStream getFileOutputStreamFromArg(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        return new FileOutputStream(file);
    }

    public static KeyStore loadKeyStoreFromFile(String pathToFile, String keystorePassword)
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(getFileInputStreamFromArg(pathToFile), keystorePassword.toCharArray());
        return keyStore;
    }

    public static KeyStore createKeyStoreFile(String pathToFile, String keystorePassword)
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        FileOutputStream fileOutputStreamFromArg = getFileOutputStreamFromArg(pathToFile);
        keyStore.store(fileOutputStreamFromArg, keystorePassword.toCharArray());
        return keyStore;
    }
}