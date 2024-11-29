package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Stage implements Serializable {
    /** Map to store files staged for addition (fileName -> blobID). */
    private Map<String, String> addedFiles;

    /** Set to store files staged for removal. */
    private Set<String> removedFiles;

    public Stage() {
        this.addedFiles = new HashMap<>();
        this.removedFiles = new HashSet<>();
    }

    /** Getters for the maps and sets. */
    public Map<String, String> getAddedFiles() {
        return addedFiles;
    }

    public Set<String> getRemovedFiles() {
        return removedFiles;
    }

    /** Add a file to the staging area. */
    public void addFile(String fileName, String blobID) {
        // Remove from removal set if it exists there.
        removedFiles.remove(fileName);
        // Add or update the file in the added map.
        addedFiles.put(fileName, blobID);
    }

    /** Remove a file from the staging area (used when unchanged). */
    public void removeFile(String fileName) {
        addedFiles.remove(fileName);
    }

    /** Mark a file for removal. */
    public void stageForRemoval(String fileName) {
        addedFiles.remove(fileName);
        removedFiles.add(fileName);
    }

    /** Clear the staging area (after a commit). */
    public void clear() {
        addedFiles.clear();
        removedFiles.clear();
    }

    /** Serialize the Stage object to a file. */
    public void saveStage(File stageFile) {
        Utils.writeObject(stageFile, this);
    }

    /** Load the Stage object from a file. */
    public static Stage loadStage(File stageFile) {
        if (!stageFile.exists()) {
            return new Stage();
        }
        return Utils.readObject(stageFile, Stage.class);
    }
}
