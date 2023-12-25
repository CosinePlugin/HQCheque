package kr.cosine.cheque.enums;

public enum Parse {
    DISPLAY,
    LORE;

    public static Parse getParse(String parserText) {
        return Parse.valueOf(parserText.toUpperCase());
    }
}
