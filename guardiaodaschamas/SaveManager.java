package guardiaodaschamas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe SaveManager
 * - Gerencia os salvamentos manuais e automáticos do jogo.
 * - Agora também salva e carrega a lista de poderes do jogador.
 */
public class SaveManager {
    public static final String SAVE_DIR = "saves"; // Modificado para public
    private static final String AUTO_SAVE_FILENAME = "auto_save.txt";

    /**
     * Salva o jogo manualmente em um arquivo cujo nome contém o nome fornecido e um timestamp.
     */
    public static void salvarManual(Jogador jogador, int bossStage, String nomeSave) {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = SAVE_DIR + "/manual_save_" + nomeSave + "_" + System.currentTimeMillis() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(nomeSave);
            writer.newLine();
            writer.write(jogador.getNome());
            writer.newLine();
            writer.write(String.valueOf(jogador.getPontosVida()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getForca()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getInteligencia()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getDestreza()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getMagia()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getExperiencia()));
            writer.newLine();
            writer.write(String.valueOf(bossStage));
            writer.newLine();
            ArrayList<Poder> poderes = jogador.getPoderes();
            writer.write(String.valueOf(poderes.size()));
            writer.newLine();
            for (Poder p : poderes) {
                writer.write(p.getNome() + ";" 
                             + p.getDano() + ";" 
                             + p.getPpMax() + ";" 
                             + p.getPpAtual() + ";" 
                             + p.getEfeito() + ";" 
                             + p.getXp() + ";" 
                             + p.getNivel());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o jogo manualmente: " + e.getMessage());
        }
    }

    /**
     * Salva o jogo automaticamente.
     */
    public static void salvarAuto(Jogador jogador, int bossStage, String nomeSave) {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = SAVE_DIR + "/" + AUTO_SAVE_FILENAME;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(nomeSave);
            writer.newLine();
            writer.write(jogador.getNome());
            writer.newLine();
            writer.write(String.valueOf(jogador.getPontosVida()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getForca()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getInteligencia()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getDestreza()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getMagia()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getExperiencia()));
            writer.newLine();
            writer.write(String.valueOf(bossStage));
            writer.newLine();
            ArrayList<Poder> poderes = jogador.getPoderes();
            writer.write(String.valueOf(poderes.size()));
            writer.newLine();
            for (Poder p : poderes) {
                writer.write(p.getNome() + ";" 
                             + p.getDano() + ";" 
                             + p.getPpMax() + ";" 
                             + p.getPpAtual() + ";" 
                             + p.getEfeito() + ";" 
                             + p.getXp() + ";" 
                             + p.getNivel());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o jogo automaticamente: " + e.getMessage());
        }
    }
    
    /**
     * Sobrescreve um save manual existente com os dados atuais do jogo.
     */
    public static void salvarOverwrite(Jogador jogador, int bossStage, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(file.getName());
            writer.newLine();
            writer.write(jogador.getNome());
            writer.newLine();
            writer.write(String.valueOf(jogador.getPontosVida()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getForca()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getInteligencia()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getDestreza()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getMagia()));
            writer.newLine();
            writer.write(String.valueOf(jogador.getExperiencia()));
            writer.newLine();
            writer.write(String.valueOf(bossStage));
            writer.newLine();
            ArrayList<Poder> poderes = jogador.getPoderes();
            writer.write(String.valueOf(poderes.size()));
            writer.newLine();
            for (Poder p : poderes) {
                writer.write(p.getNome() + ";" 
                             + p.getDano() + ";" 
                             + p.getPpMax() + ";" 
                             + p.getPpAtual() + ";" 
                             + p.getEfeito() + ";" 
                             + p.getXp() + ";" 
                             + p.getNivel());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao sobrescrever o save: " + e.getMessage());
        }
    }
    
