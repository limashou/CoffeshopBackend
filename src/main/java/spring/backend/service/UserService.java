package spring.backend.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.backend.dto.UserRequest;
import spring.backend.dto.UserResponse;
import spring.backend.entity.User;
import spring.backend.exception.AppRuntimeException;
import spring.backend.exception.AppUsernameNotFoundException;
import spring.backend.repository.jpa.RoleRepository;
import spring.backend.repository.jpa.UserRepository;

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
        return userRepository.findById(id).orElseThrow(()-> new AppRuntimeException("User not found", HttpStatus.NOT_FOUND));
    }
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

    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new AppRuntimeException("User is not authenticated", HttpStatus.UNAUTHORIZED);
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
                throw new AppUsernameNotFoundException("User not found for username: " +
                        usernameFromAccessToken, HttpStatus.NOT_FOUND);
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
        if (principal instanceof UserDetails) {
            UserDetails userDetail = (UserDetails) principal;
            String usernameFromAccessToken = userDetail.getUsername();
            User user = userRepository.findByUsername(usernameFromAccessToken);
            if (user != null) {
                UserRequest userRequest = modelMapper.map(user, UserRequest.class);
                return userRequest;
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
        List<User> users = (List<User>) userRepository.findWithRole();
        Type setOfDTOsType = new TypeToken<List<UserResponse>>(){}.getType();
        List<UserResponse> userResponses = modelMapper.map(users, setOfDTOsType);
        return userResponses;
    }
}
