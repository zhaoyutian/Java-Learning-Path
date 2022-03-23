import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionRelated {
    /**
     * 给你一个下标从 0 开始、由 n 个整数组成的数组 arr 。
     *
     * arr 中两个元素的 间隔 定义为它们下标之间的 绝对差 。更正式地，arr[i] 和 arr[j] 之间的间隔是 |i - j| 。
     *
     * 返回一个长度为 n 的数组 intervals ，其中 intervals[i] 是 arr[i] 和 arr 中每个相同元素（与 arr[i] 的值相同）的 间隔之和 。
     *
     * 注意：|x| 是 x 的绝对值。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/intervals-between-identical-elements
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * 输入：arr = [2,1,3,1,2,3,3]
     * 输出：[4,2,7,2,4,4,5]
     * 解释：
     * - 下标 0 ：另一个 2 在下标 4 ，|0 - 4| = 4
     * - 下标 1 ：另一个 1 在下标 3 ，|1 - 3| = 2
     * - 下标 2 ：另两个 3 在下标 5 和 6 ，|2 - 5| + |2 - 6| = 7
     * - 下标 3 ：另一个 1 在下标 1 ，|3 - 1| = 2
     * - 下标 4 ：另一个 2 在下标 0 ，|4 - 0| = 4
     * - 下标 5 ：另两个 3 在下标 2 和 6 ，|5 - 2| + |5 - 6| = 4
     * - 下标 6 ：另两个 3 在下标 2 和 5 ，|6 - 2| + |6 - 5| = 5
     *
     * @param arr
     * @return
     */
    public long[] getDistances(int[] arr) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        long[] ret = new long[arr.length];
        for(int i=0;i<arr.length;i++){
            int key = arr[i];
            List<Integer> list;
            if(map.containsKey(key)){
                list = map.get(key);
            } else {
                list = new ArrayList<>();
            }
            list.add(i);
            map.put(key,list);
        }
        for(int i=0;i<arr.length;i++){
            List<Integer> list = map.get(arr[i]);
            long abs = 0;
            for (int j=0;j<list.size();j++) {
                if (list.get(j) != i) {
                    abs = abs + Math.abs(i-list.get(j));
                }
            }
            ret[i] = abs;
        }
        return ret;
    }

    /**
     * 银行内部的防盗安全装置已经激活。给你一个下标从 0 开始的二进制字符串数组 bank ，表示银行的平面图，这是一个大小为 m x n 的二维矩阵。 bank[i] 表示第 i 行的设备分布，由若干 '0' 和若干 '1' 组成。'0' 表示单元格是空的，而 '1' 表示单元格有一个安全设备。
     *
     * 对任意两个安全设备而言，如果同时 满足下面两个条件，则二者之间存在 一个 激光束：
     *
     * 两个设备位于两个 不同行 ：r1 和 r2 ，其中 r1 < r2 。
     * 满足 r1 < i < r2 的 所有 行 i ，都 没有安全设备 。
     * 激光束是独立的，也就是说，一个激光束既不会干扰另一个激光束，也不会与另一个激光束合并成一束。
     *
     * 返回银行中激光束的总数量。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/number-of-laser-beams-in-a-bank
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * 输入：bank = ["011001","000000","010100","001000"]
     * 输出：8
     * 解释：在下面每组设备对之间，存在一条激光束。总共是 8 条激光束：
     *  * bank[0][1] -- bank[2][1]
     *  * bank[0][1] -- bank[2][3]
     *  * bank[0][2] -- bank[2][1]
     *  * bank[0][2] -- bank[2][3]
     *  * bank[0][5] -- bank[2][1]
     *  * bank[0][5] -- bank[2][3]
     *  * bank[2][1] -- bank[3][2]
     *  * bank[2][3] -- bank[3][2]
     * 注意，第 0 行和第 3 行上的设备之间不存在激光束。
     * 这是因为第 2 行存在安全设备，这不满足第 2 个条件。
     *
     * @param bank
     * @return
     */
    public int numberOfBeams(String[] bank) {
        int sum = 0;
        int lastSum = 0;
        for(int i=0;i<bank.length;i++){
            String s = bank[i];
            // 用空格代替特定字符，然后计算与以前字符串的长度差,再除以特定字符的长度，即可得出A中所占b的个数。
            int count= (s.length()-s.replace("1","").length())/"1".length();
            if(count !=0){
                sum = sum+lastSum*count;
                lastSum = count;
            }
        }
        return sum;
    }
}
