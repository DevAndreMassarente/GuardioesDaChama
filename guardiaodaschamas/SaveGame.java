package guardiaodaschamas;

public class SaveGame {
    private Jogador jogador;
    private int bossStage;

    public SaveGame(Jogador jogador, int bossStage) {
        this.jogador = jogador;
        this.bossStage = bossStage;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public int getBossStage() {
        return bossStage;
    }
}
