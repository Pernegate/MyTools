package algorithm;

import java.util.ArrayList;
import java.util.List;

public class HuaWei {
//substring(begin,end)不包括end

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
    }

    /**
     * 排序
     */
    public int[] MySort (int[] arr) {
        // write code here
        List<Integer> arrList = new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            arrList.add(arr[i]);
        }
        arrList.sort(Integer::compareTo);
        int[] newArr = new int[arr.length];
        for(int i=0;i<arrList.size();i++){
            newArr[i] = arrList.get(i);
        }
        return newArr;
    }

    /**
     * 报数游戏
     */
    public static List<Integer> countNums(int m) {
        List<Integer> remainPeople = new ArrayList<>();
        List<Integer> outPeoPle = new ArrayList<>();
        for (int i = 1; i < m + 1; i++) {
            remainPeople.add(i);
        }
        int index = 0;
        while (remainPeople.size() > 0) {
            index += 4;
            if (index > remainPeople.size() - 1) {
                index = index % remainPeople.size();
            }
            outPeoPle.add(remainPeople.get(index));
            remainPeople.remove(index);
        }
        return outPeoPle;
    }

    /**
     * 预定机票
     *
     * @param bookings：
     * @param n：
     * @return int[]
     */
    public static int[] corpFlightBookings(int[][] bookings, int n) {
        int[] answer = new int[n];
        // dif[i]=answer[i+1]-answer[i]
        for (int[] b : bookings) {
            answer[b[0] - 1] += b[2];
            if (b[1] < n) {
                answer[b[1]] -= b[2];
            }
        }
        for (int i = 1; i < answer.length; i++) {
            answer[i] += answer[i - 1];
        }
        return answer;
    }

    /**
     * 出租车
     *
     * @param trips：
     * @param capacity：
     * @return boolean
     */
    public static boolean carPooling(int[][] trips, int capacity) {
        int max = 0;
        boolean isAble = true;
        for (int[] t : trips) {
            max = (max > t[2]) ? max : t[2];
        }
        int[] numLine = new int[max];
        for (int[] t : trips) {
            numLine[t[1] - 1] += t[0];
            numLine[t[2] - 1] -= t[0];
        }
        int numNow = 0;
        for (int n : numLine) {
            numNow += n;
            if (numNow > capacity) {
                isAble = false;
                break;
            }
        }
        return isAble;
    }
}
