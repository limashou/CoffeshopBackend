package spring.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import spring.backend.entity.Role;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Set<Role> roles;
}