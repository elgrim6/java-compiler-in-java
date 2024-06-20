package analise_sintactica;

public class Simbolo {
	 private String token;  // Name of the identifier
	    private Categoria categoria;  // Category of the identifier
	    private Tipo tipo;  // Data type (considering only primitive types)
	    private boolean array;  // Whether it's an array or not
	    private int dimensao;  // Dimension (if it's an array)
	    private boolean inicializado;

	    public Simbolo(String token, Categoria categoria, Tipo tipo, int dimensao) {
	        this.token = token;
	        this.categoria = categoria;
	        this.tipo = tipo;
	        this.dimensao = dimensao;
	        this.array = dimensao > 0;
	        this.inicializado = false;
	    }

	    public String getToken() {
	        return token;
	    }

	    public void setToken(String token) {
	        this.token = token;
	    }

	    public Categoria getCategoria() {
	        return categoria;
	    }

	    public void setCategoria(Categoria categoria) {
	        this.categoria = categoria;
	    }

	    public Tipo getTipo() {
	        return tipo;
	    }

	    public void setTipo(Tipo tipo) {
	        this.tipo = tipo;
	    }

	    public boolean isArray() {
	        return array;
	    }

	    public void setArray(boolean array) {
	        this.array = array;
	    }

	    public int getDimensao() {
	        return dimensao;
	    }

	    public void setDimensao(int dimensao) {
	        this.dimensao = dimensao;
	        this.array = dimensao > 0;
	    }

	    @Override
	    public String toString() {
	        return String.format("Simbolo(token=%s, categoria=%s, tipo=%s, array=%s, dimensao=%d)", token, categoria, tipo, array, dimensao);
	    }

	    public boolean isInicializado() {
	        return inicializado; // Getter for inicializado
	    }

	    public void setInicializado(boolean inicializado) {
	        this.inicializado = inicializado; // Setter for inicializado
	    }
}
