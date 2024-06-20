package analise_lexica;

public class Token {
    private TokenType type;
    private String text;
    private int line;

    public Token(TokenType type, String text, int line) {
        this.type = type;
        this.text = text;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getLine() {
		return line;
	}

	@Override
    public String toString() {
        return String.format("Token(type=%s, text='%s', line=%d)", type, text, line);
    }
}
