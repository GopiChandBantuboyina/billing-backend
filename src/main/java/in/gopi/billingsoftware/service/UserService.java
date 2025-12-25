package in.gopi.billingsoftware.service;

import java.util.List;

import in.gopi.billingsoftware.io.UserRequest;
import in.gopi.billingsoftware.io.UserResponse;

public interface UserService {
	
	
	UserResponse createUser(UserRequest request);
	String getUserRole(String email);
	
	List<UserResponse> readUsers();
	
	void deleteUser(String id);

}
