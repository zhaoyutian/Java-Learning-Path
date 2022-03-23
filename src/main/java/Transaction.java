import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Transaction {


    /**
     * 不限交易次数，但同时只能持有一只股票，最大利润计算
     * 解题思路：贪心算法 (0...n)max{0,a[i]-a[i-1]}
     * @param prices
     * @return
     */
    public int maxProfit2TanXin(int prices[]){
        int sum = 0;
        for(int i =1;i<prices.length;i++){
            sum =sum+ Math.max(0,prices[i]-prices[i-1]);
        }
        return sum;
    }

    /**
     * 最大利润计算
     * 解题思路：假设在之前某天的最低价买入，再计算利润即可。
     * @param prices
     * @return
     */
    public int maxProfit(int prices[]){
        int lowest = 1000;
        int maxProfit = 0;
        for(int i =0;i<prices.length;i++){
            if(lowest>prices[i]){
                lowest = prices[i];
            }
            if(maxProfit<prices[i]-lowest){
                maxProfit = prices[i]-lowest;
            }
        }
        return maxProfit;
    }

    /**
     * 不限交易次数，但同时只能持有一只股票，最大利润计算
     * 解题思路：动态规划
     * dp[i][j] 代表第i个点是否持有股票，j=0代表未持有，j=1代表持有
     * 假设此点未持有股票，向前推导，前一点抛出的利润与此点抛出的利润比较，取最大值
     * 假设此点持有股票，向前推导，前一点仍然持有的利润与此点买入的利润比较，取最大值
     * 返回最后一天未持有股票的利润，当然，也可以返回最后一天持有与未持有股票利润的最大值，但此处默认未持有利润更大
     * @param prices
     * @return
     */
    public int maxProfit2Dynamic(int prices[]){
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for(int i = 1;i<n;i++){
            dp[i][0] = Math.max(dp[i-1][0],dp[i-1][1]+prices[i]);
            dp[i][1] = Math.max(dp[i-1][1],dp[i-1][0]-prices[i]);
        }
        return dp[n-1][0];
    }

    public int canCompleteCircuit(int[] gas, int[] cost) {
        int ret = -1;
        int left = 0;
        for(int i=0;i<gas.length;i++){
            left = left+gas[i];
            if(left<cost[i]){
                left = 0;
                continue;
            }
            left = left -cost[i];
            int j = i==gas.length-1?0: i+1;
            while(true){
                left = left+gas[j];
                if(left<cost[j]){
                    break;
                }
                left = left -cost[j];
                j = j==gas.length-1?0: j+1;
                if(i == j){
                    return i;
                }
            }
        }
        return ret;
    }

    public boolean canJump(int[] nums) {
        int n = nums.length;
        int rightmost = 0;
        for (int i = 0; i < n; ++i) {
            if (i <= rightmost) {
                rightmost = Math.max(rightmost, i + nums[i]);
                if (rightmost >= n - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canJump2(int[] nums) {
        List<Boolean> list = new ArrayList<Boolean>(nums.length);
        Collections.fill(list,false);
        list.set(0, true);
        for(int i=0;i<nums.length;i++){
            for(int j = 0;j<i;j++){
                if(list.get(j) && nums[j]+j>=i){
                    list.set(i,true);
                    break;
                }
            }
        }
        return list.get(nums.length - 1);
    }
}
