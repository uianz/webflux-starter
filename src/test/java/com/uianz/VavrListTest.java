package com.uianz;

import io.vavr.collection.List;

/**
 * @author uianz
 * @date 2021/6/18
 */
public class VavrListTest {

    public static void main(String[] args) {
        var l1 = List.of(1, 2, 3);
        var l2 = List.of("4", "5", "6");
        var allList = List.empty().appendAll(l1).appendAll(l2);
        System.out.println(allList);
    }

}
