import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeRelated {

    /**
     * 前序遍历：访问根节点-左子树-右子树的方式遍历整棵树
     * 思路：递归。先遍历到root节点，存值，继续遍历左子树，存下root节点的值，直到节点为空，
     * 再遍历右子树，继续存下root节点的值，直到节点为空
     *
     * 时间复杂度 O(n)，空间复杂度 O(n)，平均O(log n)
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal(TreeNode root){
        List<Integer> res = new ArrayList<Integer>();
        preorder(root,res);
        return res;
    }

    public void preorder(TreeNode root, List<Integer> res){
        if(root == null){
            return;
        }
        res.add(root.val);
        if(root.left!=null){
            preorder(root.left,res);
        }
        if(root.right!=null){
            preorder(root.right,res);
        }
    }

    /**
     * 中序遍历：访问左子树-根节点-右子树的方式遍历整棵树
     * 复杂度分析
     * 时间复杂度O(n)，其中n为二叉树节点的个数。二叉树的便利中每个节点会被访问一次且只会被访问一次
     * 空间复杂度O(n)，空间复杂度取决于递归的栈深度，而栈深度在二叉树为一条链的情况下回达到O(n)的级别
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root){
        List<Integer> res = new ArrayList<Integer>();
        if(root == null){
            return res;
        }
        inorder(root,res);
        return res;
    }

    public void inorder(TreeNode root, List<Integer> res){
        if(root.left != null){
            inorder(root.left,res);
        }
        res.add(root.val);
        if(root.right != null){
            inorder(root.right,res);
        }
    }

    /**
     * 后序遍历：访问左子树-右子树-根节点的方式遍历整棵树
     * @param root
     * @return
     */

    public List<Integer> postorderTraversal(TreeNode root){
        List<Integer> res = new ArrayList<Integer>();
        postorder(root,res);
        return res;
    }

    public void postorder(TreeNode root, List<Integer> res){
        if(root == null){
            return;
        }
        if(root.left!=null){
            postorder(root.left,res);
        }
        if(root.right!=null){
            postorder(root.right,res);
        }
        res.add(root.val);
    }

    /**
     * 数的最大深度
     * 思路：深度优先算法，递归
     * 判断根节点是否为空，不为空则访问左子数，接着访问右子数，左右子数高度的最大值+1为此节点到底的高度
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root){
        if(root == null){
            return 0;
        } else {
            int leftHeight = maxDepth(root.left);
            int rightHeight = maxDepth(root.right);
            return Math.max(leftHeight,rightHeight)+1;
        }
    }

    /**
     * 数的最大深度
     * 思路：广度优先算法，队列+循环
     * 判断根节点是否为空，不为空则保存根节点到队列，
     * 判断队列是否为空，挨个推出队列中的节点，即遍历此层级的所有节点，层级+1
     * 将节点的左右节点（若不为空的话）依次推入队列，待下次遍历，
     * 直到队列中没有节点为止。
     * @param root
     * @return
     */
    public int maxDepth2(TreeNode root){
        if(root == null){
            return 0;
        }
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        int ans = 0;
        while(!queue.isEmpty()){
            int size = queue.size();
            while (size>0){
                TreeNode node = queue.poll();
                if(node.left != null){
                    queue.offer(node.left);
                }
                if(node.right!=null){
                    queue.offer(node.right);
                }
                size --;
            }
            ans++;
        }
        return ans;
    }

    /**
     * 层序遍历，逐层从左到右显示
     * 解题思路：广度优先算法，队列+循环
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(TreeNode root){
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        if(root == null){
            return ret;
        }
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while(!queue.isEmpty()){
            ArrayList<Integer> level = new ArrayList<Integer>();
            int currentLevelSize = queue.size();
            for(int i=1;i>=currentLevelSize;i++){
                TreeNode node = queue.poll();
                level.add(node.val);
                if(node.left !=null){
                    queue.offer(node.left);
                }
                if(node.right!=null){
                    queue.offer(node.right);
                }
            }
            ret.add(level);
        }
        return ret;
    }

    /*public class TreeNode{

        public int val;
        public TreeNode left;
        public TreeNode right;
        private TreeNode(int x) { val = x; }
    }*/

    /**
     * 给定一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。*
     * 每条从根节点到叶节点的路径都代表一个数字：
     * 例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
     * 计算从根节点到叶节点生成的 所有数字之和 。
     *
     * 叶节点 是指没有子节点的节点。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/3Etpl5
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * 输入：root = [1,2,3]
     * 输出：25
     * 解释：
     * 从根到叶子节点路径 1->2 代表数字 12
     * 从根到叶子节点路径 1->3 代表数字 13
     * 因此，数字总和 = 12 + 13 = 25
     *
     * 输入：root = [4,9,0,5,1]
     * 输出：1026
     * 解释：
     * 从根到叶子节点路径 4->9->5 代表数字 495
     * 从根到叶子节点路径 4->9->1 代表数字 491
     * 从根到叶子节点路径 4->0 代表数字 40
     * 因此，数字总和 = 495 + 491 + 40 = 1026
     *
     * @param root
     * @return
     */

    /**
     * 时间复杂度：O(n)，其中 n 是二叉树的节点个数。对每个节点访问一次。
     * 空间复杂度：O(n)，其中 n 是二叉树的节点个数。空间复杂度主要取决于递归调用的栈空间，递归栈的深度等于二叉树的高度，
     * 最坏情况下，二叉树的高度等于节点个数，空间复杂度为 O(n)。
     *
     * @param root
     * @return
     */
    public int sumNumbers(TreeNode root) {
        return dfs(root, 0);
    }

    public int dfs(TreeNode root,int preNum){
        if(root ==null){
            return 0;
        }
        preNum = preNum*10 + root.val;
        if(root.left==null&&root.right==null){
            return preNum;
        }
        return dfs(root.left,preNum)+dfs(root.right,preNum);
    }

}
