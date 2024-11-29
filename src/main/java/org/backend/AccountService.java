package org.backend;
import org.backend.dto.LoginDto;
import org.backend.dto.UserBody;
import org.backend.dto.UserDto;
import org.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    AccountRepository repo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;

    public HttpStatusCode nuevo(UserBody user) {
        User u = new User(user.getUsuario(), user.getMail(), user.getPassword());
        this.repo.save(u);
        return HttpStatusCode.valueOf(201);
    }

    public List<UserDto> listaUsuarios() {
        return this.repo.listaUsuarios();
    }

    public ResponseEntity<Object> ingreso(LoginDto user) {
        Optional<User> u = this.repo.traerPorNombre(user.getUser());

        if(u.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User usuario = u.get();
        if(!passwordEncoder.matches(user.getPassword(), usuario.getPassword())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String role = "ROLE_USER"; // O "ROLE_ADMIN"
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuario.getUserName(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.createToken(authentication);
        return new ResponseEntity<>(token,HttpStatus.OK);
    }
}
