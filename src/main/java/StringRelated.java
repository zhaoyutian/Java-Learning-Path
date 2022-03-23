import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StringRelated {

    /**
     * 最长不重复字符串
     * 解体思路：双重遍历，i为头指针，j为末指针，用set判断此substring重复与否
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int ans = 0;

        for(int i = 0; i < n; ++i) {
            for(int j = i + 1; j <= n; ++j) {
                if (this.allUnique(s, i, j)) {
                    ans = Math.max(ans, j - i);
                }
            }
        }
        return ans;
    }

    public boolean allUnique(String s, int start, int end) {
        Set<Character> set = new HashSet();

        for(int i = start; i < end; ++i) {
            Character ch = s.charAt(i);
            if (set.contains(ch)) {
                return false;
            }
            set.add(ch);
        }
        return true;
    }


    /**
     * 最长不重复字符串
     * 解题思路：先判断尾指针的char是否重复，
     * 若不重复，头尾指针相减并与最大值比较，尾指针后移；
     * 若重复，则删除重复char，头指针后移。
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring2(String s) {
        int n = s.length();
        int ans = 0;
        Set<Character> set = new HashSet();
        int i = 0;
        int j = 0;

        while(i < n && j < n) {
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            } else {
                set.remove(s.charAt(i++));
            }
        }
        return ans;
    }

    /**
     * 最长不重复字符串
     * 解题思路：利用map结构，把尾指针的char及尾指针index+1当作key和value存入map
     * 判断尾指针的char是否在map的keySet中，若在，则更新头指针位置
     * 计算头尾指针的长度，并更新最大长度
     * 更新map的value尾尾指针index+1
     * 尾指针后移
     * @param s
     * @return
     */

    public int lengthOfLongestSubstring3(String s) {
        int n = s.length();
        int ans = 0;
        Map<Character, Integer> map = new HashMap();
        int j = 0;

        for(int i = 0; j < n; ++j) {
            if (map.containsKey(s.charAt(j))) {
                i = Math.max((Integer)map.get(s.charAt(j)), i);
            }

            ans = Math.max(ans, j - i + 1);
            map.put(s.charAt(j), j + 1);
        }

        return ans;
    }


    /**
     * 判断是否为回文字符串
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {
        int len = s.length();
        s = s.toLowerCase();
        int i = 0;

        for(int j = len - 1; i < j; --j) {
            while(!this.isAlphanumeric(s.charAt(i))) {
                ++i;
                if (i == len) {
                    return true;
                }
            }
            while(!this.isAlphanumeric(s.charAt(j))) {
                ++j;
            }
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
            ++i;
        }

        return true;
    }

    /**
     * 判断是否为字母
     * @param c
     * @return
     */
    private boolean isAlphanumeric(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || '0' <= c && c <= '9';
    }

    /**
     * 判断是否为回文字符串
     * @param sb
     * @return
     */
    private boolean isPalindrome2(String sb) {
        int left = 0;
        for(int right = sb.length() - 1; left < right; --right) {
            if (sb.charAt(left) != sb.charAt(right)) {
                return false;
            }
            ++left;
        }
        return true;
    }


    /**
     * 给你一个 仅 由字符 'a' 和 'b' 组成的字符串  s 。如果字符串中 每个 'a' 都出现在 每个 'b' 之前，返回 true ；否则，返回 false 。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/check-if-all-as-appears-before-all-bs
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * 输入：s = "aaabbb"
     * 输出：true
     * 解释：
     * 'a' 位于下标 0、1 和 2 ；而 'b' 位于下标 3、4 和 5 。
     * 因此，每个 'a' 都出现在每个 'b' 之前，所以返回 true
     *
     * 输入：s = "aaabbb"
     * 输出：true
     * 解释：
     * 'a' 位于下标 0、1 和 2 ；而 'b' 位于下标 3、4 和 5 。
     * 因此，每个 'a' 都出现在每个 'b' 之前，所以返回 true

     * @param s
     * @return
     */
    public boolean checkString(String s) {
        return s.contains("ba");
    }
}
