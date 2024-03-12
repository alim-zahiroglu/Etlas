package com.etlas;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NumberPairs {

    public static void main(String[] args) {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        List<int[]> pairs = generatePairs(numbers1, numbers2);

        // Print the pairs
        pairs.forEach(pair -> System.out.println("(" + pair[0] + "," + pair[1] + ")"));
    }

    public static List<int[]> generatePairs(List<Integer> numbers1, List<Integer> numbers2) {
        return numbers1.stream()
                .flatMap(num1 -> numbers2.stream().map(num2 -> new int[]{num1, num2}))
                .collect(Collectors.toList());
    }
}
