# Guardiões da Chama - APS 2S

Jogo de RPG em console desenvolvido em Java para aplicar conceitos de Programação Orientada a Objetos (encapsulamento, herança, polimorfismo, modularização e tratamento de exceções).

## Funcionalidades
- História com progressão por reinos e bosses.
- Treinamento com mestres, evolução por XP e desbloqueio de poderes.
- Sistema de combate por turnos com itens e chance de crítico.
- Salvamento manual e automático.

## Tecnologias
- Java (console)

## Como rodar
1) Compile os arquivos:
```bash
javac -d out guardiaodaschamas/*.java
```

2) Execute o jogo:
```bash
java -cp out guardiaodaschamas.Main
```

## Estrutura principal
- `guardiaodaschamas/`: código-fonte Java.
- `saves/`: arquivos de salvamento locais (não versionados).
- `out/`: classes compiladas (não versionadas).

## Observações
- Os saves são gerados localmente em `saves/` e não devem ir para o GitHub.
