package com.kafkab2c.KafkaB2C.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkab2c.KafkaB2C.Models.B2CRequest;
import com.kafkab2c.KafkaB2C.Models.B2CSynchronousResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * DarajaUtils class provides utility methods for interacting with Safaricom's Daraja API.
 */
public class DarajaUtils {

    /** API key for authentication with the Daraja API. */
    private static String apiKey = "u1WQxeMd9p1HCJNMN8Feq63OEhbGR6KiLg4qcpLiKq7OqGK4";

    /** API secret for authentication with the Daraja API. */
    private static String apiSecret = "aI0g7BQJ1SvwKAyZ6Be4GkmCpf5dD5CeA0MpGhf6AMkrGEr8JG2JkDmAk8ALYTGw";

    /** Base URL for the Daraja API. */
    private static String darajaApiUrl = "https://sandbox.safaricom.co.ke";

    /** Initiator name used for API requests. */
    public final static String initiatorName = "testapi";

    /** Public URL for the application. */
    public final static String baseURL = "https://d401-102-216-154-25.ngrok-free.app";

    /** URL for receiving the result of the API callback. */
    public final static String resultURL = baseURL+"/api/callback";

    /** URL for receiving the timeout response. */
    public final static String queueTimeOutURL = baseURL+"/api/timeout";

    /** Initiator password used for API requests. */
    private static String initiatorPassword = "Safaricom3021#";

    /** Short code used for API requests. */
    public final static String shortCode = "600000";

    /**
     * Generates security credentials by encrypting the initiator password.
     *
     * @return The encrypted password as a Base64 encoded string.
     */
    public static String getSecurityCredentials() {
        String encryptedPassword = "";

        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            byte[] input = initiatorPassword.getBytes();

            Resource resource = new ClassPathResource("SandboxCertificate.cer");
            InputStream inputStream = resource.getInputStream();
            FileInputStream fin = new FileInputStream(resource.getFile());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fin);
            PublicKey pk = certificate.getPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE, pk);

            byte[] cipherText = cipher.doFinal(input);
            encryptedPassword = Base64.getEncoder().encodeToString(cipherText).trim();
            return encryptedPassword;
        } catch (NoSuchAlgorithmException | CertificateException | InvalidKeyException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | NoSuchProviderException | FileNotFoundException e) {
            System.err.println("Error generating security credentials");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Retrieves an access token from the Daraja API.
     *
     * @return The access token.
     */
    public static String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = darajaApiUrl + "/oauth/v1/generate?grant_type=client_credentials";
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setBasicAuth(apiKey, apiSecret);
        tokenHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> tokenEntity = new HttpEntity<>(null, tokenHeaders);
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.GET, tokenEntity, Map.class);
        return (String) tokenResponse.getBody().get("access_token");
    }

    /**
     * Sends a B2C request to the Daraja API.
     *
     * @param accessToken The access token for authentication.
     * @param b2CRequest The B2C request to be sent.
     * @return The response from the B2C request.
     */
    public static B2CSynchronousResponse sendB2CRequest(String accessToken, B2CRequest b2CRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String securityCredential = getSecurityCredentials();

        Map<String, Object> requestPayload = b2CRequest.generateHashMap();
        requestPayload.put("SecurityCredential", securityCredential);

        String b2cUrl = darajaApiUrl + "/mpesa/b2c/v1/paymentrequest";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(b2cUrl, entity, String.class);
        return new B2CSynchronousResponse(response.getBody());
    }

    /**
     * Checks the status of a transaction using the conversation ID.
     *
     * @param conversationID The ID of the conversation to check.
     * @return True if the transaction status is successful, otherwise false.
     */
    public static B2CSynchronousResponse checkTransactionStatus(String conversationID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("Initiator", initiatorName);
        requestPayload.put("SecurityCredential", getSecurityCredentials());
        requestPayload.put("CommandID", "TransactionStatusQuery");
        requestPayload.put("TransactionID", conversationID);
        requestPayload.put("PartyA", shortCode);
        requestPayload.put("IdentifierType", "4");
        requestPayload.put("ResultURL", resultURL);
        requestPayload.put("QueueTimeOutURL", queueTimeOutURL);
        requestPayload.put("Remarks", "Checking transaction status");
        requestPayload.put("Occasion", "Checking Transaction Status");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(darajaApiUrl + "/mpesa/transactionstatus/v1/query", entity, String.class);

            return new B2CSynchronousResponse(response.getBody());
        } catch (Exception e) {
            System.err.println("Error checking transaction status: " + e.getMessage());
            return null;
        }
    }
}
