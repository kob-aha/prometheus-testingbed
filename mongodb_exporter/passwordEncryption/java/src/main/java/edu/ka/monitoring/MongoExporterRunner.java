package edu.ka.monitoring;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static edu.ka.monitoring.password.KeyUtils.loadKeyStoreFromFile;

public class MongoExporterRunner {

//    public static final String KEYSTORE_PATH = "/Users/koby/Incubator/prometheus/mongodb_exporter/passwordEncryption/passwords";
    public static final String KEYSTORE_PATH = "/vagrant/mongodb_exporter/passwordEncryption/passwords";

    private String keystorePath;
    private char[] storePass;
    private char[] passwordPassword;
    private Process process;

    public MongoExporterRunner(String keystorePath, String storePass, String passwordPassword) {
        this.keystorePath = keystorePath;
        this.storePass = storePass.toCharArray();
        this.passwordPassword = passwordPassword.toCharArray();
    }

    public void runExporter() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("/vagrant/mongodb_exporter/run-exporter.sh");
        processBuilder.redirectOutput(new File("/tmp/mongo_exporter.log"));

        process = processBuilder.start();
        char[] userPass = getDBUserPassword();

        writeInputToProcess(userPass);

        process.waitFor();

    }

    private void writeInputToProcess(char[] userPass) {
        try {
            if (process != null) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                String urlStart = "mongodb://root:";
                String urlEnd = "@192.168.42.1:27017";
                bw.write(urlStart, 0, urlStart.length());
                bw.write(userPass, 0, userPass.length);
                bw.write(urlEnd, 0, urlEnd.length());
                bw.newLine();
                bw.close();
            }
        } catch (IOException e) {
            System.out.println("Either couldn't read from the template file or couldn't write to the OutputStream.");
            e.printStackTrace();
        }
    }

    private void shutdown() {
        process.destroy();
    }

    private char[] getDBUserPassword() throws Exception {
        KeyStore keyStore = loadKeyStoreFromFile(this.keystorePath, this.storePass);

        KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(this.passwordPassword);

        KeyStore.SecretKeyEntry ske =
                (KeyStore.SecretKeyEntry)keyStore.getEntry("mongo_root", keyStorePP);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
        PBEKeySpec keySpec = (PBEKeySpec)factory.getKeySpec(
                ske.getSecretKey(),
                PBEKeySpec.class);

        return keySpec.getPassword();
    }

    private static class MongoExporterTerminator extends Thread {

        private MongoExporterRunner runner;

        public MongoExporterTerminator(MongoExporterRunner runner) {
            this.runner = runner;
        }

        @Override
        public void run() {
            runner.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {

        String storePass = System.getenv("STORE_PASS");

        MongoExporterRunner exporter = new MongoExporterRunner(KEYSTORE_PATH, storePass, storePass);

        MongoExporterTerminator mongoExporterTerminator = new MongoExporterTerminator(exporter);
        Runtime.getRuntime().addShutdownHook(mongoExporterTerminator);

        exporter.runExporter();
    }
}
