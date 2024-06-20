package analise_sintactica;

import java.util.List;

import analise_lexica.Token;
import analise_lexica.TokenType;

public class Gramatica {
    private List<Token> tokens;
    private int currentTokenIndex;
    
    Gramatica(List<Token> tokens){
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }
    
	 private Token currentToken() {
	        return tokens.get(currentTokenIndex);
	    }
	 
	void parseProgram() {
        while (currentToken().getType() != TokenType.EOF) {
            if (isType(currentToken())) {
                parseDEC();
                consume(TokenType.SEMICOLON);
            } else {
                parseINIT();
            }
        }
    }
	 void parseINIT() {
        if (currentToken().getType() == TokenType.ID) {
            consume(TokenType.ID);
            consume(TokenType.ASSIGN);
            if (currentToken().getType() == TokenType.PR_NEW) {
                consume(TokenType.PR_NEW);
                parseTD();
                parseLCN();
                consume(TokenType.SEMICOLON);
            } else if (currentToken().getType() == TokenType.LBRACE) {
                consume(TokenType.LBRACE);
                parseLV();
                consume(TokenType.RBRACE);
                consume(TokenType.SEMICOLON);
            } else {
                throw new IllegalArgumentException("Expected 'new' or '{' but found " + currentToken().getType());
            }
        } else if (isType(currentToken())) {
            parseDEC();
            consume(TokenType.ASSIGN);
            if (currentToken().getType() == TokenType.PR_NEW) {
                consume(TokenType.PR_NEW);
                parseTD();
                parseLCN();
                consume(TokenType.SEMICOLON);
            } else if (currentToken().getType() == TokenType.LBRACE) {
                consume(TokenType.LBRACE);
                parseLV();
                consume(TokenType.RBRACE);
                consume(TokenType.SEMICOLON);
            } else {
                throw new IllegalArgumentException("Expected 'new' or '{' but found " + currentToken().getType());
            }
        } else {
            throw new IllegalArgumentException("Expected ID or type declaration but found " + currentToken().getType());
        }
    }

     void parseDEC() {
        parseTD();
        parseLC();
        parseLID();
    }

     void parseTD() {
        if (isType(currentToken())) {
            consume(currentToken().getType());
        } else {
            throw new IllegalArgumentException("Expected type but found " + currentToken().getType());
        }
    }

    private void parseLC() {
        while (currentToken().getType() == TokenType.LBRACKET) {
            consume(TokenType.LBRACKET);
            consume(TokenType.RBRACKET);
        }
    }

     void parseLCN() {
        while (currentToken().getType() == TokenType.LBRACKET) {
            consume(TokenType.LBRACKET);
            consume(TokenType.NUMERO);
            consume(TokenType.RBRACKET);
        }
    }

    private void parseLID() {
        consume(TokenType.ID);
        while (currentToken().getType() == TokenType.COMMA) {
            consume(TokenType.COMMA);
            consume(TokenType.ID);
        }
    }

     void parseLV() {
        if (currentToken().getType() == TokenType.ID || currentToken().getType() == TokenType.NUMERO) {
            consume(currentToken().getType());
            while (currentToken().getType() == TokenType.COMMA) {
                consume(TokenType.COMMA);
                consume(currentToken().getType());
            }
        } else {
            throw new IllegalArgumentException("Expected ID or NUMBER but found " + currentToken().getType());
        }
    }

    boolean isType(Token token) {
        switch (token.getType()) {
            case INT: case CHAR: case BYTE: case BOOLEAN: case LONG: case SHORT: case FLOAT: case DOUBLE:
                return true;
            default:
                return false;
        }
    }
    
    void consume(TokenType type) {
        if (currentToken().getType() == type) {
            currentTokenIndex++;
        } else {
            throw new IllegalArgumentException("Expected token of type " + type + " but found " + currentToken().getType());
        }
    }
}
