
import modelo.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import classes.*;

public class MenuEpisodios {

    ArquivoEpisodios EpArquivo;
    ArquivoSeries SerieArquivo;
    private static Scanner scanner = new Scanner(System.in);

    public MenuEpisodios() throws Exception {
        EpArquivo = new ArquivoEpisodios();
        SerieArquivo = new ArquivoSeries();
    }

    public void menu() {

        int seletor;
        do {

            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-------");
            System.out.println("> Inicio > Episodios");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir um episodio");
            System.out.println("5 - Excluir todos os episodios de uma serie");
            System.out.println("0 - Voltar");

            System.out.print("\nDigite: ");
            try {
                seletor = Integer.valueOf(scanner.nextLine());
            } catch (NumberFormatException e) {
                seletor = -1;
            }

            switch (seletor) {
                case 1:
                    buscarEpisodio();
                    break;
                case 2:
                    incluirEpisodio();
                    break;
                case 3:
                    alterarEpisodio();
                    break;
                case 4:
                    excluirEpisodio();
                    break;
                case 5:
                    excluirEpisodiosPorSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (seletor != 0);
    }

    public void incluirEpisodio() {
        String nome = "";
        int id = 0;
        int temporada = 0;
        LocalDate lancamento = null;
        int duracao = 0;
        boolean bool = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Serie[] serie = null;

        do {
            System.out.println("\nInserir episodio em qual serie?");
            String nomeSerie = scanner.nextLine();

            try {
                serie = SerieArquivo.readNome(nomeSerie);
            } catch (Exception e) {

                System.out.println("Não foi possível buscar a serie!");
                e.printStackTrace();
            }
            if (serie == null) {
                System.out.println("Serie nao cadastrada.");
            } else {
                id = serie[0].getID();
            }

        } while (serie == null);

        do {
            System.out.print("\nNome do episodio: ");
            nome = scanner.nextLine();

        } while (nome.length() < 1);

        do {
            System.out.print("Numero da Temporada: ");
            temporada = scanner.nextInt();
            scanner.nextLine();

        } while (temporada < 0);

        do {
            System.out.print("Data de lancamento: ");
            String dataStr = scanner.nextLine();
            bool = false;
            try {
                lancamento = LocalDate.parse(dataStr, formatter);
                bool = true;
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato DD/MM/AAAA.");
            }
        } while (!bool);

        do {
            System.out.print("Duracao em minutos: ");
            duracao = scanner.nextInt();
            scanner.nextLine();
            if (duracao <= 0)
                System.out.println("Insira um numero valido!: ");

        } while (duracao <= 0);

        System.out.print("\nConfirma a inclusão da episodio? (S/N) ");
        char resp = scanner.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Episodio c = new Episodio(id, nome, temporada, lancamento, duracao);
                EpArquivo.create(c);
                System.out.println("Episodio incluído com sucesso.");
            } catch (Exception e) {
                System.out.println("Não foi possível incluir o episodio!");
            }
        }
    }

