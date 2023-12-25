package kr.cosine.cheque;

import org.junit.jupiter.api.Test;

public class CodeTest {

    @Test
    public void switch_test() {
        String input = "테스트2";
        switch (input) {
            case "테스트": {
                System.out.println("1");
                return;
            }
            case "테스트2": {
                System.out.println("2");
                return;
            }
            default: System.out.println("3");
        }
    }
}
