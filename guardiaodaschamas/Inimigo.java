package guardiaodaschamas;

// Classe Inimigo que estende Personagem (Herança)
// Demonstra o uso de construtor, encapsulamento e sobrescrita (Polimorfismo)
public class Inimigo extends Personagem {
    private boolean boss; // Indica se é um boss (Encapsulamento)

    // Construtor da classe Inimigo (Método Construtor com super())
    public Inimigo(String nome, int pontosVida, int forca, int inteligencia, int destreza, int magia, boolean boss) {
        super(nome, pontosVida, forca, inteligencia, destreza, magia);
        this.boss = boss;
    }

    // Sobrescrita do método atacar (Polimorfismo de Classe e Sobrescrita)
    @Override
    public void atacar(Personagem alvo) {
        int dano = this.getForca() + (this.getMagia() / 3);
        alvo.setPontosVida(alvo.getPontosVida() - dano);
        System.out.println(this.getNome() + " ataca " + alvo.getNome() + " causando " + dano + " de dano!");
    }
    
    public boolean isBoss() {
        return boss;
    }
}
