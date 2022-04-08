package cn.enilu.flash.api;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        int n = 5;
        int k = 555;
        StringBuilder sb = new StringBuilder();
        int[] factorials = new int[n+1];//阶乘数
        List<Integer> candidates = new ArrayList<>();//剩余待选择的数
        factorials[0]=1;
        int d = 1;
        for(int i=1; i<=n; i++){
            candidates.add(i);
            d *= i;
            factorials[i]= d;
        }
        k--;
        for(int j=n-1; j>=0; j--){
            int index = k / factorials[j];
            sb.append(candidates.remove(index));
            k -= index*factorials[j];
        }
        System.out.println(sb.toString());
    }
}
