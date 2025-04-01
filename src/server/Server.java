package server;

import utils.KeyGeneratorUtil;
import utils.SignatureUtil;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final KeyPair serverKeyPair;
    private final Map<String, PublicKey> clientPublicKeyDatabase; // Fake БД для хранения публичных ключей клиентов

    public Server() {
        this.serverKeyPair = KeyGeneratorUtil.generateRSAKeyPair();
        this.clientPublicKeyDatabase = new HashMap<>();
    }

    public PublicKey getPublicKey() {
        return serverKeyPair.getPublic();
    }

    public boolean verifyClientMessage(String clientId, String message, String signatureBase64) throws Exception {
        PublicKey clientPublicKey = getClientPublicKeyFromDatabase(clientId);
        if (clientPublicKey == null) {
            System.out.println("Сервер: Клиент с ID " + clientId + " не найден в базе данных.");
            return false;
        }
        return SignatureUtil.verifySignature(message, signatureBase64, clientPublicKey);
    }

    public String createSignedMessage(String message) throws Exception {
        PrivateKey privateKey = serverKeyPair.getPrivate();
        return message + "|" + SignatureUtil.signMessage(message, privateKey);
    }

    public void registerClient(String clientId, PublicKey publicKey) {
        clientPublicKeyDatabase.put(clientId, publicKey);
        System.out.println("Сервер: Клиент с ID " + clientId + " успешно зарегистрирован.");
    }


    public PublicKey getClientPublicKeyFromDatabase(String clientId) {
        return clientPublicKeyDatabase.get(clientId);
    }
}
