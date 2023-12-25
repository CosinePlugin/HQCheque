package kr.cosine.cheque.util;

import java.text.DecimalFormat;

public class FormatUtils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");

    public static String format(double value) {
        return decimalFormat.format(value);
    }
}
