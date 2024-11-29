package org.backend.AuthPackage;

import org.backend.dto.UserBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class AuthController {
    @Autowired
    AuthService servicio;

    @PostMapping("")
    public ResponseEntity<String> nuevoUsuario(UserBody user) {
        try {
            return new ResponseEntity<>("Usuario agregado", this.servicio.nuevo(user));
        } catch(Exception e) {
            return new ResponseEntity<>("Algo salio mal", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> listaUsuarios() {
        try {
            return new ResponseEntity<>(this.servicio.listaUsuarios(), HttpStatus.ACCEPTED);
        } catch(Exception e) {
            return new ResponseEntity<>("Algo salio mal", HttpStatus.BAD_REQUEST);
        }
    }
}
