package guardiaodaschamas;

import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.File;
import java.util.Arrays;

/**
 * Classe que gerencia o fluxo e a lógica do jogo "Guardião das Chamas".
 * Aplica conceitos de Orientação a Objetos: Encapsulamento, Herança, Polimorfismo, 
 * Modularização e Tratamento de Exceções.
 * (Comentários indicando “Método Construtor”, “Herança”, “Sobrescrita”, etc, estão espalhados no código.)
 */
public class Jogo {

    private Scanner scanner;                   // Encapsulamento
    private Jogador jogador;                   // Herança (Jogador estende Personagem)
    private int bossStage;                     // 1: Reino da Terra; 2: Reino das Águas; 3: Reino dos Ventos; 4: Reino do Fogo (Lian)
    private int treinamentoContador = 0;
    private boolean treinamentoConcluido = false;
    private boolean treinamentoConcluidoParaBoss = false;
    private String nomeSave;                   // Encapsulamento
    private boolean proximoCritico = false;    // Controla chance de crítico

    // Construtor da classe Jogo (Método Construtor)
    public Jogo() {
        scanner = new Scanner(System.in);
        bossStage = 1;
    }

    public void iniciar() {
        // Caso a entrada seja inválida, o jogo solicitará nova opção em vez de encerrar
        while (true) {
            limparTela();
            System.out.println("**************************************");
            System.out.println("*      Guardião das Chamas           *");
            System.out.println("**************************************\n");
            System.out.println("1 - Carregar jogo salvo");
            System.out.println("2 - Novo jogo");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();
            if (opcao.equals("1") || opcao.equals("2")) {
                if (opcao.equals("1")) {
                    SaveGame save = SaveManager.selecionarSave();
                    if (save == null) {
                        System.out.println("Nenhum save foi selecionado. Aguardando retorno...");
                        pausar(1500);
                        continue;
                    }
                    jogador = save.getJogador();
                    bossStage = save.getBossStage();
                    nomeSave = "";
                    System.out.println("\nJogo carregado com sucesso!");
                    pausar(1500);
                    exibirCheckpoint();
                } else {
                    criarNovoJogo();
                }
                break;
            }
            System.out.println("Opção inválida. Tente novamente.");
            pausar(1500);
        }
        menuPrincipal();
    }

    private void criarNovoJogo() {
        limparTela();
        System.out.print("Digite um nome para seu salvamento: ");
        nomeSave = scanner.nextLine().trim();
        jogador = new Jogador("Zuf");
        bossStage = 1;
        treinamentoContador = 0;
        treinamentoConcluido = false;
        treinamentoConcluidoParaBoss = false;
        pausar(1000);
        exibirIntroducao();
    }

