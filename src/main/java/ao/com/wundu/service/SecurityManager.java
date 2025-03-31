package ao.com.wundu.service;

public interface SecurityManager {

    String encrypt(String data);
    String decrypt(String encryptedData);
}
