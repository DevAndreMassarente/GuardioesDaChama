package guardiaodaschamas;

// Classe Poder que representa um poder/ataque do personagem
// Demonstra Encapsulamento, Método Construtor e Modularização
public class Poder {
    private String nome;
    private int dano;
    private int ppAtual;
    private int ppMax;
    private String efeito; // Pode ser "congelar", "aumentar", "nenhum", etc.
    
    // Campos para XP e nível do poder
    private int xp;
    private int nivel;
    
    // Construtor da classe Poder (Método Construtor)
    public Poder(String nome, int dano, int ppMax, String efeito) {
        this.nome = nome;
        this.dano = dano;
        this.ppMax = ppMax;
        this.ppAtual = ppMax;
        this.efeito = efeito;
        this.xp = 0;
        this.nivel = 1;
    }
    
    // Getters e Setters para os atributos de Poder (Encapsulamento)
    
    public String getNome() {
        return nome;
    }
    
    public int getDano() {
        return dano;
    }
    
    public int getPpAtual() {
        return ppAtual;
    }
    
    public void setPpAtual(int ppAtual) {  // Novo método setter para PP Atual
        this.ppAtual = ppAtual;
    }
    
    public int getPpMax() {
        return ppMax;
    }
    
    public String getEfeito() {
        return efeito;
    }
    
    // Métodos getters e setters para XP e Nível
    public int getXp() {
        return xp;
    }
    
    public void setXp(int xp) {
        this.xp = xp;
    }
    
    public int getNivel() {
        return nivel;
    }
    
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
    // Tenta utilizar o poder decrementando o PP (Encapsulamento)
    public boolean usar() {
        if (ppAtual > 0) {
            ppAtual--;
            return true;
        }
        return false;
    }
    
    // Recarrega os PP para o valor máximo
    public void recarregar() {
        ppAtual = ppMax;
    }
    
    // Adiciona XP ao poder, com mecanismo de level up a cada 50 XP
    public void ganharXP(int quantidade) {
        this.xp += quantidade;
        while (this.xp >= xpParaLevelUp()) {
            this.xp -= xpParaLevelUp();
            levelUp();
        }
    }
    
    // Calcula o XP necessário para subir de nível
    private int xpParaLevelUp() {
        return 50;
    }
    
    // Realiza o level up, incrementando o nível e aumentando o dano
    private void levelUp() {
        this.nivel++;
        this.dano++;
        System.out.println("Seu poder " + nome + " subiu para o nível " + nivel + "!");
    }
    
    // Sobrescrita do método toString() para exibir as informações básicas do poder (Encapsulamento)
    @Override
    public String toString() {
        return nome + " (Dano: " + dano + ", PP: " + ppAtual + "/" + ppMax + ", Nível: " + nivel + ")";
    }
}