    private void exibirIntroducao() {
        limparTela();
        String titulo = centralizar("Guardião das Chamas");
        System.out.println(titulo + "\n");
        String introducao = "Em um mundo repleto de magia e criaturas místicas, reinos de terra, água, ar e fogo mantinham o equilíbrio da vida através de um guardião escolhido a cada mil anos. " +
                            "Mil anos após a última ascensão do Reino do Fogo, duas crianças nasceram: Zuf, abençoado com o dom de dominar as chamas, e Lian, dotado de um extraordinário poder mágico. " +
                            "Quando Zuf foi coroado como o guardião do fogo, Lian – consumido pela inveja e ambição – roubou seus poderes, espalhando o caos que devastou a vila e mergulhou o reino em trevas. " +
                            "Hoje, sem seus poderes e com o coração em pedaços, Zuf parte em uma jornada épica para recuperar sua força, enfrentar Lian e restaurar o equilíbrio perdido.";
        List<String> linhas = wrapText(introducao, 80);
        for (String linha : linhas) {
            System.out.println(linha);
            pausar(600);
        }
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    // Método auxiliar para quebrar o texto em linhas (Modularização)
    private List<String> wrapText(String text, int maxWidth) {
        List<String> linhas = new ArrayList<>();
        String[] paragrafos = text.split("\n\n");
        for (String paragrafo : paragrafos) {
            String[] palavras = paragrafo.split(" ");
            StringBuilder linha = new StringBuilder();
            for (String palavra : palavras) {
                if (linha.length() + palavra.length() + 1 > maxWidth) {
                    linhas.add(linha.toString());
                    linha = new StringBuilder(palavra);
                } else {
                    if (linha.length() > 0)
                        linha.append(" ");
                    linha.append(palavra);
                }
            }
            if (linha.length() > 0)
                linhas.add(linha.toString());
            linhas.add("");
        }
        return linhas;
    }

    private void exibirCheckpoint() {
        limparTela();
        String titulo = centralizar("Guardião das Chamas");
        System.out.println(titulo + "\n");
        System.out.println("Você parou no checkpoint: " + getNomeBoss());
        System.out.println("Prepare-se para continuar sua aventura...");
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    // Retorna o texto centralizado na tela (apenas formatação)
    private String centralizar(String texto) {
        int largura = 80;
        int espacos = (largura - texto.length()) / 2;
        if (espacos < 0)
            espacos = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < espacos; i++)
            sb.append(" ");
        sb.append(texto);
        return sb.toString();
    }

    private void limparTela() {
        for (int i = 0; i < 10; i++)
            System.out.println();
    }

    private void pausar(int tempo) {
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void menuPrincipal() {
        while (true) {
            limparTela();
            System.out.println("===== MENU PRINCIPAL =====\n");
            System.out.println("Seu nível (Força): " + jogador.getForca());
            if (treinamentoConcluido && bossStage != 4)
                System.out.println("Treinamento concluído");
            else
                System.out.println("Requisito para próxima fase de treino: " + getRequisitoTreino());
            System.out.println("Nível do Boss (" + getNomeBoss() + "): " + getForcaBoss());
            System.out.println("Mestre atual: " + getMestreAtual());
            System.out.println();
            System.out.println("1 - Treinar com o mestre");
            System.out.println("2 - Combater o Boss do Reino (" + getNomeBoss() + ")");
            System.out.println("3 - Caçar inimigos");
            System.out.println("4 - Exibir status do personagem");
            System.out.println("5 - Salvar jogo manualmente");
            System.out.println("6 - Sair do jogo (Salvar e fechar)");
            System.out.print("Escolha uma opção: ");
            String escolha = scanner.nextLine().trim();
            System.out.println();
            switch (escolha) {
                case "1":
                    // Ao iniciar a terceira fase, o personagem recebe o poder do mestre e inicia o teste final
                    if (bossStage == 3 && treinamentoContador == 0) {
                        System.out.println("Ao iniciar a terceira fase, o Mestre dos Ventos diz: 'Sinta a brisa que anuncia sua nova era!'");
                        pausar(1200);
                        treinarConMestre();
                    }
                    if (bossStage != 4 && treinamentoConcluido)
                        System.out.println("Treinamento concluído neste reino.");
                    else {
                        if (bossStage == 4) {
                            boolean desafioConcluido = desafioFinalDosMestres();
                            if (desafioConcluido) {
                                System.out.println("Todos os mestres dizem: 'Você está verdadeiramente pronto, Zuf!'");
                                treinamentoConcluidoParaBoss = true;
                            } else {
                                System.out.println("Você não superou o desafio final dos mestres. Continue tentando!");
                            }
                        } else {
                            treinarConMestre();
                        }
                    }
                    autoSalvar();
                    break;
                case "2":
                    // Removida a restrição de força; o combate sempre ocorre
                    if (bossStage == 4) {
                        if (!treinamentoConcluidoParaBoss) {
                            System.out.println("Você precisa completar o desafio final dos mestres para enfrentar Lian!");
                            pausar(1500);
                            break;
                        }
                        lutaFinalComLian();
                    } else {
                        if (!treinamentoConcluidoParaBoss) {
                            System.out.println("Você precisa completar o treinamento com o mestre para enfrentar o boss!");
                            pausar(1500);
                            break;
                        }
                        combateBoss();
                    }
                    autoSalvar();
                    break;
                case "3":
                    cacaInimigos();
                    autoSalvar();
                    pausar(1500);
                    break;
                case "4":
                    limparTela();
                    jogador.exibirStatus();
                    System.out.println("\nPressione Enter para voltar ao menu...");
                    scanner.nextLine();
                    break;
                case "5":
                    salvarManualOpcao();
                    break;
                case "6":
                    System.out.println("\nSalvando progresso...");
                    autoSalvar();
                    System.out.println("Progresso salvo! Encerrando o jogo...");
                    pausar(1500);
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    pausar(1500);
                    break;
            }
        }
    }

    // Sistema de salvamento manual
    private void salvarManualOpcao() {
        File dir = new File(SaveManager.SAVE_DIR);
        if (!dir.exists() || dir.listFiles() == null) {
            SaveManager.salvarManual(jogador, bossStage, nomeSave);
            System.out.println("Novo save criado com sucesso!");
            pausar(1500);
            return;
        }
        File[] manualFiles = dir.listFiles((d, name) -> name.startsWith("manual_save_") && name.endsWith(".txt"));
        List<File> manualSaves = new ArrayList<>();
        if (manualFiles != null)
            manualSaves.addAll(Arrays.asList(manualFiles));
        System.out.println("Saves atuais:");
        if (manualSaves.isEmpty())
            System.out.println("Nenhum save manual encontrado.");
        else {
            for (int i = 0; i < manualSaves.size(); i++) {
                String nome = SaveManager.lerPrimeiraLinha(manualSaves.get(i));
                System.out.println((i + 1) + " - " + nome);
            }
        }
        System.out.println("Digite 'O' para sobrescrever um save ou 'N' para criar um novo save manual:");
        String resp = scanner.nextLine().trim();
        if (resp.equalsIgnoreCase("O") && !manualSaves.isEmpty()) {
            System.out.println("Digite o número do save que deseja sobrescrever:");
            String num = scanner.nextLine().trim();
            try {
                int idx = Integer.parseInt(num);
                if (idx < 1 || idx > manualSaves.size()) {
                    System.out.println("Opção inválida. Cancelando operação.");
                    pausar(1500);
                    return;
                }
                SaveManager.salvarOverwrite(jogador, bossStage, manualSaves.get(idx - 1));
                System.out.println("Save sobrescrito com sucesso!");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Cancelando operação.");
            }
        } else if (resp.equalsIgnoreCase("N")) {
            SaveManager.salvarManual(jogador, bossStage, nomeSave);
            System.out.println("Novo save criado com sucesso!");
        } else {
            System.out.println("Opção inválida. Cancelando operação.");
        }
        pausar(1500);
    }

    private void dialogoMestre() {
        if (treinamentoContador == 1)
            System.out.println("Mestre: 'A base do conhecimento é o primeiro passo para dominar o destino.'");
        if (treinamentoContador == 2)
            System.out.println("Mestre: 'Hora de colocar em prática o que aprendeu!'");
        if (treinamentoContador == 3)
            System.out.println("Mestre: 'Mostre-me sua força verdadeira, sem medo!'");
        pausar(1200);
    }

    private void dialogoBoss(String nomeBoss) {
        if (bossStage == 1)
            System.out.println(nomeBoss + ": 'Terra é minha fortaleza! Sinta o impacto do meu ataque!'");
        if (bossStage == 2)
            System.out.println(nomeBoss + ": 'Minha fúria aquática não pode ser contida!'");
        if (bossStage == 3)
            System.out.println(nomeBoss + ": 'Os ventos cortam como lâminas! Prepare-se!'");
        pausar(1200);
    }

    private void dialogoBossFinal() {
        System.out.println("Lian: 'A escuridão sempre assombrou sua existência, Zuf...'");
        pausar(1200);
        System.out.println("Zuf: 'Hoje, renasço das cinzas para romper com esse destino!'");
        pausar(1200);
        System.out.println("Lian: 'Sua luz é frágil e efêmera...'");
        pausar(1200);
    }

    // Conversa antes do desafio final dos mestres
    private void conversaAntesDesafioMestres() {
        System.out.println("Você se encontra com os três mestres:");
        System.out.println("Mestre da Terra: 'Sua força vem das raízes profundas de nossa terra.'");
        System.out.println("Mestre das Águas: 'As águas revelam sua determinação.'");
        System.out.println("Mestre dos Ventos: 'Os ventos anunciam que o clímax do seu treinamento se aproxima.'");
        pausar(1500);
    }

    // Treinamento com o mestre; ao final, o personagem recebe o poder do determinado mestre
    // e imediatamente enfrenta o mestre para testar se deve manter o poder.
    // Se perder, o poder é retirado; se ganhar, o poder é mantido.
    private void treinarConMestre() {
        if (bossStage != 4 && treinamentoConcluido) {
            System.out.println("Treinamento com o mestre deste reino já foi concluído.");
            pausar(1500);
            return;
        }
        limparTela();
        if (!treinamentoConcluidoParaBoss) {
            if (jogador.getForca() < getRequisitoTreino()) {
                System.out.println("Você não atingiu o nível requerido para esta fase de treino. (Requisito: " + getRequisitoTreino() + ")");
                System.out.println("Você pode caçar inimigos para ganhar XP.");
                pausar(1500);
                return;
            }
        }
        dialogoMestre();
        treinamentoContador++;
        if (treinamentoContador == 1) {
            System.out.println("Treinamento Teórico: Você absorve os conhecimentos iniciais.");
            jogador.ganharXP(20);
        }
        if (treinamentoContador == 2) {
            System.out.println("Treinamento Prático: Você testa seus limites em simulações.");
            jogador.ganharXP(40);
        }
        if (treinamentoContador == 3) {
            System.out.println("Treinamento Final: A prova suprema de sua força.");
            Inimigo mestre = criarMestre();
            if (mestre == null) {
                System.out.println("Nenhum mestre disponível para este reino.");
            } else {
                // RECEBE O PODER DO MESTRE E IMEDIATAMENTE ENFRENTA-O
                String novoPoder;
                if (bossStage == 1)
                    novoPoder = "Fúria Terrestre";
                else if (bossStage == 2)
                    novoPoder = "Maré Energética";
                else if (bossStage == 3)
                    novoPoder = "Vento Assassino";
                else
                    novoPoder = "Poder Místico";
                jogador.unlockNovoPoder(novoPoder, bossStage);
                System.out.println("Você recebeu o poder " + novoPoder + " do mestre!");
                pausar(1200);
                System.out.println("Agora, enfrente o mestre para provar que você merece manter esse poder!");
                pausar(1200);
                combateTurnBased(mestre, true);
                if (mestre.getPontosVida() <= 0 && jogador.getPontosVida() > 0) {
                    System.out.println("Parabéns! Você passou no teste final e manteve o poder " + novoPoder + "!");
                    jogador.ganharXP(60);
                    Item especial = gerarDropConsumivel();
                    System.out.println("Você também recebeu um " + especial.getNome() + " especial!");
                    jogador.getInventario().add(especial);
                    System.out.println("Seus PP foram restaurados após o treinamento.");
                    jogador.recarregarPoderes();
                    treinamentoConcluido = true;
                    treinamentoConcluidoParaBoss = true;
                    treinamentoContador = 0;
                } else {
                    System.out.println("Você não conseguiu superar o mestre. O poder " + novoPoder + " foi retirado de você.");
                    // Remove o poder recém-adicionado (assumindo que é o último da lista de poderes)
                    ArrayList<Poder> poderes = jogador.getPoderes();
                    if (!poderes.isEmpty()) {
                        poderes.remove(poderes.size() - 1);
                    }
                    System.out.println("Continue treinando e tente novamente.");
                }
            }
        }
        pausar(1500);
    }

    // Calcula o XP requerido para o treino de acordo com a etapa e o reino atual
    private int getRequisitoTreino() {
        int incremento = (bossStage - 1) * 2;
        if (treinamentoContador == 0)
            return 2 + incremento;
        if (treinamentoContador == 1)
            return 3 + incremento;
        if (treinamentoContador >= 2)
            return 5 + incremento;
        return 0;
    }

    /**
     * Combate contra o boss com o sistema de força implementado.
     * (A restrição por nível de força foi removida, permitindo que o combate ocorra independentemente da força do jogador.)
     */
    private void combateBoss() {
        Inimigo boss = criarBoss();
        System.out.println("Prepare-se para enfrentar " + boss.getNome() + "!");
        if (boss.getNome().equals("Lian"))
            dialogoBossFinal();
        else
            dialogoBoss(boss.getNome());
        pausar(1500);
        combateTurnBased(boss, true);
        if (jogador.getPontosVida() <= 0) {
            System.out.println("Você foi derrotado pelo boss! Voltando ao reino...");
            pausar(1500);
            jogador.setPontosVida(jogador.calcularPontosVidaInicial());
            jogador.recarregarPoderes();
        } else if (boss.getPontosVida() <= 0) {
            bossStage++;
            treinamentoConcluido = false;
            treinamentoConcluidoParaBoss = false;
            System.out.println("Você venceu o boss! Retornando ao reino...");
            pausar(1500);
            jogador.setPontosVida(jogador.calcularPontosVidaInicial());
            jogador.recarregarPoderes();
        }
    }

    private void autoSalvar() {
        SaveManager.salvarAuto(jogador, bossStage, nomeSave);
    }

    // Modo de caça: derrota monstros, acumula XP e, ao final, restaura HP e PP
    private void cacaInimigos() {
        limparTela();
        System.out.println("Você entrou no modo de caça.");
        int defeatedEnemies = 0;
        boolean continuar = true;
        while (continuar && jogador.getPontosVida() > 0) {
            Inimigo mob = gerarMobPorReino();
            System.out.println("\nInimigo encontrado: " + mob.getNome() + " [HP: " + mob.getPontosVida() + "]");
            combateTurnBased(mob, true);
            if (mob.getPontosVida() <= 0) {
                defeatedEnemies++;
                if (Math.random() < 0.3) {
                    Item drop = gerarDropConsumivel();
                    System.out.println("O inimigo dropou um " + drop.getNome() + "!");
                    jogador.getInventario().add(drop);
                }
                jogador.ganharXP(40);
                boolean respostaValida = false;
                String resposta = "";
                while (!respostaValida) {
                    System.out.print("Deseja continuar caçando? (S para sim / N para não): ");
                    resposta = scanner.nextLine().trim();
                    if (resposta.equalsIgnoreCase("S") || resposta.equalsIgnoreCase("N"))
                        respostaValida = true;
                    else
                        System.out.println("Resposta inválida, digite 'S' ou 'N'.");
                }
                if (resposta.equalsIgnoreCase("N"))
                    continuar = false;
            } else {
                System.out.println("Você fugiu deste combate.");
                break;
            }
            if (jogador.getPontosVida() <= 0) {
                System.out.println("Você foi derrotado caçando! Durante sua caçada, derrotou " + defeatedEnemies + " inimigos.");
                return;
            }
        }
        System.out.println("\nDurante sua caçada, você derrotou " + defeatedEnemies + " inimigos.");
        System.out.println("Você retornou ao reino. Sua vida e PP foram restaurados!");
        pausar(1500);
        jogador.setPontosVida(jogador.calcularPontosVidaInicial());
        jogador.recarregarPoderes();
    }

    // Permite ao jogador escolher e usar um poder no combate (Polimorfismo, Encapsulamento)
    private boolean usarPoder(Personagem alvo) {
        while (true) {
            System.out.println("Selecione um poder:");
            for (int i = 0; i < jogador.getPoderes().size(); i++) {
                Poder p = jogador.getPoderes().get(i);
                System.out.println((i + 1) + " - " + p);
            }
            System.out.println("Digite 'V' para voltar ao menu de combate.");
            System.out.print("Opção: ");
            String opcao = scanner.nextLine().trim();
            if (opcao.equalsIgnoreCase("V")) {
                System.out.println("Voltando ao menu de combate...");
                return false;
            }
            try {
                int escolha = Integer.parseInt(opcao);
                if (escolha < 1 || escolha > jogador.getPoderes().size()) {
                    System.out.println("Opção inválida. Tente novamente.");
                    continue;
                }
                Poder poderEscolhido = jogador.getPoderes().get(escolha - 1);
                if (poderEscolhido.getPpAtual() <= 0) {
                    System.out.println("PP insuficiente para o poder '" + poderEscolhido.getNome() + "'. Digite 'V' para voltar ou escolha outro poder.");
                    continue;
                }
                if (poderEscolhido.usar()) {
                    int dano = poderEscolhido.getDano();
                    if (proximoCritico) {
                        dano *= 2;
                        proximoCritico = false;
                        System.out.print("CRÍTICO! ");
                    }
                    alvo.setPontosVida(alvo.getPontosVida() - dano);
                    System.out.println("Você usou " + poderEscolhido.getNome() + "! O ataque causou " + dano + " de dano!");
                    poderEscolhido.ganharXP(5);
                }
                return true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Tente novamente.");
            }
        }
    }

    // Permite usar um item do inventário durante o combate
    private boolean usarItem() {
        System.out.println("Itens disponíveis:");
        ArrayList<Item> inventario = jogador.getInventario();
        if (inventario.isEmpty()) {
            System.out.println("Você não possui itens.");
            return false;
        }
        LinkedHashMap<String, List<Item>> grupos = new LinkedHashMap<>();
        for (Item item : inventario) {
            String key = item.getNome() + " - " + item.getDescricao();
            if (!grupos.containsKey(key))
                grupos.put(key, new ArrayList<>());
            grupos.get(key).add(item);
        }
        int index = 1;
        List<String> keys = new ArrayList<>(grupos.keySet());
        for (String key : keys) {
            int quantidade = grupos.get(key).size();
            System.out.println(index + " - " + key + " (Quantidade: " + quantidade + ")");
            index++;
        }
        System.out.println("Digite o número do item para usá-lo ou 'V' para voltar:");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("V")) {
            System.out.println("Operação cancelada. Voltando ao menu de combate...");
            return false;
        }
        int opcao;
        try {
            opcao = Integer.parseInt(input);
            if (opcao < 1 || opcao > keys.size()) {
                System.out.println("Opção inválida.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return false;
        }
        String keyEscolhido = keys.get(opcao - 1);
        Item itemSelecionado = grupos.get(keyEscolhido).get(0);
        System.out.println("Você usou: " + itemSelecionado.getNome());
        if (itemSelecionado.getNome().equalsIgnoreCase("Elixir de Poder")) {
            System.out.println("Selecione o número do poder para restaurar PP:");
            for (int i = 0; i < jogador.getPoderes().size(); i++) {
                System.out.println((i + 1) + " - " + jogador.getPoderes().get(i));
            }
            String inputPP = scanner.nextLine().trim();
            try {
                int indice = Integer.parseInt(inputPP);
                if (indice >= 1 && indice <= jogador.getPoderes().size()) {
                    Poder p = jogador.getPoderes().get(indice - 1);
                    p.setPpAtual(p.getPpAtual() + 10);
                    if (p.getPpAtual() > p.getPpMax())
                        p.setPpAtual(p.getPpMax());
                    System.out.println("PP do poder " + p.getNome() + " restaurado em 10 pontos!");
                } else {
                    System.out.println("Opção inválida. Nenhum poder restaurado.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida. Nenhum poder restaurado.");
            }
        } else if (itemSelecionado.getNome().equalsIgnoreCase("Poção de Vida")) {
            jogador.setPontosVida(jogador.getPontosVida() + itemSelecionado.getBonusMagia());
            System.out.println("Seus pontos de vida foram restaurados em " + itemSelecionado.getBonusMagia() + "!");
        } else if (itemSelecionado.getNome().equalsIgnoreCase("Poção de Impacto")) {
            proximoCritico = true;
            System.out.println("Sua chance de crítico foi aumentada para o próximo ataque!");
        }
        inventario.remove(itemSelecionado);
        return true;
    }

    // Desafio Final dos Mestres: enfrenta os três mestres, um após o outro.
    // Adiciona uma conversa do protagonista com os mestres antes do desafio e diálogo após a vitória.
    private boolean desafioFinalDosMestres() {
        System.out.println("Desafio Final dos Mestres: Enfrente os três mestres, um após o outro!");
        System.out.println("Zuf: 'Mestres, eu sei que o caminho à frente será árduo. Preciso de sua orientação para enfrentar o desafio contra Lian!'");
        pausar(1500);
        conversaAntesDesafioMestres();
        int[] realms = {1, 2, 3};
        for (int realm : realms) {
            Inimigo mestreFinal = criarMestreFinal(realm);
            System.out.println("Enfrente " + mestreFinal.getNome() + "!");
            combateTurnBased(mestreFinal, true);
            if (jogador.getPontosVida() <= 0) {
                System.out.println("Você foi derrotado por " + mestreFinal.getNome() + " no desafio final.");
                return false;
            }
            // Apenas recarregar os PP; o HP permanece
            jogador.recarregarPoderes();
            System.out.println("Você venceu " + mestreFinal.getNome() + "!");
            pausar(1500);
        }
        System.out.println("Zuf: 'Mestres, agradeço por toda sua orientação. Sei que enfrentar Lian não será apenas um teste físico, mas também uma provação do meu coração. Mesmo que o caminho seja cheio de dor e incertezas, farei o que for preciso!'");
        pausar(1500);
        System.out.println("Os três mestres se reúnem e dizem: 'Enfrentar Lian não será fácil. A dor e a escuridão aguardam, mas sua coragem o torna digno. Lute, mesmo que o caminho seja desastroso.'");
        pausar(1500);
        return true;
    }

    // Luta final contra Lian com diálogo estendido e desfecho trágico/redentor
    private void lutaFinalComLian() {
        System.out.println("\nIniciando a luta final contra Lian...");
        pausar(1200);
        System.out.println("Lian: 'Zuf, você finalmente veio. Eu esperava por este momento, mas nunca imaginei que seria assim...'");
        pausar(1200);
        System.out.println("Zuf: 'Lian, todos os caminhos me trouxeram até aqui. Você me separou do que eu era, mas não conseguiu roubar minha essência.'");
        pausar(1200);
        System.out.println("Lian: 'Sua luz sempre foi fraca, incapaz de iluminar este mundo sombrio.'");
        pausar(1200);
        System.out.println("Zuf: 'A verdadeira força vem do que carregamos no coração.'");
        pausar(1200);
        System.out.println("Lian: 'Eu também tive um coração, mas a dor o transformou em trevas.'");
        pausar(1200);
        System.out.println("Zuf: 'Dor nos molda, mas pode nos transformar em algo maior. Hoje, renasço das cinzas do meu passado!'");
        pausar(1200);
        System.out.println("Lian: 'Prove que você é o guardião, mesmo sem os poderes que um dia foram seus!'");
        pausar(1200);
        System.out.println("\nCom essas palavras, a batalha final se inicia.");
        pausar(1200);
        Inimigo lian = criarBoss();
        combateTurnBased(lian, true);
        if (jogador.getPontosVida() > 0) {
            System.out.println("\nCom um esforço final, Zuf derrota Lian.");
            pausar(1200);
            System.out.println("Lian: 'Eu queria ser reconhecido...'");
            pausar(1200);
            System.out.println("Zuf: 'Reconheço sua dor, Lian, mas a escuridão não pode prevalecer.'");
            pausar(1200);
            System.out.println("Dos escombros, uma luz intensa emerge. Zuf sente seus poderes retornarem e renasce mais forte.");
            pausar(1200);
            System.out.println("O equilíbrio é restaurado. A chama roubada retorna, e o destino se sela.");
            pausar(1200);
            exibirCreditos();
        } else {
            System.out.println("\nZuf foi consumido pela escuridão! Você foi derrotado! Voltando ao reino...");
            pausar(1500);
        }
    }

    // Exibe os créditos finais do jogo
    private void exibirCreditos() {
        limparTela();
        String[] creditos = {
            "======================================",
            "             CRÉDITOS FINAIS          ",
            "======================================",
            "",
            "Direção: André Massarente",
            "História: Taiane Colares e Jonathan Martins",
            "Documentação: Isabel Da Silva e Bruno Morais",
            "Desenvolvimento: Alberto Dias e Enzo Consani",
            "QA: Luis Felipe Freitas",
            "",
            "======================================",
            "              FIM.                    ",
            "        Obrigado por jogar!           ",
            "======================================"
        };
        for (String linha : creditos) {
            System.out.println(linha);
            pausar(1000);
        }
        System.exit(0);
    }

    // Gera um drop equilibrado para ser consumido: Poção de Vida, Elixir de Poder ou Poção de Impacto.
    private Item gerarDropConsumivel() {
        Random rnd = new Random();
        double chance = rnd.nextDouble();
        if (chance < 0.4)
            return new Item("Poção de Vida", "poção", 0, 70, "Restaura 70 pontos de vida.");
        if (chance < 0.8)
            return new Item("Elixir de Poder", "consumível", 0, 0, "Restaura 10 pontos de PP para um poder selecionado.");
        return new Item("Poção de Impacto", "consumível", 0, 0, "Aumenta a chance de crítico no próximo ataque.");
    }

    // Modificação: Para possibilitar o enfrentamento, o boss da fase 1 terá força 6 (ao invés de 8).
    private Inimigo criarBoss() {
        switch (bossStage) {
            case 1: return new Inimigo("Guardião da Terra", 100, 6, 3, 3, 3, true);
            case 2: return new Inimigo("Senhor das Águas", 110, 10, 3, 3, 3, true);
            case 3: return new Inimigo("Sábio dos Ventos", 120, 12, 4, 4, 4, true);
            case 4: return new Inimigo("Lian", 140, 15, 5, 5, 5, true);
            default: return null;
        }
    }

    // Ajuste correspondente na consulta da força do boss
    private int getForcaBoss() {
        switch (bossStage) {
            case 1: return 6;
            case 2: return 10;
            case 3: return 12;
            case 4: return 15;
            default: return 0;
        }
    }
    
    private String getNomeBoss() {
        switch (bossStage) {
            case 1: return "Guardião da Terra";
            case 2: return "Senhor das Águas";
            case 3: return "Sábio dos Ventos";
            case 4: return "Lian";
            default: return "Sem mais desafios";
        }
    }
    
    private String getMestreAtual() {
        switch (bossStage) {
            case 1: return "Mestre da Terra (Reino: Terra)";
            case 2: return "Mestre das Águas (Reino: Águas)";
            case 3: return "Mestre dos Ventos (Reino: Ventos)";
            default: return "Nenhum mestre disponível";
        }
    }
    
    // Gera um mob (inimigo comum) de acordo com o reino (bossStage)
    private Inimigo gerarMobPorReino() {
        Random rnd = new Random();
        int incremento = (bossStage - 1) * 5;
        switch (bossStage) {
            case 1:
                return (rnd.nextInt(2) == 0)
                        ? new Inimigo("Animal Selvagem", 30 + 10 * bossStage + incremento, 2 + bossStage, 1, 1, 1, false)
                        : new Inimigo("Besta da Floresta", 35 + 10 * bossStage + incremento, 3 + bossStage, 1, 1, 1, false);
            case 2:
                return (rnd.nextInt(2) == 0)
                        ? new Inimigo("Ser de Águas", 40 + 10 * bossStage + incremento, 2 + bossStage, 1, 1, 1, false)
                        : new Inimigo("Guardião Marinho", 45 + 10 * bossStage + incremento, 3 + bossStage, 1, 1, 1, false);
            case 3:
                return (rnd.nextInt(2) == 0)
                        ? new Inimigo("Espírito do Vento", 50 + 10 * bossStage + incremento, 3 + bossStage, 1, 1, 1, false)
                        : new Inimigo("Vagante Aéreo", 55 + 10 * bossStage + incremento, 4 + bossStage, 1, 1, 1, false);
            case 4:
                return (rnd.nextInt(2) == 0)
                        ? new Inimigo("Fervor Incandescente", 60 + 10 * bossStage + incremento, 4 + bossStage, 1, 1, 1, false)
                        : new Inimigo("Crepúsculo Flamejante", 65 + 10 * bossStage + incremento, 5 + bossStage, 1, 1, 1, false);
            default:
                return new Inimigo("Criatura Selvagem", 20, 2, 1, 1, 1, false);
        }
    }
    
    private Inimigo criarMestre() {
        int hpMaster;
        switch (bossStage) {
            case 1:
                hpMaster = (int)(jogador.calcularPontosVidaInicial() * 0.8);
                return new Inimigo("Mestre da Terra", hpMaster, 5, 2, 2, 2, false);
            case 2:
                hpMaster = (int)(jogador.calcularPontosVidaInicial() * 0.85);
                return new Inimigo("Mestre das Águas", hpMaster, 6, 2, 2, 2, false);
            case 3:
                hpMaster = (int)(jogador.calcularPontosVidaInicial() * 0.9);
                return new Inimigo("Mestre dos Ventos", hpMaster, 7, 3, 3, 3, false);
            default:
                return null;
        }
    }
    
    private Inimigo criarMestreFinal(int realm) {
        int hpMaster;
        switch (realm) {
            case 1:
                hpMaster = (int)(jogador.calcularPontosVidaInicial() * 0.8);
                return new Inimigo("Mestre da Terra", hpMaster, 5, 2, 2, 2, false);
            case 2:
                hpMaster = (int)(jogador.calcularPontosVidaInicial() * 0.85);
                return new Inimigo("Mestre das Águas", hpMaster, 6, 2, 2, 2, false);
            case 3:
                hpMaster = (int)(jogador.calcularPontosVidaInicial() * 0.9);
                return new Inimigo("Mestre dos Ventos", hpMaster, 7, 3, 3, 3, false);
            default:
                return null;
        }
    }
    
    // Método de Combate Turn-Based entre o jogador e um Personagem (Inimigo).
    // Aceita qualquer objeto do tipo Personagem (Polimorfismo).
    private void combateTurnBased(Personagem inimigo, boolean podeFugir) {
        while (jogador.getPontosVida() > 0 && inimigo.getPontosVida() > 0) {
            System.out.println("\n--- Seu turno ---");
            System.out.println("Seu HP: " + jogador.getPontosVida() + " | " + inimigo.getNome() + " HP: " + inimigo.getPontosVida());
            System.out.println("\nEscolha sua ação:");
            System.out.println("1 - Usar Poder");
            System.out.println("2 - Usar Item");
            if (podeFugir)
                System.out.println("3 - Fugir");
            System.out.print("Opção: ");
            String acao = scanner.nextLine().trim();
            System.out.println();
            if (acao.equals("1")) {
                boolean atacou = usarPoder(inimigo);
                if (!atacou)
                    continue;
            } else if (acao.equals("2")) {
                boolean itemUsado = usarItem();
                if (!itemUsado)
                    continue;
            } else if (acao.equals("3") && podeFugir) {
                if (Math.random() < 0.2) {
                    System.out.println("Sua tentativa de fugir falhou!");
                } else {
                    System.out.println("Você fugiu da batalha e retorna ao reino.");
                    pausar(1500);
                    return;
                }
            } else {
                System.out.println("Opção inválida, tente novamente.");
                continue;
            }
            if (inimigo.getPontosVida() < 0)
                inimigo.setPontosVida(0);
            if (jogador.getPontosVida() < 0)
                jogador.setPontosVida(0);
            if (inimigo.getPontosVida() <= 0) {
                System.out.println(inimigo.getNome() + " foi derrotado!");
                break;
            }
            // Turno do Inimigo
            System.out.println("\n--- Turno do " + inimigo.getNome() + " ---");
            pausar(1000);
            int danoInimigo = 0;
            Random rnd = new Random();
            if (inimigo.getNome().contains("Mestre")) {
                int ataque = rnd.nextInt(4) + 1;
                if (ataque == 1) {
                    danoInimigo = inimigo.getForca() + 2;
                    System.out.println(inimigo.getNome() + " usa Golpe Rápido, causando " + danoInimigo + " de dano!");
                }
                if (ataque == 2) {
                    danoInimigo = inimigo.getForca() + 4;
                    System.out.println(inimigo.getNome() + " usa Soco Poderoso, causando " + danoInimigo + " de dano!");
                }
                if (ataque == 3) {
                    danoInimigo = inimigo.getForca() + 3;
                    System.out.println(inimigo.getNome() + " usa Chute Veloz, causando " + danoInimigo + " de dano!");
                }
                if (ataque == 4) {
                    danoInimigo = inimigo.getForca() * 2;
                    System.out.println(inimigo.getNome() + " usa Combo Devastador, causando " + danoInimigo + " de dano!");
                }
            } else if (inimigo.isBoss()) {
                int ataque = rnd.nextInt(4) + 1;
                danoInimigo = ataque + inimigo.getForca();
                System.out.println(inimigo.getNome() + " ataca com seu poder, causando " + danoInimigo + " de dano!");
            } else {
                danoInimigo = rnd.nextInt(5) + inimigo.getForca();
                System.out.println(inimigo.getNome() + " ataca, causando " + danoInimigo + " de dano!");
            }
            jogador.setPontosVida(jogador.getPontosVida() - danoInimigo);
            System.out.println("Você recebeu " + danoInimigo + " de dano!");
            if (jogador.getPontosVida() <= 0) {
                System.out.println("Você foi derrotado! Voltando ao reino...");
                pausar(1500);
                return;
            }
        }
    }
}

