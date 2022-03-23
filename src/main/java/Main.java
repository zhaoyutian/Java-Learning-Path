import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    /*public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // receive the input string.
        String inputStr = scan.nextLine();

        // split the string to array.
        String[] strArray = inputStr.split(" ");

        // add ","
        StringBuffer newSb = new StringBuffer();
        for(String str: strArray) {
            newSb.append(str);
            newSb.append(",");
        }

        // print output string.
        System.out.println(newSb.toString());
    }*/

    /*public static void main(String[] args) {
        CollectionRelated collectionRelated = new CollectionRelated();
        int[] arr = new int[]{2,1,3,1,2,3,3};
        long[] arrNew = collectionRelated.getDistances(arr);
        System.out.println(arrNew.toString());
    }*/

    /*public static void main(String[] args) {
        TreeRelated treeRelated = new TreeRelated();
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(9);
        root.right = new TreeNode(0);
        TreeNode root2 = root.left;
        root2.left = new TreeNode(5);
        root2.right = new TreeNode(1);
        treeRelated.sumNumbers(root);
    }*/
    public static void main(String[] args) {
        Transaction transaction = new Transaction();
        int[] gas = new int[]{2,3,4};
        int[] cost  = new int[]{3,4,4};
        transaction.canCompleteCircuit(gas,cost);
    }



}
