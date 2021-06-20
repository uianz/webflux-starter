package com.uianz;



import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author uianz
 * @date 2021/6/18
 */
public class ListTest {

    public static void main(String[] args) {
        var l1 = List.of(1, 2, 3);
        var l2 = List.of("4","5","6");
        var l3 = List.of(true,false);
        var allL = Stream.of(l1, l2,l3)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println(allL);
    }

}
