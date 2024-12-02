package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validatenums(firstArg,args,1);
                if (!Repository.GITLET_DIR.exists()) {
                    Repository.setup();
                    Repository.init();
                } else {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateifgitdir();
                validatenums(firstArg,args,2);
                Repository.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case"commit":
                validatenums(firstArg,args,2);
                validateifgitdir();
                Repository.commit(args[1]);
                break;
            case"rm":
                validateifgitdir();
                validatenums(firstArg,args,2);
                Repository.rm(args[1]);
                break;
            case"log":
                validatenums(firstArg,args,1);
                validateifgitdir();
                Repository.log();
                break;
            case"global-log":
                validatenums(firstArg,args,1);
                validateifgitdir();
                Repository.global_log();
                break;
            case"find":
                validatenums(firstArg,args,2);
                validateifgitdir();
                Repository.find(args[1]);
                break;
            case"status":
                validatenums(firstArg,args,1);
                validateifgitdir();
                Repository.status();
                break;
            case"checkout":
                if (args.length == 3 && args[1].equals("--")) {
                    validateifgitdir();
                    Repository.checkoutfilename(args[2]);
                    break;
                } else if (args.length == 4) {
                    validateifgitdir();
                    Repository.checkoutcommitidfilename(args[1],args[3]);
                    break;
                } else if (args.length == 2) {
                    validateifgitdir();
                    Repository.checkoutbranchname(args[1]);
                    break;
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(4);
                    break;
                }
            default:
                System.out.println("No command with that name exists.");
                System.exit(2);
                break;
        }
    }

    public static void validatenums(String cmd, String[] args, int n) {
        if (args[0] != cmd || args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(1);
        }
    }

    public static void validateifgitdir() {
        if (!Repository.GITLET_DIR.exists() || !Repository.GITLET_DIR.isDirectory()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0); // Optionally terminate the program if not in a Gitlet directory
        }
    }
}
