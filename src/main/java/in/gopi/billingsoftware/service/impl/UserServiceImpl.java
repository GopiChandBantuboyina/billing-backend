package in.gopi.billingsoftware.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.gopi.billingsoftware.entity.UserEntity;
import in.gopi.billingsoftware.exception.UserAlreadyExistsException;
import in.gopi.billingsoftware.io.UserRequest;
import in.gopi.billingsoftware.io.UserResponse;
import in.gopi.billingsoftware.repository.UserRepository;
import in.gopi.billingsoftware.service.UserService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponse createUser(UserRequest request) {
		
		//Check for existing user
	    if (userRepository.existsByEmail(request.getEmail())) {
	        throw new UserAlreadyExistsException("User with email '" + request.getEmail() + "' already exists");
	    }
		
		UserEntity newUser =  convertToEntity(request);
		newUser = userRepository.save(newUser);
		
		return convertToResponse(newUser);
	}

	private UserResponse convertToResponse(UserEntity newUser) {
		
		return UserResponse.builder()
			.userId(newUser.getUserId())
			.name(newUser.getName())
			.email(newUser.getEmail())
			.role(newUser.getRole())
			.createdAt(newUser.getCreatedAt())
			.updatedAt(newUser.getUpdatedAt())
			.build();
			
	}

	private UserEntity convertToEntity(UserRequest request) {
	    return UserEntity.builder()
	        .userId(UUID.randomUUID().toString())
	        .name(request.getName())
	        .email(request.getEmail())
	        .password(passwordEncoder.encode(request.getPassword())) // <-- encode here
	        .role(request.getRole())
	        .build();
	}

	
	
	
	
	
	@Override
	public String getUserRole(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email)
		.orElseThrow(() -> new UsernameNotFoundException("User not found for the" +email));
		
		return userEntity.getRole();
	}

	@Override
	public List<UserResponse> readUsers() {
		return userRepository.findAll()
		.stream()
		.map(user -> convertToResponse(user))
		.collect(Collectors.toList());
		
	}

	@Override
	public void deleteUser(String id) {
		UserEntity userEntity =  userRepository.findByUserId(id)
		.orElseThrow( () -> new UsernameNotFoundException("User not found for the "+ id));
		
		userRepository.delete(userEntity);
		
	}

}
