package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** the object dir */
    public static  final  File OBJECTS_DIR = join(GITLET_DIR,"Objectc");

    /** the commit object dir */
    public static final File COMMITS_DIR = join(OBJECTS_DIR,"commits");

    /** the blob object dir */
    public static final File BLOBS_DIR = join(OBJECTS_DIR,"blobs");

    /** the branch_dir */
    public static final File Refs = join(GITLET_DIR,"refs");

    /** The head branch dir */
    public static final File heads = join(Refs,"heads");

    /** the default branch dir */
    public static final File MASTER_file = join(heads,"master");

    /** The Head filr pointer */
    public static final File HEAD_DIR = join(GITLET_DIR,"HEAD");

    /** the Stage area */
     public static final File STAGE_DIR = join(GITLET_DIR,"stage_area");

    /* TODO: fill in the rest of this class. */

    /** creat gitlet internal structures dir */
    public static void setup() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }
        if (!OBJECTS_DIR.exists()) {
            OBJECTS_DIR.mkdir();
        }
        if (!COMMITS_DIR.exists()) {
            COMMITS_DIR.mkdir();
        }
        if (!BLOBS_DIR.exists()) {
            BLOBS_DIR.mkdir();
        }
        if (!Refs.exists()) {
            Refs.mkdir();
        }
        if (!heads.exists()) {
            heads.mkdir();
        }

        try {
            if (!HEAD_DIR.exists() && !MASTER_file.exists() && !STAGE_DIR.exists()) {
                HEAD_DIR.createNewFile();
                MASTER_file.createNewFile();
                STAGE_DIR.createNewFile();
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }

    }



    public static void init() {

        //create a initcommit Object
        Date currentTime = new Date(0);
        String message = "initial commit";
        List<String> parents = null;
        Map<String,String> pathtoBlobid = new HashMap<>();
        String id = generateID(currentTime,message,parents,pathtoBlobid);

        Commit init_commit = new Commit(id,parents,message,pathtoBlobid,dateToTimeStamp(currentTime));

        // write the commit object in a file
        File init = join(COMMITS_DIR,id);
        try {
            if (!init.exists()) {
                init.createNewFile();
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
        writeObject(init,init_commit);

        //switch master branch and point to init commit
        writeContents(MASTER_file,id);

        // make head pointer point to master branch
        writeContents(HEAD_DIR,"master");

    }

    public static void add(String filename) {
        //check the file is exist?
        File workfile = join(CWD,filename);
        if(!workfile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        // Step 2: 加载当前提交和暂存区
        Commit currentCommit = getcurrentcommit();
        Stage stage = Stage.loadStage(STAGE_DIR);

        // Step 3: 计算文件的 SHA-1 值
        String blobContent = readContentsAsString(workfile);
        String newBlobID = Utils.sha1(blobContent);

        // Step 4: 获取当前提交中的文件 blob ID
        String currentBlobID = currentCommit.getblobid(filename);

        // Step 5: 比较文件状态
        if (newBlobID.equals(currentBlobID)) {
            // 文件未修改，且已在暂存区，则移除暂存区中的记录
            stage.removeFile(filename);
        } else {
            // 文件修改，或新增，将其添加到暂存区
            if (stage == null) {
                stage = new Stage();  // 如果 stage 为 null，初始化一个新的 Stage
            }
            stage.addFile(filename, newBlobID);

            // 保存 blob 到 blobs 目录
            File blobFile = join(BLOBS_DIR, newBlobID);
            if (!blobFile.exists()) {
                writeContents(blobFile, blobContent);
            }
        }

        // Step 6: 保存暂存区状态
        stage.saveStage(STAGE_DIR);


    }

    public static void commit(String message) {

        if (message == null || message.trim().isEmpty()) {
            System.out.println("Please enter a commit message.");
            return; // Exit the method early if the message is invalid
        }

        //create a new commit object
        Date currenttime = new Date();
        String timecommit = dateToTimeStamp(currenttime); //get the time commit
        Commit precommit = getcurrentcommit();           // get the precommit
        String precommitid = precommit.getcommitid();    // get the precommit object to store information
        List<String> parent = new ArrayList<>();     //get the parent id
        parent.add(precommitid);                         //store the parentid to a list;
        //System.out.print(precommitid);
        Map<String,String> blobidlist = precommit.getblobidlist(); //get the precommit's bloblist

        // check the stage area to commit
        Stage stage = Stage.loadStage(STAGE_DIR);

        // compare stage area's blobs and commit's to add or remove
        Map<String,String> stageadd = stage.getAddedFiles();
        //failure cases check
        if (stageadd == null) {
            System.out.println("No changes added to the commit.");
        }
        blobidlist.putAll(stageadd);     //upadate commit for stage_for_add

        for (String key : stage.getRemovedFiles()) {
            blobidlist.remove(key);      // update commit for stage_for_remove
        }

        stage.clear();//clear the stage area
        stage.saveStage(HEAD_DIR);  //update the contents in stage file

        // create new commit and save it in new commit_dir
        String newcommitid = generateID(currenttime,message,parent,blobidlist);
        Commit newcommit = new Commit(newcommitid,parent,message,blobidlist,timecommit);

        if (newcommit.getParent().isEmpty()) {
            System.out.print("no");
        }

        if (!join(COMMITS_DIR,newcommitid).exists()) {
            writeObject(join(COMMITS_DIR,newcommitid),newcommit);
        }

        // Update the current branch (e.g., master) to point to the new commit
        writeContents(MASTER_file, newcommitid);

        // Update the HEAD pointer to point to the current branch
        writeContents(HEAD_DIR, "master");

    }

    public static void rm(String filename) {
        Commit currentcommit = getcurrentcommit(); //get the current commit
        Stage stage = Stage.loadStage(STAGE_DIR);  //check stage stutus

        //obtain two maps ons is commit's bloblist and the other is stage_for_add
        Map<String,String> bloblist = currentcommit.getblobidlist();
        Map<String,String> stageadd = stage.getAddedFiles();
        Set<String> stage_for_remove = stage.getRemovedFiles();

        //faliure case
        if (!stageadd.containsKey(filename) && !bloblist.containsKey(filename)) {
            System.out.println("No reason to remove the file.");
        }

        //first case: if the file is in the stage area but dont in bloblist
        if (stageadd.containsKey(filename) && !bloblist.containsKey(filename)) {
            stageadd.remove(filename);
        } else {
            // both in but not exist CWD;
            if (!join(CWD,filename).exists()) {
                stage_for_remove.add(filename);
            } else {
                stage_for_remove.add(filename);
                join(CWD,filename).delete();
            }
        }

        stage.saveStage(STAGE_DIR);
    }

    public static void log() {
        List<String> commitlist = getCommitList();
        if (commitlist.isEmpty()) {
            System.out.println("No commits found.");
            return; // 如果提交列表为空，直接返回
        }

        for (String commitID : commitlist) {
            Commit commit = readObject(join(COMMITS_DIR,commitID), Commit.class);
            System.out.println("===");
            System.out.println("commit "+commit.getcommitid());
            if (commit.getParent().size() > 1) {
                System.out.print("Merge:");
                for (String id : commit.getParent()) {
                    System.out.print(" "+id);
                }
            }
            System.out.println("Date: "+commit.getTimestamp());
            System.out.println(commit.getMessage());
            if (!commit.getParent().isEmpty()) {
                System.out.println(); // 输出空行
            }
        }
    }

    private static List<String> getCommitList() {
        List<String> commitList = new ArrayList<>();
        Commit cur = getcurrentcommit();

        commitList.add(cur.getcommitid());

        // 如果当前提交有父提交，则递归获取父提交
        while (cur != null && cur.getParent() != null && !cur.getParent().isEmpty()) {
            String commitId = cur.getParent().get(0);  // 获取父提交 ID
            commitList.add(commitId);
            cur = readObject(join(COMMITS_DIR, commitId), Commit.class);
        }

        return commitList;
    }

    public static void global_log() {
        List<String> commitlist = plainFilenamesIn(COMMITS_DIR);// get all commit files in commit_dir
        for (String commitid : commitlist) {
            Commit commits = readObject(join(COMMITS_DIR, commitid), Commit.class); //get commit object
            System.out.println("===");
            System.out.println("commit "+commits.getcommitid());
            System.out.println("Date: "+commits.getTimestamp());
            System.out.println(commits.getMessage());
            System.out.println(); // 输出空行
        }
    }

    public static void find(String commit_message) {
        List<String> commitlist = plainFilenamesIn(COMMITS_DIR);// get all commit files in commit_dir
        int count = 0; //record the nums of find
        for (String commitid : commitlist) {
            Commit commits = readObject(join(COMMITS_DIR, commitid), Commit.class); //get commit object
            if (commits.getMessage() == commit_message) {
                System.out.println(commitid);
                count++;
            }
        }
        //faliure case: no commits coubld be found
        if (count == 0) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status() {
        //try get ref's branches
        List<String> branchlist = plainFilenamesIn(Refs);

        //get stage_area files
        Stage stage = Stage.loadStage(STAGE_DIR);
        Map<String,String> stage_for_add = stage.getAddedFiles();
        Set<String> addkeys = stage_for_add.keySet();

        //get stage_for_remove files
        Set<String> stage_for_remove = stage.getRemovedFiles();

        //the branch part
        System.out.println("=== Branches ===");
        for(String branch : branchlist) {
            if (branch == readContentsAsString(HEAD_DIR)) {
                System.out.println("*"+branch);
                branchlist.remove(branch);
            }
        }
        for (String branch : branchlist) {
            System.out.println(branch);
        }
        System.out.println();

        //stage_for_add part
        System.out.println("=== Staged Files ===");
        for(String key : addkeys) {
            System.out.println(key);
        }
        System.out.println();

        //stage_for_remove part
        System.out.println("=== Removed Files ===");
        for(String key : stage_for_remove) {
            System.out.println(key);
        }
        System.out.println();
    }

    public static void checkoutfilename(String filename) {
        Commit currentcommit = getcurrentcommit();
        Map<String,String> Bloblist = currentcommit.getblobidlist();

        //faliure case: if this file dont exist
        if (!Bloblist.containsKey(filename)){
            System.out.println("File does not exist in that commit.");
        }

        //check the file exist in CWD? if so overwrite it ,else,put the flie in CWD
        String blobid = Bloblist.get(filename);
        String newcontents = readContentsAsString(join(BLOBS_DIR,blobid));
        putfileincwd(filename,newcontents);

    }

    public static void checkoutcommitidfilename(String commitid, String filename) {
        Commit commit = readObject(join(COMMITS_DIR,commitid), Commit.class);
        Map<String,String> Bloblist = commit.getblobidlist();
        //System.out.println(Bloblist);
        //faliure case: if this file dont exist
        if (!join(COMMITS_DIR,commitid).exists()) {
            System.out.println("No commit with that id exists.");
        }
        if (!Bloblist.containsKey(filename)){
            System.out.println("File does not exist in that commit.");
        }

        //check the file exist in CWD? if so overwrite it ,else,put the flie in CWD
        String blobid = Bloblist.get(filename);
        String newcontents = readContentsAsString(join(BLOBS_DIR,blobid));
        putfileincwd(filename,newcontents);
    }

    public static void checkoutbranchname(String branchname) {

        //failure case:
        if (!join(Refs,branchname).exists()) {
            System.out.println("No such branch exists.");
        } else {
            if (readContentsAsString(HEAD_DIR) == branchname) {
                System.out.println("No need to checkout the current branch.");
            }
        }

        //get two branchs's commit object and swithch branch
        Commit commita = getcurrentcommit();
        changebranch(branchname);
        Commit commitb = getcurrentcommit();

        //get two commits' bloblits
        Map<String, String> bloblista = commita.getblobidlist();
        Map<String, String> bloblistb = commitb.getblobidlist();
        Set<String> filelista = bloblista.keySet();
        Set<String> filelistb = bloblistb.keySet();
        //compare two bloblist

        for(String filename : filelista) {
            if (bloblistb.containsKey(filename)) {    //if the file was tracked by two commits
                if (bloblista.get(filename) != bloblistb.get(filename)) {
                    String commitbid = bloblistb.get(filename);
                    String newcontents = readContentsAsString(join(COMMITS_DIR,commitbid));
                    putfileincwd(filename,newcontents);
                }
            } else {   //only tracked by a
                join(CWD,filename).delete();
            }
        }

        for (String filename : filelistb) {        // only tracked by commitb
            if (!bloblista.containsKey(filename)) {
                if (!join(CWD,filename).exists()) {
                    String commitbid = bloblistb.get(filename);
                    String newcontents = readContentsAsString(join(COMMITS_DIR,commitbid));
                    putfileincwd(filename,newcontents);
                } else {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first");
                }
            }
        }

        //clear the stage_area
        Stage stage = Stage.loadStage(STAGE_DIR);
        stage.clear();

    }

    private static Commit getcurrentcommit() {
        String branch = readContentsAsString(HEAD_DIR);//get the current branch
        String commitID = readContentsAsString(join(heads, branch)); // 获取当前分支指向的提交 ID
        return readObject(join(COMMITS_DIR, commitID), Commit.class); // 加载提交对象
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    private static String generateID(Date currentTime,String message,List<String> parents,Map<String,String> pathToBlobID) {
        String parentsStr = (parents == null) ? "null" : parents.toString();
        return Utils.sha1(dateToTimeStamp(currentTime), message, parentsStr, pathToBlobID.toString());
    }

    private static void changebranch(String branchname) {
        String branch = readContentsAsString(HEAD_DIR);//get the current branch
        branch = branchname;
        writeContents(HEAD_DIR,branch);
    }

    private static void putfileincwd (String filename, String newcontents) {

        if (join(CWD,filename).exists()) {
            String oldcontents = readContentsAsString(join(CWD,filename));
            oldcontents = newcontents;
            writeContents(join(CWD,filename),oldcontents);  //overwrite it
        } else {
            writeContents(join(CWD,filename),newcontents);
        }
    }






}
