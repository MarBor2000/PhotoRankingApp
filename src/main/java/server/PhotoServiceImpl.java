package server;

import service.PhotoService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoServiceImpl extends UnicastRemoteObject implements PhotoService {
    private static final long serialVersionUID = 1L;
    private Map<String, Photo> photos;
    private List<String> bannedWords;

    protected PhotoServiceImpl() throws RemoteException {
        super();
        photos = new HashMap<>();
        bannedWords = loadBannedWords("C:\\Users\\marbo\\OneDrive\\Desktop\\PhotoRankingApp\\src\\main\\resources\\badwords.txt");
    }

    @Override
    public void uploadPhoto(String name, byte[] data) throws RemoteException {
        photos.put(name, new Photo(name, data));
    }

    @Override
    public byte[] downloadPhoto(String name) throws RemoteException {
        if (photos.containsKey(name)) {
            return photos.get(name).getData();
        } else {
            throw new RemoteException("Photo not found.");
        }
    }

    @Override
    public void addComment(String photoName, String username, String comment) throws RemoteException {
        if (photos.containsKey(photoName)) {
            if (containsBannedWords(comment)) {
                throw new RemoteException("Comment contains banned words.");
            } else {
                photos.get(photoName).addComment(username + ": " + comment);
            }
        } else {
            throw new RemoteException("Photo not found.");
        }
    }

    @Override
    public String[] getComments(String photoName) throws RemoteException {
        if (photos.containsKey(photoName)) {
            return photos.get(photoName).getComments().toArray(new String[0]);
        } else {
            throw new RemoteException("Photo not found.");
        }
    }

    @Override
    public void ratePhoto(String photoName, int rating) throws RemoteException {
        if (photos.containsKey(photoName)) {
            photos.get(photoName).addRating(rating);
        } else {
            throw new RemoteException("Photo not found.");
        }
    }

    @Override
    public double getAverageRating(String photoName) throws RemoteException {
        if (photos.containsKey(photoName)) {
            return photos.get(photoName).getAverageRating();
        } else {
            throw new RemoteException("Photo not found.");
        }
    }

    @Override
    public int getViewCount(String photoName) throws RemoteException {
        if (photos.containsKey(photoName)) {
            return photos.get(photoName).getViewCount();
        } else {
            throw new RemoteException("Photo not found.");
        }
    }

    @Override
    public void incrementViewCount(String photoName) throws RemoteException {
        if (photos.containsKey(photoName)) {
            photos.get(photoName).incrementViewCount();
        } else {
            throw new RemoteException("Photo not found.");
        }
    }

    @Override
    public String[] getPhotoNames() throws RemoteException {
        return photos.keySet().toArray(new String[0]);
    }

    private boolean containsBannedWords(String comment) {
        for (String word : bannedWords) {
            if (comment.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private List<String> loadBannedWords(String fileName) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
