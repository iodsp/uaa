package org.iodsp.uaa;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        int aa = 111__1111;
        System.out.println(aa);
        String[] src = {"aa", "nn"};
        String[] desc = new String[10];
        System.arraycopy(src, 0, desc, 0, src.length);

        for (int i = 0; i < desc.length; i++) {
            System.out.println(desc[i]);
            Arrays.binarySearch(src, "aa");
        }
    }
}
