package vn.iotstar.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            return null;
        }
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            JWSSigner signer = new MACSigner(secretKey.getBytes());
            
            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + jwtExpiration));
                    
            for (Map.Entry<String, Object> entry : extraClaims.entrySet()) {
                claimsBuilder.claim(entry.getKey(), entry.getValue());
            }
            
            JWTClaimsSet claimsSet = claimsBuilder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);
            
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error generating token using Nimbus", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            
            // Verify chữ ký
            if (!signedJWT.verify(verifier)) {
                return false;
            }
            
            final String username = signedJWT.getJWTClaimsSet().getSubject();
            final Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            
            return (username != null && username.equals(userDetails.getUsername())) 
                    && !expiration.before(new Date());
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }
}