    /**
     * Permite ao usuário selecionar um save.
     */
    public static SaveGame selecionarSave() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            return null;
        }
        File[] manualFiles = dir.listFiles((d, name) -> name.startsWith("manual_save_") && name.endsWith(".txt"));
        File autoSaveFile = new File(SAVE_DIR + "/" + AUTO_SAVE_FILENAME);
        List<File> manualSaves = new ArrayList<>();
        if (manualFiles != null) {
            manualSaves.addAll(Arrays.asList(manualFiles));
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Opções de Salvamento:");
        if (!manualSaves.isEmpty()) {
            System.out.println("Saves Manuais:");
            for (int i = 0; i < manualSaves.size(); i++) {
                String saveName = lerPrimeiraLinha(manualSaves.get(i));
                System.out.println((i + 1) + " - " + saveName);
            }
        } else {
            System.out.println("Nenhum save manual encontrado.");
        }
        boolean autoSaveExiste = autoSaveFile.exists();
        if (autoSaveExiste) {
            String autoSaveName = lerPrimeiraLinha(autoSaveFile);
            System.out.println("Salvamento Automático: " + autoSaveName);
        }
        System.out.println("\nOutras Opções:");
        System.out.println("V - Voltar ao menu");
        System.out.println("D - Excluir um save manual");
        System.out.print("\nDigite o número do save desejado, 'A' para auto save, 'V' para voltar, ou 'D' para excluir: ");
        String entrada = sc.nextLine();
        if (entrada.equalsIgnoreCase("V")) {
            return null;
        }
        if (entrada.equalsIgnoreCase("D")) {
            excluirSaveManual(manualSaves);
            return selecionarSave();
        }
        File selected = null;
        if (autoSaveExiste && entrada.equalsIgnoreCase("A")) {
            selected = autoSaveFile;
        } else {
            try {
                int escolha = Integer.parseInt(entrada);
                if (escolha < 1 || escolha > manualSaves.size()) {
                    System.out.println("Opção inválida.");
                    return null;
                }
                selected = manualSaves.get(escolha - 1);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
                return null;
            }
        }
        return carregarSaveFromFile(selected);
    }
    
    private static void excluirSaveManual(List<File> manualSaves) {
        if (manualSaves.isEmpty()) {
            System.out.println("Nenhum save manual disponível para exclusão.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Selecione o número do save manual que deseja excluir:");
        for (int i = 0; i < manualSaves.size(); i++) {
            String saveName = lerPrimeiraLinha(manualSaves.get(i));
            System.out.println((i + 1) + " - " + saveName);
        }
        System.out.print("Digite o número do save a ser excluído: ");
        String entrada = sc.nextLine();
        try {
            int escolha = Integer.parseInt(entrada);
            if (escolha < 1 || escolha > manualSaves.size()) {
                System.out.println("Opção inválida.");
                return;
            }
            File fileToDelete = manualSaves.get(escolha - 1);
            if (fileToDelete.delete()) {
                System.out.println("Save excluído com sucesso!");
            } else {
                System.out.println("Falha ao excluir o save.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }
    
    private static SaveGame carregarSaveFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String nomeSave = reader.readLine();
            String nome = reader.readLine();
            int pontosVida = Integer.parseInt(reader.readLine());
            int forca = Integer.parseInt(reader.readLine());
            int inteligencia = Integer.parseInt(reader.readLine());
            int destreza = Integer.parseInt(reader.readLine());
            int magia = Integer.parseInt(reader.readLine());
            int experiencia = Integer.parseInt(reader.readLine());
            int bossStage = Integer.parseInt(reader.readLine());
            Jogador jogador = new Jogador(nome);
            jogador.setPontosVida(pontosVida);
            jogador.setForca(forca);
            jogador.setInteligencia(inteligencia);
            jogador.setDestreza(destreza);
            jogador.setMagia(magia);
            jogador.setExperiencia(experiencia);
            String numPoderesLine = reader.readLine();
            int numPoderes = Integer.parseInt(numPoderesLine.trim());
            jogador.getPoderes().clear();
            for (int i = 0; i < numPoderes; i++) {
                String line = reader.readLine();
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    String pNome = parts[0];
                    int pDano = Integer.parseInt(parts[1]);
                    int pPpMax = Integer.parseInt(parts[2]);
                    int pPpAtual = Integer.parseInt(parts[3]);
                    String pEfeito = parts[4];
                    int pXp = Integer.parseInt(parts[5]);
                    int pNivel = Integer.parseInt(parts[6]);
                    Poder p = new Poder(pNome, pDano, pPpMax, pEfeito);
                    p.setPpAtual(pPpAtual);
                    p.setXp(pXp);
                    p.setNivel(pNivel);
                    jogador.getPoderes().add(p);
                }
            }
            return new SaveGame(jogador, bossStage);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar o save: " + e.getMessage());
            return null;
        }
    }
    
    public static String lerPrimeiraLinha(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        } catch (IOException e) {
            return "Erro ao ler save";
        }
    }
}
