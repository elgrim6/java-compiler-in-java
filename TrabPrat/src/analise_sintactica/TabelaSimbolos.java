package analise_sintactica;

import java.util.HashMap;

public class TabelaSimbolos {
    private HashMap<String, Simbolo> tabela;

    public TabelaSimbolos() {
        this.tabela = new HashMap<>();
    }

    public void adicionarSimbolo(String token, Categoria categoria, Tipo tipo, int dimensao) {
        tabela.put(token, new Simbolo(token, categoria, tipo, dimensao));
    }

    public boolean verificarExistencia(String nome) {
        return tabela.containsKey(nome);
    }

    public Simbolo obterEntrada(String nome) {
        return tabela.get(nome);
    }

    public HashMap<String, Simbolo> getTabela() {
        return tabela;
    }
}

enum Categoria {
    VARIAVEL,
    CONSTANTE
}

enum Tipo {
    INT,
    CHAR,
    BOOLEAN,
    FLOAT,
    LONG,
    SHORT,
    BYTE,
    DOUBLE
}
