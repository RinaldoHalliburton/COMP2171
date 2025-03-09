package service;

import java.sql.SQLException;

public class LoginService {

	private UserService userService;
	private AdminService adminService;

	public LoginService(UserService userService, AdminService adminService) {
		this.userService = userService;
		this.adminService = adminService;
	}

	public boolean login(String id, String password, boolean isAdmin) throws SQLException {
		if (isAdmin) {
			return adminService.authenticateAdmin(id, password);
		} else
			return userService.authenticateUser(id, password);
	}
}
