package kr.cosine.cheque.enums;

public enum Notice {
    INPUT_MONEY("§c발행할 금액을 입력해주세요."),
    INPUT_LONG("§c숫자만 입력할 수 있습니다."),
    INPUT_POSITIVE_LONG("§c양의 정수만 입력할 수 있습니다."),
    LACK_MONEY("§c보유 중인 돈이 부족합니다."),
    INVENTORY_FULL("§c인벤토리에 공간이 없습니다."),
    ISSUE_CHEQUE("§a%money%원 수표를 발행하였습니다."),
    USE_CHEQUE("§a%money%원 수표를 사용했습니다.");

    public String message;

    Notice(String message) {
        this.message = message;
    }

    public String replace(String target, String replacement) {
        return message.replace(target, replacement);
    }

    public static Notice getNotice(String noticeText) {
        return Notice.valueOf(noticeText.toUpperCase().replace("-", "_"));
    }
}
