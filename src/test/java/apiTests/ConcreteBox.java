package apiTests;

import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ConcreteBox extends AbstractBox {
    static AbstractBox box = new ConcreteBox();

    @Override
    public LocalDate getDate(String date) {
        System.out.println("ConcreteBox getDate method called with date: " + date);
        box.getDate("2023-10-01");
        return LocalDate.now();
    }

    public TreeSet<String> allTest() {
        return new TreeSet<>(Set.of("test2", "test1", "test3"));
    }

    @Test
    public void testAllTest() {
        TreeSet<String> stringTreeSet = allTest();
        if (!stringTreeSet.isEmpty()) {
            System.out.println("TreeSet is empty");
            SortedSet<String> sortedSet = stringTreeSet.subSet("test1", "test3");
            System.out.println("SortedSet: " + sortedSet);

        } else {
            System.out.println("TreeSet is not empty");
        }
    }
}
