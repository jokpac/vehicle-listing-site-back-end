package lt.ca.javau11.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lt.ca.javau11.models.UserDTO;
import lt.ca.javau11.services.UserService;

import java.security.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {
	
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
  
  @Autowired
  UserService userService;

  @Value("${jokpac.app.jwtSecret}")
  private String jwtSecret;

  @Value("${jokpac.app.jwtExpirationMs}")
  private int jwtExpirationMs;
  
  
  public void setSecretKey(String key) {
	  jwtSecret = key;
  }
  
  public void setExpirationMs(int ms) {
	  jwtExpirationMs = ms;
  }
  

  public String generateJwtToken(Authentication authentication) {

    UserDTO userPrincipal = (UserDTO) authentication.getPrincipal();

    return Jwts.builder()
        .subject((userPrincipal.getUsername()))
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key())
        .compact();
  }
  
  public Key toKey(String secret) {
	  return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }
  
  private Key key() {
    return toKey(jwtSecret);
  }

  public String getUserNameFromJwtToken(String token) {
	  
	  return Jwts
			  .parser()
			  .verifyWith((SecretKey) key())
			  .build()
              .parseSignedClaims(token)
              .getPayload()
              .getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
    	logger.debug("Trying to validate jwt token");
      Jwts.parser().verifyWith((SecretKey)key()).build().parse(authToken);
        logger.debug("Jwt token is valid \n" + authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
  
  public Authentication getAuthentication(String token) {
	  logger.trace("getAuthentication method is working");
	  
	    String username = getUserNameFromJwtToken(token);
	    UserDetails userDetails = userService.loadUserByUsername(username);
	    logger.debug("UserDetails loaded: " + userDetails.getUsername() + ", Roles: " + userDetails.getAuthorities());

	    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
  
}