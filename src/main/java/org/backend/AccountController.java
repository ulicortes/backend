package org.backend;

import org.backend.dto.LoginDto;
import org.backend.dto.UserBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acceso")
public class AccountController {
    @Autowired
    AccountService servicio;

    @PostMapping("/registrarse")
    public ResponseEntity<String> nuevoUsuario(UserBody user) {
        try {
            return new ResponseEntity<>("Usuario agregado", this.servicio.nuevo(user));
        } catch(Exception e) {
            return new ResponseEntity<>("Algo salio mal", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/ingresar")
    public ResponseEntity<Object> ingresarUsuario(LoginDto user) {
        try {
            return this.servicio.ingreso(user);
        } catch(Exception e) {
            return new ResponseEntity<>("Algo salio mal", HttpStatus.BAD_REQUEST);
        }
    }

}
