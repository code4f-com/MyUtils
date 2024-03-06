/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.atest;

import com.tuanpla.utils.json.GsonUtil;
import java.util.List;

/**
 *
 * @author tuanp
 */
public class NewClass {

    public static void main(String[] args) {
        String s = "[{\"name\": \"ádas\", \"summary\": \"đâsd\"}]";
        List<GroupBody> arr = GsonUtil.toArrayList(s, GroupBody.class);
        System.out.println(arr.toString());
        for (GroupBody one : arr) {
            System.out.println(one);
        }
    }
}
