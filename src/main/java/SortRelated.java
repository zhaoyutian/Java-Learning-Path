import java.util.ArrayList;

public class SortRelated {

    /**
     * 插入排序：核心是将数字往前插入到第一个小于其的前一格
     * i指针从头遍历数组，
     * j指针从i指针前一格往前遍历，当j对应数字大于i时，停止遍历，否则遍历直到j==0，
     * 将i指针所在数字插入j指针位置后一格。
     * @param list
     * @return
     */
    public static int[] CharRuSort(int[] list) {
        ArrayList<Integer> array = new ArrayList();

        int i;
        for(i = 0; i < list.length; ++i) {
            array.add(list[i]);
        }

        for(i = 1; i < list.length; ++i) {
            int a = -1;

            int j;
            for(j = i - 1; j >= 0; --j) {
                if ((Integer)array.get(i) > (Integer)array.get(j)) {
                    a = j;
                    break;
                }
            }

            j = (Integer)array.get(i);
            array.remove(i);
            array.add(a + 1, j);
        }
        return array.stream().mapToInt(Integer::valueOf).toArray();
    }

    /**
     * 冒泡排序：
     * 双重循环，第一层循环从头指针开始到队列尾部结束，指针i记录的是已经排好序的部分
     * 第二层循环从尾指针开始，到第一层指针i的前一个结束，指针j记录的是交换比较的数字
     * 具体为，j指针数字与j-1格数字比较，若j-1的数字大，则交换j与j-1的数字的位置
     *
     * 最坏时间复杂度：O(N^2)
     * 空间复杂度：O(1)
     *
     * @param list
     * @return
     */
    public static int[] maoPaoFun(int[] list) {
        for(int i = 0; i < list.length; ++i) {
            for(int j = list.length - 1; j > i; --j) {
                if (list[j - 1] > list[j]) {
                    int temp = list[j];
                    list[j] = list[j - 1];
                    list[j - 1] = temp;
                }
            }
        }
        return list;
    }



    /**
     * 快速排序：采用分治法
     * @param A
     * @return
     */
    public int[] quickSort(int[] A) {
        this.quickSort(A, 0, A.length - 1);
        return A;
    }


    public int[] quickSort(int[] A, int p, int r) {
        if (p < r) {
            int q = this.partition(A, p, r);
            this.quickSort(A, p, q - 1);
            this.quickSort(A, q + 1, r);
        }

        return A;
    }

    /**
     * 把末尾的值当作比较值，把数组分成两列，前列小于等于它，后列大于它。
     * 具体：
     * 从头遍历数组，小于它则与后一位数字互换，分割指针+1；最后把尾指针与分割指针的数字互换。
     * @param A
     * @param p
     * @param r
     * @return
     */
    public int partition(int[] A, int p, int r) {
        int x = A[r];
        int low = p - 1;

        int j;
        for(j = p; j <= r - 1; ++j) {
            if (A[j] <= x) {
                ++low;
                int temp = A[j];
                A[j] = A[low];
                A[low] = temp;
            }
        }

        j = A[r];
        A[r] = A[low + 1];
        A[low + 1] = j;
        return low;
    }


    /**
     * 归并排序
     * @param A
     * @return
     */
    public int[] mergeSort(int[] A) {
        return this.mergeSort(A, 1, A.length);
    }

    public int[] mergeSort(int[] A, int start, int last) {
        if (start < last) {
            int mid = (start + last) / 2;
            this.mergeSort(A, start, mid);
            this.mergeSort(A, mid + 1, last);
            this.merge(A, start, mid, last);
        }

        return A;
    }

    public void merge(int[] A, int start, int mid, int last) {
        int[] L = new int[mid - start + 1];
        int[] R = new int[last - mid];

        int i;
        for(i = 0; i < mid - start + 1; ++i) {
            L[i] = A[start - 1 + i];
        }

        for(i = 0; i < last - mid; ++i) {
            R[i] = A[mid + i];
        }

        i = 0;
        int j = 0;
        int s = start - 1;

        while(i < L.length && j < R.length) {
            if (L[i] < R[j]) {
                A[s] = L[i];
                ++s;
                ++i;
            } else {
                A[s] = R[j];
                ++s;
                ++j;
            }
        }

        while(i < L.length) {
            A[s] = L[i];
            ++s;
            ++i;
        }

        while(j < R.length) {
            A[s] = R[j];
            ++s;
            ++j;
        }

    }
}
