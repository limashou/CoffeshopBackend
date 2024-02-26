package spring.backend.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.backend.dto.user.UserRequest;
import spring.backend.dto.user.UserResponse;
import spring.backend.entity.User;
import spring.backend.exception.AppRuntimeException;
import spring.backend.exception.AppUsernameNotFoundException;
import spring.backend.repository.jpa.RoleRepository;
import spring.backend.repository.jpa.UserRepository;

import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    ModelMapper modelMapper;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public User saveUser(UserRequest userRequest) {
        if(userRequest.getUsername() == null){
            throw new AppRuntimeException("Parameter username is not found in request..!!", HttpStatus.NO_CONTENT);
        } else if(userRequest.getPassword() == null){
            throw new AppRuntimeException("Parameter password is not found in request..!!", HttpStatus.NO_CONTENT);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userRequest.getPassword());

        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(encodedPassword);
        if(userRequest.getId() != null){
            User oldUser = userRepository.findById(userRequest.getId()).orElseThrow(() ->
                    new AppRuntimeException("User not found", HttpStatus.NOT_FOUND));
            if(oldUser != null){
                BeanUtils.copyProperties(user,oldUser);
                userRepository.save(oldUser);
            } else {
                throw new AppRuntimeException("Can't find record with identifier: " +
                        userRequest.getId(), HttpStatus.NOT_FOUND);
            }
        }
        return userRepository.save(user);
    }

    public void deleteUserById(Long userId) {
        User user = userRepository.findByIdWithRole(userId);
        if (user != null) {
            user.getRoles().clear();
            userRepository.save(user);
            userRepository.delete(user);
        }
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new AppRuntimeException("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetail) {
            User user = userRepository.findByUsername(userDetail.getUsername());
            if (user != null) {
                return user;
            } else {
                throw new AppUsernameNotFoundException("User not found for username: " +
                        userDetail.getUsername(), HttpStatus.NOT_FOUND);
            }
        } else {
            throw new AppRuntimeException("Principal is not an instance of UserDetails", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public UserRequest getAllInformationUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new AppRuntimeException("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetail) {
            String usernameFromAccessToken = userDetail.getUsername();
            User user = userRepository.findByUsername(usernameFromAccessToken);
            if (user != null) {
                return modelMapper.map(user, UserRequest.class);
            } else {
                throw new AppUsernameNotFoundException("User not found for username: " + usernameFromAccessToken, HttpStatus.NOT_FOUND);
            }
        } else {
            throw new AppRuntimeException("Principal is not an instance of UserDetails",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findWithRole();
        Type setOfDTOsType = new TypeToken<List<UserResponse>>(){}.getType();
        return modelMapper.map(users, setOfDTOsType);
    }
}
