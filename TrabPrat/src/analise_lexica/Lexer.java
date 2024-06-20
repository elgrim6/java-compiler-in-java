package analise_lexica;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String input;
    private int position;
    private List<Token> tokens;
    private int line;

    private static final Pattern PATTERN = Pattern.compile(
        "\\s*(?:(int|char|byte|boolean|long|short|float|double)|" +
        "(\\[)|(\\])|(\\{)|(\\})|(,)|(;)|(=)|(new)|([a-zA-Z_][a-zA-Z0-9_]*)|([0-9]+))");

    public Lexer(String input, int line) {
        this.input = input;
        this.position = 0;
        this.tokens = new ArrayList<>();
        this.line = line;
    }

    public List<Token> tokenize() {
        Matcher matcher = PATTERN.matcher(input);
        while (matcher.find()) {
            String tokenText = matcher.group().trim();
            if (tokenText.isEmpty()) {
                continue;
            }

            if (matcher.group(1) != null) {
                tokens.add(new Token(TokenType.valueOf(matcher.group(1).toUpperCase()), matcher.group(1), line));
            } else if (matcher.group(2) != null) {
                tokens.add(new Token(TokenType.LBRACKET, "[", line));
            } else if (matcher.group(3) != null) {
                tokens.add(new Token(TokenType.RBRACKET, "]", line));
            } else if (matcher.group(4) != null) {
                tokens.add(new Token(TokenType.LBRACE, "{", line));
            } else if (matcher.group(5) != null) {
                tokens.add(new Token(TokenType.RBRACE, "}", line));
            } else if (matcher.group(6) != null) {
                tokens.add(new Token(TokenType.COMMA, ",", line));
            } else if (matcher.group(7) != null) {
                tokens.add(new Token(TokenType.SEMICOLON, ";", line));
                line++;
            } else if (matcher.group(8) != null) {
                tokens.add(new Token(TokenType.ASSIGN, "=", line));
            } else if (matcher.group(9) != null) {
                tokens.add(new Token(TokenType.PR_NEW, "new", line));
            } else if (matcher.group(10) != null) {
                tokens.add(new Token(TokenType.ID, matcher.group(10), line));
            } else if (matcher.group(11) != null) {
                tokens.add(new Token(TokenType.NUMERO, matcher.group(11), line));
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    public static void main(String[] args) {
        String code = "int x = new int[10]; int a;";
        Lexer lexer = new Lexer(code, 1);
        List<Token> tokens = lexer.tokenize();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
