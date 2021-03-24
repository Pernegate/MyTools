package algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SplitByJava {
    public static void main(String[] args) throws IOException {
        strCountAndSort();

    }

    public static void strCountAndSort(){
        Scanner in = new Scanner(System.in);
        String inStr = in.nextLine();
        Map<String, Integer> letterMap = new HashMap<>(16);
        List<String> charStore = new ArrayList<>();
        List<String> sortResult = new ArrayList<>();
        for (int i = 0; i < inStr.length(); i++) {
            String tmpStr = inStr.charAt(i) + "";
            int tmpNum = 0;
            if (letterMap.containsKey(tmpStr)) {
                tmpNum = letterMap.get(tmpStr);
            } else {
                charStore.add(tmpStr);
            }
            tmpNum++;
            letterMap.put(tmpStr, tmpNum);
        }
        charStore.forEach(item -> {
            getIndex(sortResult, item, letterMap);
        });
        sortResult.forEach(item -> {
            System.out.print(item + ":" + letterMap.get(item) + ";");
        });
    }

    public static void getIndex(List<String> sortResult, String newStr, Map<String, Integer> letterMap) {
        int index = 0;
//        String tmpSaveStr = "";
        for (int i = 0; i < sortResult.size(); i++) {
            String tmpStr = sortResult.get(i);
            int newStrCount = letterMap.get(newStr);
            int tmpStrCount = letterMap.get(tmpStr);
            boolean isSmaller = (newStrCount < tmpStrCount) ||
                    ((newStrCount == tmpStrCount) && (newStr.compareTo(tmpStr) > 0));
            if (isSmaller) {
                index++;
            } else {
//                tmpSaveStr = tmpStr;
                break;
            }
        }
        sortResult.add(index, newStr);
    }

    public static void fiveFu() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            int count2 = Integer.parseInt(str);
            int[] count = new int[5];
            int times = 0;
            for (int j = 0; j < count2; j++) {
                str = bufferedReader.readLine();
                for (int i = 0; i < str.length(); i++) {
                    if (times >= 5) {
                        times = 0;
                    }
                    char c = str.charAt(i);
                    if (c == '1') {
                        count[times++]++;
                    }
                }
            }
            int min = count[0];
            for (int num : count) {
                if (num < min) {
                    min = num;
                }
            }
            System.out.println(min);
        }
    }

    public static void splitByJava() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = bufferedReader.readLine()) != null) {
            int count = Integer.parseInt(input);
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < count; i++) {
                input = bufferedReader.readLine();

                int start = 0;
                int end = 8;

                int length = input.length() / 8;
                if (input.length() % 8 > 0) {
                    ++length;
                }

                for (int j = 0; j < length; j++) {
                    end = end > input.length() ? input.length() : end;
                    String current = input.substring(start, end);
                    result.append(current);
                    if (current.length() < 8) {
                        for (int k = 0; k < 8 - current.length(); k++) {
                            result.append("0");
                        }
                    }
                    result.append("\n");
                    start += 8;
                    end += 8;
                }
            }
            System.out.println(result.toString().trim());
        }
    }
}
