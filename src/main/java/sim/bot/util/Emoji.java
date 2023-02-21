package sim.bot.util;

public enum Emoji {
    GREY_QUESTION("\u2754"),
    RED_X("\u274C"),
    ARROW_LEFT("\u2B05\uFE0F"),
    ARROW_RIGHT("\u27A1\uFE0F"),
    THUMBS_UP("\uD83D\uDC4D"),
    ONE_TWO_THREE_FOUR("\uD83D\uDD22"),
    FAST_FORWARD("\u23E9"),
    REWIND("\u23EA"),
    BOWING("\uD83D\uDE47\u200D\u2642\uFE0F"),
    ANGRY("\uD83D\uDE21"),
    WAVE("\uD83D\uDC4B"),
    UNAMUSED("\uD83D\uDE12"),
    PAUSE("\u23F8\uFE0F"),
    RESUME("\u25B6\uFE0F"),
    FREE("\uD83C\uDD93"),
    SHUFFLE("\uD83D\uDD00");
    private final String charcode;

    Emoji(String charcode) {
        this.charcode = charcode;
    }

    public String getCharCode() {
        return charcode;
    }
}