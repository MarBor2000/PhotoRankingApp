package server;

import service.UserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {
    private Map<String, User> users = new HashMap<>();

    public UserServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerUser(String username, String password, String role) throws RemoteException {
        if (users.containsKey(username)) {
            throw new RemoteException("User already exists.");
        }
        users.put(username, new User(username, password, role));
    }

    @Override
    public User loginUser(String username, String password) throws RemoteException {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new RemoteException("Invalid username or password.");
        }
        return user;
    }
}
