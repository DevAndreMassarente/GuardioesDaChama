package guardiaodaschamas;

// Classe abstrata representando um personagem (Classe Abstrata)
// Demonstra Encapsulamento, Herança, Método Construtor, Método Abstrato e Polimorfismo
public abstract class Personagem {
    // Atributos privados (Encapsulamento)
    private String nome;
    private int pontosVida;
    private int forca;
    private int inteligencia;
    private int destreza;
    private int magia;

    // Método Construtor (Método Construtor)
    public Personagem(String nome, int pontosVida, int forca, int inteligencia, int destreza, int magia) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.forca = forca;
        this.inteligencia = inteligencia;
        this.destreza = destreza;
        this.magia = magia;
    }

    // Métodos Getters e Setters (Encapsulamento)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontosVida() {
        return pontosVida;
    }

    public void setPontosVida(int pontosVida) {
        this.pontosVida = pontosVida;
    }

    public int getForca() {
        return forca;
    }

    public void setForca(int forca) {
        this.forca = forca;
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }

    public int getDestreza() {
        return destreza;
    }

    public void setDestreza(int destreza) {
        this.destreza = destreza;
    }

    public int getMagia() {
        return magia;
    }

    public void setMagia(int magia) {
        this.magia = magia;
    }

    // Método abstrato para atacar (Método Abstrato, Polimorfismo de Classe)
    public abstract void atacar(Personagem alvo);

    // Exibe o status do personagem
    public void exibirStatus() {
        System.out.println("Nome: " + nome);
        System.out.println("Pontos de Vida: " + pontosVida);
        System.out.println("Força: " + forca);
        System.out.println("Inteligência: " + inteligencia);
        System.out.println("Destreza: " + destreza);
        System.out.println("Magia: " + magia);
    }
    
    // [Modificado] Método isBoss adicionado para permitir verificação se o personagem é um boss.
    public boolean isBoss() {
        return false;
    }
}
