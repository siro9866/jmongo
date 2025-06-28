package com.sil.jmongo.global.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JasyptConfigTest {
    @Value("${custom.jasypt.encryptor.key}") String key;

    @Test
    @DisplayName("비밀번호 암호화")
    public void passwordEncode(){

        String dbinfo = "mongodb://dbuser:dbpassword@localhost:27017/jmongo?authSource=admin";
        String secret = "ThisIsSilFrameworkAndThisFrameworkIsBorn2025Bykw";

        System.out.println("key = " + key);
        System.out.println("dbinfo:" + jasyptEncoding(dbinfo));
        System.out.println("secret:" + jasyptEncoding(secret));
    }

    public String jasyptEncoding(String value) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }
}