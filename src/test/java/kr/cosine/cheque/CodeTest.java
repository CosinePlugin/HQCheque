package kr.cosine.cheque;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Test
    public void version_test() {
        String version = "v1_12_R3";

        // Define the pattern to capture only v1_12
        Pattern pattern = Pattern.compile("v\\d+_(\\d+)_R\\d+");

        // Match and extract the version
        Matcher matcher = pattern.matcher(version);
        if (matcher.find()) {
            String extractedVersion = matcher.group(1);
            System.out.println("Extracted version: " + extractedVersion);
        }
    }
}
