package com.yoanpetrov.studentmanagementsystem.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT service. Used to extract different claims from a JWT token or validate it.
 */
@Service
public class JwtService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * Validates whether a JWT token represents the correct user.
     *
     * @param token       the token.
     * @param userDetails the user to validate the token against.
     * @return true if the token is not expired and represents the same user, false otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        LOG.debug("Validating token {} against user {}", token, userDetails.getUsername());
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
            && !isTokenExpired(token);
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token the token.
     * @return the extracted username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract a claim from the JWT token, using the given claim resolver.
     *
     * @param token          the token.
     * @param claimsResolver the resolver for the needed claim.
     * @param <T>            the type of the claim, part of the resolver.
     * @return the extracted claim.
     */
    public <T> T extractClaim(
        String token,
        Function<Claims, T> claimsResolver
    ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param userDetails the user to generate a JWT token for.
     * @return the generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for the given user, with the given extra claims to be included in it.
     *
     * @param extraClaims the extra claims to include in the token.
     * @param userDetails the user to generate a JWT token for.
     * @return the generated JWT token.
     */
    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
        LOG.debug("Generating JWT token");
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Generates a refresh token for the given user.
     *
     * @param userDetails the user to generate a refresh token for.
     * @return the generated refresh token.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        LOG.debug("Generating JWT refresh token");
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * Checks whether a JWT token has expired.
     *
     * @param token the token.
     * @return true if the token's expiration time was before the current time, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration time of a JWT token.
     *
     * @param token the token.
     * @return the {@code Date} object, representing the exact time of expiration.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all the claims from a JWT token.
     *
     * @param token the token.
     * @return the extracted {@code Claims}.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * Builds a JWT token.
     *
     * @param extraClaims the extra claims to include in the token.
     * @param userDetails the user to build the token for.
     * @param expiration  the expiration time of the token.
     * @return the newly built JWT token.
     */
    private String buildToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails,
        long expiration
    ) {
        LOG.debug("Building JWT token for {} with expiration time of {} ms", userDetails.getUsername(), expiration);
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();

    }

    /**
     * Gets the signing key for the JWT tokens.
     *
     * @return the newly created {@code Key}.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
