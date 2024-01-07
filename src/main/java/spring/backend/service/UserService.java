package spring.backend.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.backend.dto.UserRequest;
import spring.backend.dto.UserResponse;
import spring.backend.entity.User;
import spring.backend.repository.RoleRepository;
import spring.backend.repository.UserRepository;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    ModelMapper modelMapper = new ModelMapper();
    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(null);
    }
    public UserResponse saveUser(UserRequest userRequest) {
        if(userRequest.getUsername() == null){
            throw new RuntimeException("Parameter username is not found in request..!!");
        } else if(userRequest.getPassword() == null){
            throw new RuntimeException("Parameter password is not found in request..!!");
        }

        User savedUser = null;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = userRequest.getPassword();
        String encodedPassword = encoder.encode(rawPassword);

        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(encodedPassword);
        if(userRequest.getId() != null){
            User oldUser = userRepository.findById(userRequest.getId()).orElseThrow(null);
            if(oldUser != null){
                oldUser.setId(user.getId());
                oldUser.setPassword(user.getPassword());
                oldUser.setUsername(user.getUsername());
                oldUser.setEmail(user.getEmail());
                oldUser.setRoles(userRequest.getRoles());

                savedUser = userRepository.save(oldUser);
            } else {
                throw new RuntimeException("Can't find record with identifier: " + userRequest.getId());
            }
        } else {
            savedUser = userRepository.save(user);
        }
        UserResponse userResponse = modelMapper.map(savedUser, UserResponse.class);
        return userResponse;
    }
    public void deleteUserById(Long userId) {
        User user = userRepository.findByIdWithRole(userId);
        if (user != null) {
            user.getRoles().clear();
            userRepository.save(user);
            userRepository.delete(user);
        }
    }

    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("User is not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetail = (UserDetails) principal;
            String usernameFromAccessToken = userDetail.getUsername();
            User user = userRepository.findByUsername(usernameFromAccessToken);

            if (user != null) {
                UserResponse userResponse = modelMapper.map(user, UserResponse.class);
                return userResponse;
            } else {
                throw new UsernameNotFoundException("User not found for username: " + usernameFromAccessToken);
            }
        } else {
            throw new RuntimeException("Principal is not an instance of UserDetails");
        }
    }

    public List<UserResponse> getAllUser() {
        List<User> users = (List<User>) userRepository.findWithRole();
        Type setOfDTOsType = new TypeToken<List<UserResponse>>(){}.getType();
        List<UserResponse> userResponses = modelMapper.map(users, setOfDTOsType);
        return userResponses;
    }
}
