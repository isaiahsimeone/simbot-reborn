package sim.bot.util;

public enum Emoji {
    GREY_QUESTION("❔"),
    RED_X("❌"),
    ARROW_LEFT("⬅️"),
    ARROW_RIGHT("➡️"),
    THUMBS_UP("\uD83D\uDC4D"),
    ONE_TWO_THREE_FOUR("\uD83D\uDD22"),
    FAST_FORWARD("⏩"),
    REWIND("⏪"),
    BOWING("\uD83D\uDE47\u200D♂️"),
    ANGRY("\uD83D\uDE21"),
    WAVE("\uD83D\uDC4B"),
    UNAMUSED("\uD83D\uDE12"),
    FREE("\uD83C\uDD93");

    public final String charcode;

    Emoji(String charcode) {
        this.charcode = charcode;
    }

    public String get_char_code() {
        return charcode;
    }
}