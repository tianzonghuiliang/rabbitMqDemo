package com.rabbitmq.test;

/**
 * Created by star on 2019/12/2.
 */
public class BinaryInserSort1 {
    public static void main(String []args){
//        int [] arr ={1,6,8,2,3,7,5,0};
//        System.out.println("-----1--------");
//        binayInsertSort(arr);
//        System.out.println("-------------");
//        for(int i=0; i<arr.length;i++){
//            System.out.print(arr[i]);
//        }

        int [][] arr ={{11,1,3,5},{20,21,8,9},{58,14,16,2}};
        System.out.print(find(arr,1));
    }

    public static void binayInsertSort(int []arr){
        int size = arr.length;
        int i,j;
        for(i =1;i<size;i++){
            int tmp = arr[i];
            int low = 0;
            int high = i-1;
            while(low<=high){
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
           arr[j+1] =tmp;
        }
    }



//    public static boolean find(int[][] array,int target) {
//        if (array == null) {
//            return false;
//        }
//        int row = 0;
//        int column = array[0].length-1;
//
//        while (row < array.length && column >= 0) {
//            if(array[row][column] == target) {
//                return true;
//            }
//            if(array[row][column] > target) {
//                column--;
//            } else {
//                row++;
//            }
//        }
//        return false;
//    }

    public static boolean find(int [][]arr,int target){
        if(arr==null){
            return false;
        }
        int low = 0;
        int col = arr[0].length-1;
        while(low<arr.length&& col>=0){
            if(arr[low][col]==target){
                return true;
            }
            if(arr[low][col]>target){
                col--;
            }else{
                low++;
            }
        }
        return false;
    }



}
