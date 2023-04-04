package com.mark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CamelToSnakeUtil {

    public static boolean checkCamelCase(String camelCaseString) {
        char[] arr = camelCaseString.toCharArray();

        for(char c: arr ){
            if(isUppercase(c)){
                return true;
            }
        }
        return false;
    }
    public static String changeCamelCaseToSnakeCase(String camelCaseString){
        char[] arr = camelCaseString.toCharArray();
        StringBuilder builder = new StringBuilder();
        for(int i = 0 ; i < arr.length ; i++){
            boolean isUppercase = false;
            if(isUppercase(arr[i])){
                if(i != 0) {
                    builder.append('_');
                }
                isUppercase = true;
            }
            char nextChar = arr[i];
            if(isUppercase) {
                 nextChar= (char) (arr[i] + 32);
            }
            builder.append(nextChar);
        }
        return builder.toString();
    }
    public static boolean isUppercase(char c){
        return c >= 65 && c < 90;
    }
}
