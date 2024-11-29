package org.backend;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final SecretKey key;
    private final JwtParser jwtParser;
    private final long tokenValidityInMilliseconds =  Constants.JWT_EXPIRATION_TOKEN;
    private static final String AUTHORITIES_KEY = "auth";



    public JwtProvider(){
        byte[] keyBytes = Decoders.BASE64.decode( Constants.JWT_SIGNATURE );
        this.key = Keys.hmacShaKeyFor( keyBytes );
        this.jwtParser = Jwts.parser().verifyWith( key ).build();

    }



    public String createToken( Authentication authentication ) {
        String authorities = authentication.getAuthorities().stream().map( GrantedAuthority::getAuthority ).collect( Collectors.joining(",") );

        long now = ( new Date() ).getTime();
        Date validity = new Date( now + this.tokenValidityInMilliseconds );

        return Jwts
                .builder()
                .subject( authentication.getName() )
                .claim( AUTHORITIES_KEY, authorities )
                .signWith( key )
                .expiration(validity)
                .issuedAt( new Date() )
                .compact();
    }

    public boolean validate(String token){
        try{
            final var claims = Jwts.parser().verifyWith(this.key).build().parseSignedClaims(token);
            this.checkTokenExpiration( claims );
            return true;
        }catch (Exception e ){
            return false;
        }
    }

    private void checkTokenExpiration( Jws<Claims> token ){
        try {
            final var payload = token.getPayload();
            if ( payload.getExpiration().before(new Date()) || payload.getIssuedAt().after( new Date((new Date()).getTime() + this.tokenValidityInMilliseconds) ) )
                throw new ExpiredJwtException(null, null, null);
        } catch ( Exception e ) {
            throw new ExpiredJwtException(null, null, null);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }



}
