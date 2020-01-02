package io.github.fdj32.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class BeautifyOutputUtil {

    public static void printTable(List<String[]> list) {
        if (null == list || list.isEmpty())
            return;
        int size = list.get(0).length;
        int[] maxLengthArray = new int[size];
        for (String[] array : list) {
            for (int i = 0; i < size; i++) {
                if (maxLengthArray[i] < array[i].length()) {
                    maxLengthArray[i] = array[i].length();
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String[] array : list) {
            for (int i = 0; i < size; i++) {
                sb.append(StringUtils.rightPad(array[i], maxLengthArray[i] + 2, ' '));
            }
            sb.append(System.lineSeparator());
        }
        System.out.println(sb.toString());
    }
}
