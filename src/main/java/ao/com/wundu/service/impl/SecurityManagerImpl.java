package ao.com.wundu.service.impl;

import ao.com.wundu.exception.DecryptionException;
import ao.com.wundu.exception.EncryptionException;
import ao.com.wundu.service.SecurityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class SecurityManagerImpl implements SecurityManager {

    @Value("${encryption.key}")
    private String encryptionKey;

    private static final String ALGORITHM = "AES";

    @Override
    public String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new EncryptionException("Algoritmo de criptografia não disponível", e);
        } catch (InvalidKeyException e) {
            throw new EncryptionException("Chave de criptografia inválida", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new EncryptionException("Erro durante o processo de criptografia", e);
        } catch (Exception e) {
            throw new EncryptionException("Erro inesperado ao criptografar dados", e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new DecryptionException("Algoritmo de descriptografia não disponível", e);
        } catch (InvalidKeyException e) {
            throw new DecryptionException("Chave de descriptografia inválida", e);
        } catch (IllegalArgumentException e) {
            throw new DecryptionException("Dados criptografados inválidos", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new DecryptionException("Erro durante o processo de descriptografia", e);
        } catch (Exception e) {
            throw new DecryptionException("Erro inesperado ao descriptografar dados", e);
        }
    }
}
