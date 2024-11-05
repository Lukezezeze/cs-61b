package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository implements Serializable{
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(".capers"); // TODO Hint: look at the `join`
    static final File story_folder = Utils.join(".capers","story");                                     //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence()  {
        // TOD
        if (!CAPERS_FOLDER.exists()) {
            CAPERS_FOLDER.mkdir();
        }
        if (!Dog.DOG_FOLDER.exists()) {
            Dog.DOG_FOLDER.mkdir();
        }


        try {
            if (!story_folder.exists()) {
                story_folder.createNewFile();  // 创建 story 文件
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        // TODO
        // 读取当前故事的内容（如果文件存在）
        String existingContent = "";
        if (story_folder.exists()) {
            existingContent = Utils.readContentsAsString(story_folder);
        }

        // 将新的内容附加到现有内容后，并加上换行符
        String newContent = existingContent + text + "\n";

        // 写回到文件
        Utils.writeContents(story_folder, newContent);
        String newtxt = Utils.readContentsAsString(story_folder);
        System.out.println(newtxt);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        // TODO
        Dog dog = new Dog(name, breed, age);

        dog.saveDog();

        // 打印狗的信息
        System.out.println(dog.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        // TODO
        //根据名字获取specific dog；
        Dog temp;
        File dogFile = Utils.join(Dog.DOG_FOLDER, name + ".ser");
        temp = readObject(dogFile, Dog.class);

        //更新狗的年龄
        temp.haveBirthday();

        Utils.writeObject(dogFile,temp);



    }
}
