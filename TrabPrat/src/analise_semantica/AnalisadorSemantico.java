package analise_semantica;

import analise_sintactica.Simbolo;
import analise_sintactica.TabelaSimbolos;
import analise_lexica.Token;
import analise_lexica.TokenType;

import java.util.List;

public class AnalisadorSemantico {
    private TabelaSimbolos tabelaSimbolos;

    public AnalisadorSemantico(TabelaSimbolos tabelaSimbolos) {
        this.tabelaSimbolos = tabelaSimbolos;
    }

    public void analisar(List<Token> tokens) {
        for (Token token : tokens) {
            if (token.getType() == TokenType.ID) {
                String id = token.getText();
                if (tabelaSimbolos.verificarExistencia(id)) {
                    Simbolo simbolo = tabelaSimbolos.obterEntrada(id);
                    verificarInicializacao(simbolo, token.getLine());
                } else {
                    throw new IllegalArgumentException("Erro Semantico na linha " + token.getLine() + ": Variavel nao declarada " + id);
                }
            }
        }
    }

    private void verificarInicializacao(Simbolo simbolo, int linha) {
        if (simbolo.getDimensao() > 0 && simbolo.isInicializado()) {
            throw new IllegalArgumentException("Erro Semantico na linha " + linha + ": Array " + simbolo.getToken() + " nao inicializado");
        }
    }

    public void verificarAcessoArray(Simbolo simbolo, int indice, int linha) {
        if (simbolo.getDimensao() == 0) {
            throw new IllegalArgumentException("Erro Semantico na linha " + linha + ": Variavel " + simbolo.getToken() + " nao e um array");
        }
        // Add more checks here, such as bounds checking
    }

    public void verificarAtribuicaoArray(Simbolo simbolo, int[] indices, int linha) {
        if (indices.length != simbolo.getDimensao()) {
            throw new IllegalArgumentException("Erro Semantico na linha " + linha + ": Dimensao do array " + simbolo.getToken() + " incorreta");
        }
    }
}
