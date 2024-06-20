package analise_sintactica;

import analise_lexica.Token;
import analise_lexica.TokenType;
import analise_semantica.AnalisadorSemantico;

import java.util.List;

public class Analisador_Sintactico {
    private List<Token> tokens;
    private int currentTokenIndex;
    private TabelaSimbolos tabelaSimbolos;
    private AnalisadorSemantico analisadorSemantico;

    public Analisador_Sintactico(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.tabelaSimbolos = new TabelaSimbolos();
        this.analisadorSemantico = new AnalisadorSemantico(tabelaSimbolos);
    }

    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private void consume(TokenType type) {
        if (currentToken().getType() == type) {
            currentTokenIndex++;
        } else {
            throw new IllegalArgumentException("Erro Sintactico na linha " + currentToken().getLine() + ": Esperado token do tipo " + type + " mas encontrado " + currentToken().getType());
        }
    }

    public void parse() {
        parseProgram();
        consume(TokenType.EOF);
        analisadorSemantico.analisar(tokens);
    }

    private void parseProgram() {
        while (currentToken().getType() != TokenType.EOF) {
            if (isType(currentToken())) {
                parseDEC();
            } else if (currentToken().getType() == TokenType.ID) {
                parseINIT();
            } else {
                throw new IllegalArgumentException("Erro Sintactico na linha " + currentToken().getLine() + ": Esperado tipo ou ID mas encontrado " + currentToken().getType());
            }
        }
    }

    private void parseINIT() {
        if (currentToken().getType() == TokenType.ID) {
            String id = currentToken().getText();
            if (!tabelaSimbolos.verificarExistencia(id)) {
                throw new IllegalArgumentException("Erro Sintactico na linha " + currentToken().getLine() + ": Variavel nao declarada: " + id);
            }
            consume(TokenType.ID);
            consume(TokenType.ASSIGN);
            Simbolo simbolo = tabelaSimbolos.obterEntrada(id);
            if (currentToken().getType() == TokenType.PR_NEW) {
                consume(TokenType.PR_NEW);
                Tipo tipo = parseTD();
                int dimensao = parseLCN();
                simbolo.setInicializado(true);
                simbolo.setTipo(tipo);
                simbolo.setDimensao(dimensao);
            } else if (currentToken().getType() == TokenType.LBRACE) {
                consume(TokenType.LBRACE);
                parseLV();
                consume(TokenType.RBRACE);
                simbolo.setInicializado(true);
            } else {
                throw new IllegalArgumentException("Erro Sintactico na linha " + currentToken().getLine() + ": Esperado 'new' ou '{' mas encontrado " + currentToken().getType());
            }
            consume(TokenType.SEMICOLON);
        } else {
            throw new IllegalArgumentException("Erro Sintactico na linha " + currentToken().getLine() + ": Esperado ID mas encontrado " + currentToken().getType());
        }
    }

    private void parseDEC() {
        Tipo tipo = parseTD();
        int dimensao = parseLC();
        parseLID(tipo, dimensao);
        if (currentToken().getType() == TokenType.ASSIGN) {
            consume(TokenType.ASSIGN);
            if (currentToken().getType() == TokenType.PR_NEW) {
                consume(TokenType.PR_NEW);
                parseTD();
                parseLCN();
            } else if (currentToken().getType() == TokenType.LBRACE) {
                consume(TokenType.LBRACE);
                parseLV();
                consume(TokenType.RBRACE);
            } else {
                throw new IllegalArgumentException("Erro Sintactico na linha " + currentToken().getLine() + ": Esperado 'new' ou '{' mas encontrado " + currentToken().getType());
            }
        }
        consume(TokenType.SEMICOLON);
    }

    private Tipo parseTD() {
        Tipo tipo;
        switch (currentToken().getType()) {
            case INT: tipo = Tipo.INT; break;
            case CHAR: tipo = Tipo.CHAR; break;
            case BYTE: tipo = Tipo.BYTE; break;
            case BOOLEAN: tipo = Tipo.BOOLEAN; break;
            case LONG: tipo = Tipo.LONG; break;
            case SHORT: tipo = Tipo.SHORT; break;
            case FLOAT: tipo = Tipo.FLOAT; break;
            case DOUBLE: tipo = Tipo.DOUBLE; break;
            default:
                throw new IllegalArgumentException("Erro Sintactico na linha " + currentToken().getLine() + ": Esperado tipo mas encontrado " + currentToken().getType());
        }
        consume(currentToken().getType());
        return tipo;
    }

    private int parseLC() {
        int dimensao = 0;
        while (currentToken().getType() == TokenType.LBRACKET) {
            consume(TokenType.LBRACKET);
            consume(TokenType.RBRACKET);
            dimensao++;
        }
        return dimensao;
    }

    private int parseLCN() {
        int dimensao = 0;
        while (currentToken().getType() == TokenType.LBRACKET) {
            consume(TokenType.LBRACKET);
            consume(TokenType.NUMERO);
            consume(TokenType.RBRACKET);
            dimensao++;
        }
        return dimensao;
    }

    private void parseLID(Tipo tipo, int dimensao) {
        String id = currentToken().getText();
        tabelaSimbolos.adicionarSimbolo(id, Categoria.VARIAVEL, tipo, dimensao);
        consume(TokenType.ID);
        while (currentToken().getType() == TokenType.COMMA) {
            consume(TokenType.COMMA);
            id = currentToken().getText();
            tabelaSimbolos.adicionarSimbolo(id, Categoria.VARIAVEL, tipo, dimensao);
            consume(TokenType.ID);
        }
    }

    private void parseLV() {
        if (currentToken().getType() == TokenType.ID || currentToken().getType() == TokenType.NUMERO) {
            consume(currentToken().getType());
            while (currentToken().getType() == TokenType.COMMA) {
                consume(TokenType.COMMA);
                consume(currentToken().getType());
            }
        } else {
            throw new IllegalArgumentException("Erro Sintactico na linha " + currentToken().getLine() + ": Esperado ID ou NUMBER mas encontrado " + currentToken().getType());
        }
    }
    
    private boolean isType(Token token) {
        switch (token.getType()) {
            case INT: case CHAR: case BYTE: case BOOLEAN: case LONG: case SHORT: case FLOAT: case DOUBLE:
                return true;
            default:
                return false;
        }
    }

    public TabelaSimbolos getTabelaSimbolos() {
        return tabelaSimbolos;
    }
}
