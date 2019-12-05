package com.rabbitmq.test;

/**
 * Created by star on 2019/12/2.
 */
public class BinaryInserSort {
    public static void main(String []args){
        int [] arr ={1,6,8,2,3,7,5,0};
        binayInsertSort(arr);
        for(int i=0; i<arr.length;i++){
            System.out.print(arr[i]);
        }
    }

    public static void binayInsertSort(int []arr){
        int n = arr.length;
        int i,j;
        for(i =1; i<n; i++){
            int tmp = arr[i];
            int low =0;
            int high = i-1;
            while(low <=high){
                int mid = (low+high)/2;
                if (arr[mid]>tmp) {
                    high = mid -1;
                }else{
                    low = mid+1;
                }
            }
            for(j=i-1;j>=low;j--){
                arr[j+1] =arr[j];
            }
            arr[low] = tmp;
        }
    }



}
