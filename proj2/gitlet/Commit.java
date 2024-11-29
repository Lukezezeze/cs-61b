package gitlet;

// TODO: any imports you need here


import java.io.Serializable;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /** the Date type of time you commit */
    private Date timecommit;

    /** the String format type of commit time */
    private String timestamp;

    /** the commit id for this commit */
    private String id;

    /** the parentcommit */
    private List<String> parent;

    /** the blobs a commit have */
    private Map<String,String> blobid = new HashMap<>();

    /* TODO: fill in the rest of this class. */

    //constructor
    public Commit(String id ,List<String> parent, String message,Map<String,String> blobid,String timestamp) {
        this.id = id;
        this.blobid = blobid;
        this.message = message;
        this.parent = parent;
        this.timestamp = timestamp;
    }


    public String getblobid(String filename) {
        return blobid.get(filename);
    }

    public String getcommitid() {
        return id;
    }

    public List<String> getParent() {
        return parent;
    }

    public Map<String,String> getblobidlist() {
        return blobid;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
