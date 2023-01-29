package com.snippet.spring.jwt;

import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt test
 *
 * @author w00623538
 * @since 2023-01-29
 */
public class HutoolJWTTest {

    String privateKey =
        "-----BEGIN PRIVATE KEY-----\\n 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\\n yrcAwJN7nuvu3VZVzecH2CE=\\n -----END PRIVATE KEY-----\n";

    String publicKey =
        "-----BEGIN PUBLIC KEY-----\\n MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAwbmexCqELYH1MRJEEjJ1\\n TjWF64pMXYlbonqVik5CYC4n8YN76y0qK7xZWKm/Za/Q8ivZcKgLPdImElj8dyiJ\\n rOjgUoOpmwmH+KSk4+TIbfKPx3M39SuZje6d6qFx/0bI1dsl9sjo4CZ36TnBYn3w\\n YUkS4X8vMplR5W9T1c4IBkVu5bbga+O2c5fQzrXZGh7476yh3tIAsMLHHtVVC+f8\\n nFURpPaerbFRaVq0t8x8MOa4jevSqpCxQTf3fKhMYd9cw8by+QbunBn4qCT2e927\\n 9d/E98nfDNERu2BrFQcZPipoYEt9bQuSJ14qVXSwF33t+92EwVTcuNqdspX0MtVq\\n DJKP7X+CTgtp2P58VJuQM08HzWtQMTGDTfgxiLyoW0VQwRE3DTHQoZjf0/uuY+V/\\n KDqhz++QU7jRucvf578JopMSACSxfl1G1L3Ew/vcajxyrFC2NlLUbNK6Tz6pJo5x\\n eidBglWaIiLdwnu0FpIGI7H02oxJoCOQyRhAgLAjIiiJAgMBAAE=\\n -----END PUBLIC KEY-----";

    @Test
    public void jwt_operation_test() {
        // header
        Map<String, Object> header = new HashMap<>();
        header.put(JWTHeader.ALGORITHM, HmacAlgorithm.HmacSHA256.getValue());
        header.put(JWTHeader.TYPE, "JWT");
        // payload
        Map<String, Object> payload = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, now.plusHours(24));
        payload.put(JWTPayload.NOT_BEFORE, now);
        payload.put("userId", "1");
        // signer
        JWTSigner signer = JWTSignerUtil.createSigner(HmacAlgorithm.HmacSHA256.getValue(), privateKey.getBytes());

        // create jwt
        String token = JWTUtil.createToken(header, payload, signer);

        // parse jwt
        JWT jwt = JWTUtil.parseToken(token);
        JWTPayload parserPayload = jwt.getPayload();
        Long userId = Long.parseLong(parserPayload.getClaim("userId").toString());
        Assertions.assertEquals(userId, 1L);

        // verify jwt
        boolean verify = JWTUtil.verify(token, publicKey.getBytes());
        Assertions.assertTrue(verify);
    }
}
