package server;

import service.PhotoService;
import service.UserService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PhotoServer {
    public static void main(String[] args) {
        try {
            PhotoService photoService = new PhotoServiceImpl();
            UserService userService = new UserServiceImpl();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("PhotoService", photoService);
            registry.rebind("UserService", userService);

            System.out.println("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
