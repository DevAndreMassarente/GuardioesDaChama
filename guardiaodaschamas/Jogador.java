package guardiaodaschamas;

import java.util.ArrayList;

/* 
   Classe Jogador 
   - Demonstra Herança (estende Personagem)
   - Aplica Encapsulamento (atributos privados com getters/setters)
   - Utiliza Método Construtor para inicialização
   - Implementa evolução por XP (metodologia de level up com aumento progressivo do requisito de XP)
*/
public class Jogador extends Personagem {
    private int experiencia; // XP total do jogador (Encapsulamento)
    private int nivel;       // Nível do jogador (Encapsulamento)
    private int poderesDesbloqueados;
    private ArrayList<Poder> poderes;
    private ArrayList<Item> inventario;

    // Método Construtor
    public Jogador(String nome) {
        // Inicializa com 50 de HP para nível 1
        super(nome, 50, 1, 1, 1, 1); // Herança: inicializa atributos da classe Personagem
        this.experiencia = 0;
        this.nivel = 1;
        this.poderesDesbloqueados = 0;
        poderes = new ArrayList<>();
        inventario = new ArrayList<>();
        // Poder básico com PP máximo de 20, dano fixo de 5
        poderes.add(new Poder("Ataque Básico", 5, 20, "nenhum"));
        // Item padrão: Poção de Vida (recupera 50 HP) – valor aumentado conforme solicitação.
        inventario.add(new Item("Poção de Vida", "poção", 0, 50, "Restaura 50 pontos de vida."));
    }

    // Métodos Getters e Setters (Encapsulamento)
    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }
    
    public int getNivel() {
        return nivel;
    }
    
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getPoderesDesbloqueados() {
        return poderesDesbloqueados;
    }

    /**
     * Desbloqueia um novo poder com um dano base progressivo.
     * O dano base é calculado a partir do valor 8, incrementado por 2 a cada reino (bossStage)
     * e acrescido de um ponto para cada poder desbloqueado já existente.
     *
     * @param poder O nome do novo poder
     * @param bossStage O reino/estágio atual (para balanceamento)
     */
    public void unlockNovoPoder(String poder, int bossStage) {
        poderesDesbloqueados++;
        int danoBase = 8 + (bossStage - 1) * 2 + (poderesDesbloqueados - 1);
        poderes.add(new Poder(poder, danoBase, 10, "nenhum"));
        System.out.println("Novo poder desbloqueado: " + poder + " (Dano base: " + danoBase + ")");
    }

    public ArrayList<Poder> getPoderes() {
        return poderes;
    }

    public ArrayList<Item> getInventario() {
        return inventario;
    }

    // Recarrega todos os poderes (Exemplo de Encapsulamento e Modularização)
    public void recarregarPoderes() {
        for (Poder p : poderes) {
            p.recarregar();
        }
    }

    /* 
       Método ganharXP(int quantidade)
       – Recebe uma quantidade de XP e acumula no jogador.
       – Quando o XP acumulado atinge o limiar (que aumenta conforme o nível atual),
         o jogador sobe de nível (Método Abstrato e Polimorfismo - implementação própria aqui) 
         e ganha 1 ponto de força.
    */
    public void ganharXP(int quantidade) {
        this.experiencia += quantidade;
        System.out.println("Você ganhou " + quantidade + " de XP. XP atual: " + this.experiencia);
        while (this.experiencia >= xpParaLevelUp()) {
            this.experiencia -= xpParaLevelUp();
            levelUp();
        }
    }

    // Calcula o XP necessário para subir de nível (aumenta conforme o nível atual)
    private int xpParaLevelUp() {
        return 50 * nivel;
    }

    // Calcula os pontos de vida iniciais do jogador de acordo com o nível (para balanceamento)
    public int calcularPontosVidaInicial() {
        return 50 + (this.nivel - 1) * 20;  // Ex.: nível 1 -> 50; nível 2 -> 70; nível 3 -> 90; etc.
    }

    // Sobe o nível do jogador, aumentando sua força.
    // OBS: Os pontos de vida atuais NÃO são restaurados no meio de uma batalha.
    private void levelUp() {
        this.nivel++;
        System.out.println("Parabéns, você subiu para o nível " + this.nivel + "!");
        // Ao subir de nível, o jogador ganha 1 ponto de força
        this.setForca(this.getForca() + 1);
        // O HP máximo aumenta conforme o novo nível, mas a recuperação total só ocorre ao retornar ao reino.
    }

    // Método sobrescrito para atacar (Polimorfismo de Classe e Sobrescrita)
    @Override
    public void atacar(Personagem alvo) {
        int dano = this.getForca() + (this.getMagia() / 2);
        alvo.setPontosVida(alvo.getPontosVida() - dano);
        System.out.println(this.getNome() + " ataca " + alvo.getNome() + " causando " + dano + " de dano!");
    }

    // Exibe o status completo do jogador
    public void exibirStatus() {
        System.out.println("Nome: " + getNome());
        System.out.println("Pontos de Vida: " + getPontosVida());
        System.out.println("Força: " + getForca());
        System.out.println("Inteligência: " + getInteligencia());
        System.out.println("Destreza: " + getDestreza());
        System.out.println("Magia: " + getMagia());
        System.out.println("Nível: " + this.nivel);
        System.out.println("XP: " + this.experiencia + " / " + xpParaLevelUp());
        System.out.println("Poderes Desbloqueados: " + poderesDesbloqueados);
    }
}
