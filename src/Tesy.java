import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by forvoid on 5/31/2017.
 */
public class Tesy {
    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode();
        HashSet hashSet = new HashSet();
        hashSet.add(new Object());

        Map hashMap = Collections.synchronizedMap(new HashMap<Integer,Integer>());
        hashMap.put(new Object(),new Object());

    }
    static class TreeNode{
         int val;
         int right;
         int left;
    }
}
