package client;

import utils.KeyGeneratorUtil;
import utils.SignatureUtil;

import java.security.*;

public class Client {

    private final String clientId; // Уникальный идентификатор клиента
    private final KeyPair clientKeyPair;
    private PublicKey serverPublicKey;

    public Client(String clientId) {
        this.clientId = clientId;
        this.clientKeyPair = KeyGeneratorUtil.generateRSAKeyPair();
    }

    public String getClientId() {
        return clientId;
    }

    public PublicKey getPublicKey() {
        return clientKeyPair.getPublic();
    }

    public void setServerPublicKey(PublicKey serverPublicKey) {
        this.serverPublicKey = serverPublicKey;
    }

    public String createSignedMessage(String message) throws Exception {
        PrivateKey privateKey = clientKeyPair.getPrivate();
        String signature = SignatureUtil.signMessage(message, privateKey);
        return message + "|" + signature;
    }

    public boolean verifyServerMessage(String message, String signatureBase64) throws Exception {
        return SignatureUtil.verifySignature(message, signatureBase64, serverPublicKey);
    }
}