package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    private final String blobid;        // 文件内容的哈希值
    private final byte[] content;     // 文件内容
    private final String fileName;    // 文件名（用于调试或展示）

    public Blob(String fileName, byte[] content,String blobid) {
        this.fileName = fileName;
        this.content = content;
        this.blobid = blobid;// 根据内容计算哈希值
    }





}
