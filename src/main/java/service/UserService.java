package service;

import server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote {
    void registerUser(String username, String password, String role) throws RemoteException;
    User loginUser(String username, String password) throws RemoteException;
}
