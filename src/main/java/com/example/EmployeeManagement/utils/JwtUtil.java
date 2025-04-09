package com.example.EmployeeManagement.utils;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.security.key}")
    private String SECRET_KEY;

    public String generateToken(String username){
        return generateToken(username ,"Trainee");
    }

    public String generateToken(String username, String role){

        long oneDayMillis = 24 * 60 * 60 * 1000;
        Date issueDate = new Date(System.currentTimeMillis());
        Date tokenExpiryDate = new Date(issueDate.toInstant().toEpochMilli() + oneDayMillis);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                                .subject(username)
                                .claim("role", role)
                                .issueTime(issueDate)
                                .expirationTime(tokenExpiryDate)
                                .build();
        try{
          JWSSigner signer = new MACSigner(SECRET_KEY.getBytes());
          JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
          
          SignedJWT signedJWT = new SignedJWT(header, claimsSet);
          signedJWT.sign(signer);

          return signedJWT.serialize();

        }
        catch(JOSEException ex){
            log.error("Error in token generation", ex);
        }
        return null;                        
    }

    public boolean verifyToken(String token){
        try{
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            if(!signedJWT.verify(verifier)){
                return false;
            }

            Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expTime != null && expTime.after(new Date());
        }
        catch(JOSEException | ParseException ex)
        {
            log.error("Error occured while verifing the token", ex);
            return false;
        }
    }
 
    public String extractUsername(String token){
        try{
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
        }
        catch(ParseException ex){
            log.error("Error while extracting username", ex);
            throw new RuntimeException("Error while parsing the JWT token" , ex);
        }
    }
  
    public Date extractExpiration(String token){
        try{
            return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
        }
        catch(ParseException ex){
            log.error("Error while extracting expiration", ex);
            throw new RuntimeException("Error while parsing the JWT token" , ex);
        }
    }

    public String extractRole(String token){
        try{
            return (String) SignedJWT.parse(token).getJWTClaimsSet().getClaim("role");
        }
        catch(ParseException ex){
            log.error("Error while parsing token for extracting role", ex);
            return null;
        }
    }
}