    public void alterarEpisodio() {

        System.out.println("\nAlteracao de episodio");
        System.out.print("Digite o nome do episodio: ");
        String nome = scanner.nextLine();
        Episodio[] episodioAlterado;

        try {
            episodioAlterado = EpArquivo.readNome(nome);
            if (episodioAlterado != null && episodioAlterado.length > 0) {
                System.out.println("\nEpisodios encontrados com o nome '" + nome + "':");
                for (int i = 0; i < episodioAlterado.length; i++) {
                    System.out.println("\n[" + (i + 1) + "] ");
                    mostraEpisodio(episodioAlterado[i]);
                }

                System.out.printf("\nSelecione (1-%d) qual episodio deseja alterar: ", episodioAlterado.length);
                int seletor = scanner.nextInt();
                scanner.nextLine();

                if (seletor < 1 || seletor > episodioAlterado.length) {
                    System.out.println("Opção inválida.");
                    return;
                }

                Episodio episodio = episodioAlterado[seletor - 1];
                System.out.println("Episodio escolhido:");
                mostraEpisodio(episodio);

                System.out.print("\nNovo nome: ");
                String novoNome = scanner.nextLine();
                if (!novoNome.isEmpty()) {
                    episodio.setNome(novoNome);
                }

                System.out.print("Nova temporada: ");
                String novaTemporada = scanner.nextLine();
                if (!novaTemporada.isEmpty()) {
                    try {
                        episodio.setTemporada(Integer.parseInt(novaTemporada));
                    } catch (NumberFormatException e) {
                        System.err.println("Temporada inválida. Valor mantido.");
                    }
                }

                // Alterar duração
                System.out.print("Nova duracao: ");
                String novaDuracaoStr = scanner.nextLine();
                if (!novaDuracaoStr.isEmpty()) {
                    try {
                        episodio.setDuracao(Integer.parseInt(novaDuracaoStr));
                    } catch (NumberFormatException e) {
                        System.err.println("Duracao inválida. Valor mantido.");
                    }
                }

                System.out.print("Nova data de lancamento (DD/MM/AAAA): ");
                String novaDataStr = scanner.nextLine();
                if (!novaDataStr.isEmpty()) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        episodio.setLancamento(LocalDate.parse(novaDataStr, formatter));
                    } catch (Exception e) {
                        System.err.println("Data inválida. Valor mantido.");
                    }
                }

                // Confirmar alterações
                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = scanner.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    boolean alterado = EpArquivo.update(episodio);
                    if (alterado) {
                        System.out.println("Episodio alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o episodio.");
                    }
                } else {
                    System.out.println("Alterações canceladas.");
                }
            } else {
                System.out.println("Nenhum episódio encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Não foi possível alterar o episodio!");
            e.printStackTrace();
        }
    }

    public void buscarEpisodio() {

        System.out.print("Insira o nome do episodio: ");
        String nome = scanner.nextLine();

        try {

            Episodio[] episodios = EpArquivo.readNome(nome);

            if (episodios != null && episodios.length > 0) {
                boolean encontrouEpisodio = false;

                for (Episodio episodio : episodios) {
                    if (episodio != null) {
                        mostraEpisodio(episodio);
                        encontrouEpisodio = true;
                    }
                }

                if (!encontrouEpisodio) {
                    System.out.println("Nenhum episódio encontrado.");
                }

            } else {
                System.out.println("Tente Novamente.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o episodio!");
            e.printStackTrace();
        }
    }

    public void excluirEpisodio() {

        System.out.println("\nExclusão de episodio");
        System.out.print("Digite o nome do episodio: ");
        String nome = scanner.nextLine();
        Episodio[] ea = null;

        try {

            // Buscar todos os episódios com o nome
            ea = EpArquivo.readNome(nome);

            if (ea != null && ea.length > 0) {

                System.out.println("\nEpisódios encontrados com o nome '" + nome + "':");
                for (int i = 0; i < ea.length; i++) {
                    System.out.println("\n[" + (i + 1) + "] ");
                    mostraEpisodio(ea[i]); // Exibe detalhes de cada episódio encontrado
                }

                System.out.printf("\nSelecione (1-%d) qual episódio deseja excluir: ", ea.length);
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha

                if (opcao < 1 || opcao > ea.length) {
                    System.out.println("Opção inválida.");
                    return;
                }

                Episodio episodio = ea[opcao - 1];
                System.out.print("\nConfirma a exclusão do episodio? (S/N) ");
                char resp = scanner.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {

                    boolean excluido = EpArquivo.delete(episodio.getID());
                    if (excluido) {
                        System.out.println("Episodio excluído com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o episodio.");
                    }

                } else {
                    System.out.println("Exclusão cancelada.");
                }

            } else {
                System.out.println("Nenhum episódio encontrado com esse nome.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o episodio!");
            e.printStackTrace();
        }
    }

    public void mostraEpisodio(Episodio episodio) {
        if (episodio != null) {
            System.out.println("\nDetalhes do Episodio:");
            System.out.println("----------------------");
            try {
                // ler a serie de arqSeries usando o ID do episodio
                Serie s = SerieArquivo.read(episodio.getIDSerie());
                System.out.printf("Serie........: %s\n", (s != null ? s.getNome() : "Serie não encontrada"));
            } catch (Exception e) {
                System.out.println("Erro: nao foi possível buscar a serie do episodio.");
            }
            System.out.printf("Nome.........: %s\n", episodio.getNome());
            System.out.printf("Temporada....: %d\n", episodio.getTemporada());
            System.out.printf("lancamento...: %s\n",
                    episodio.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.printf("Duracao......: %d\n", episodio.getDuracao());
            System.out.println("----------------------");
        }
    }

    public void excluirEpisodiosPorSerie() {
        System.out.print("Digite o nome da série para excluir seus episódios: ");
        String nomeSerie = scanner.nextLine();

        try {
            Serie[] series = SerieArquivo.readNome(nomeSerie);

            if (series == null || series.length == 0) {
                System.out.println("Série não encontrada.");
                return;
            }

            Serie serie = series[0];
            System.out.printf("Nome: %s\n", serie.getNome());

            System.out.print("\nConfirma a exclusão dos episódios da série? (S/N) ");
            char resp = scanner.nextLine().charAt(0);

            if (resp == 'S' || resp == 's') {
                boolean encontrouErro = false;

                while (true) {
                    Episodio[] episodios = EpArquivo.readPorSerie(serie.getID());

                    if (episodios == null || episodios.length == 0) {
                        break;
                    }

                    for (Episodio episodio : episodios) {
                        try {
                            boolean excluido = EpArquivo.delete(episodio.getID());
                            if (excluido) {
                                System.out.printf("Episódio '%s' excluído com sucesso.\n", episodio.getNome());
                            } else {
                                System.out.printf("Erro ao excluir o episódio '%s'.\n", episodio.getNome());
                                encontrouErro = true;
                            }
                        } catch (Exception e) {
                            System.out.printf("Erro ao excluir o episódio '%s': %s\n", episodio.getNome(),
                                    e.getMessage());
                            e.printStackTrace();
                            encontrouErro = true;
                        }
                    }
                }

                if (!encontrouErro) {
                    System.out.println("Todos os episódios excluídos com sucesso.");
                }
            } else {
                System.out.println("Exclusão cancelada.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao excluir episódios da série!");
            e.printStackTrace();
        }
    }

}
