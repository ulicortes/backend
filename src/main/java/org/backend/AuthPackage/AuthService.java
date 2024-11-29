package org.backend.AuthPackage;
import org.backend.AuthRepository;
import org.backend.dto.UserBody;
import org.backend.dto.UserDto;
import org.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    AuthRepository repo;

    public HttpStatusCode nuevo(UserBody user) {
        User u = new User(user.getUsuario(), user.getMail(), user.getPassword());
        this.repo.save(u);
        return HttpStatusCode.valueOf(201);
    }

    public List<UserDto> listaUsuarios() {
        return this.repo.listaUsuarios();
    }
}
