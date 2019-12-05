package com.rabbitmq.test;

import java.util.Arrays;

/**
 * Created by star on 2019/11/29.
 */
public class RealInsertSort {
    public static void main(String [] args){
          int []arr ={8,2,6,2,1,7};
          sort(arr);
        for(int i =0; i<arr.length; i++){
            System.out.println(arr[i]);
        }
    }


    public static void sort(int [] arr){
        int size = arr.length;
        for(int i = 1; i<size; i++) {
            int tmp = arr[i];
            int j = i - 1;
            while (j >= 0 && tmp < arr[j]) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = tmp;
        }
    }
}
