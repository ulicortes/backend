package org.backend;

import org.backend.dto.UserDto;
import org.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {
    @Query("select new org.backend.dto.UserDto(u.userName, u.mail)" +
            "from User u")
    List<UserDto> listaUsuarios();
}
