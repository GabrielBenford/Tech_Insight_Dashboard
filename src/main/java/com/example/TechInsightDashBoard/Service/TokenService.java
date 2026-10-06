package com.example.TechInsightDashBoard.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class TokenService {


    @Value("${security.jwt.secret}")
    private String securityKey;


    public String creatingToken(UserEntity userEntity){
      try{
        Algorithm algorithm = Algorithm.HMAC256(securityKey);
        return JWT.create()
                .withSubject(userEntity.getEmail())
                .withExpiresAt(generateExpirationDate())
                .withClaim("id", userEntity.getId())
                .withClaim("email", userEntity.getEmail())
                .sign(algorithm);
    }
    catch (Exception e){
        throw new RuntimeException("Error creating token", e);
    }
    }
    public String validateToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(securityKey);
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
        return decodedJWT.getSubject();
    }



    private Instant generateExpirationDate() {
        return Instant.now().plusSeconds(3600);
    }
}
