package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PhotoService extends Remote {
    void uploadPhoto(String photoName, byte[] data) throws RemoteException;
    byte[] downloadPhoto(String photoName) throws RemoteException;
    void addComment(String photoName, String username, String comment) throws RemoteException;
    String[] getComments(String photoName) throws RemoteException;
    void ratePhoto(String photoName, int rating) throws RemoteException;
    double getAverageRating(String photoName) throws RemoteException;
    int getViewCount(String photoName) throws RemoteException;
    void incrementViewCount(String photoName) throws RemoteException;
    String[] getPhotoNames() throws RemoteException;
}
