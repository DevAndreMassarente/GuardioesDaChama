package guardiaodaschamas;

// Classe representando um item (Encapsulamento)
// Demonstra uso de atributos, construtor e método toString()
public class Item {
    private String nome;
    private String tipo; // Por exemplo: "arma", "armadura", "poção", etc.
    private int bonusForca;
    private int bonusMagia;
    private String descricao;  // Nova propriedade

    // Construtor da classe Item (Método Construtor) com descrição
    public Item(String nome, String tipo, int bonusForca, int bonusMagia, String descricao) {
        this.nome = nome;
        this.tipo = tipo;
        this.bonusForca = bonusForca;
        this.bonusMagia = bonusMagia;
        this.descricao = descricao;
    }
    
    // Sobrecarga de construtor: se não for fornecida uma descrição, gera uma padrão
    public Item(String nome, String tipo, int bonusForca, int bonusMagia) {
        this(nome, tipo, bonusForca, bonusMagia, gerarDescricaoPadrao(nome, tipo, bonusForca, bonusMagia));
    }
    
    // Método auxiliar para gerar uma descrição padrão com base no tipo
    private static String gerarDescricaoPadrao(String nome, String tipo, int bonusForca, int bonusMagia) {
        if (tipo.equalsIgnoreCase("poção")) {
            return "Restaura " + bonusMagia + " pontos de vida.";
        }
        // Outras regras de descrição podem ser adicionadas aqui para outros tipos.
        return "";
    }

    // Getters (Encapsulamento)
    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getBonusForca() {
        return bonusForca;
    }

    public int getBonusMagia() {
        return bonusMagia;
    }
    
    public String getDescricao() {
        return descricao;
    }

    // Método toString() para exibir as informações essenciais do item, incluindo a descrição.
    @Override
    public String toString() {
        return nome + " (" + tipo + "): " + descricao;
    }
}
