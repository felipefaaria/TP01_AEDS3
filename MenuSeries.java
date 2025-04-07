import modelo.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import classes.*;

public class MenuSeries {
    ArquivoSeries SerieArquivo;
    ArquivoEpisodios EpArquivo;

    private static Scanner scanner = new Scanner(System.in);

    public MenuSeries() throws Exception {
        SerieArquivo = new ArquivoSeries();
        EpArquivo = new ArquivoEpisodios();
    }

    public void menu() {
        int seletor;
        do {
            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-------");
            System.out.println("> Inicio > Series");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Listar todos episodios da serie");
            System.out.println("6 - Listar episodios por temporada");
            System.out.println("7 - Listar todas as series cadastradas");
            System.out.println("0 - Voltar");

            System.out.print("\nDigite: ");
            try {
                seletor = Integer.valueOf(scanner.nextLine());
            } catch (NumberFormatException e) {
                seletor = -1;
            }

            switch (seletor) {
                case 1:
                    buscarSerie();
                    break;
                case 2:
                    incluirSerie();
                    break;
                case 3:
                    alterarSerie();
                    break;
                case 4:
                    excluirSerie();
                    break;
                case 5:
                    listarEpisodiosPorSerie();
                    break;
                case 6:
                    listarEpisodiosPorTemporada();
                    break;
                case 7:
                    listarTodasSeries();
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (seletor != 0);
    }

    public void buscarSerie() {
        String nome;

        System.out.print("\nDigite o nome da serie: ");
        nome = scanner.nextLine();

        try {
            Serie[] series = SerieArquivo.readNome(nome);

            if (series != null && series.length > 0) {

                for (Serie serie : series) {
                    mostraSerie(serie);
                }

            } else {

                System.out.println("Serie não encontrada.");

            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o serie!");
            e.printStackTrace();
        }
    }

    public void listarEpisodiosPorSerie() {

        System.out.println("\nListagem de episodios");
        String nome;

        System.out.print("\nDigite o nome da serie: ");
        nome = scanner.nextLine();

        try {

            Serie[] series = SerieArquivo.readNome(nome);

            if (series == null || series.length == 0) {

                System.out.println("Serie não encontrada.");

            }

            Serie s = series[0];

            int idSerie = s.getID();

            Episodio[] episodios = EpArquivo.readPorSerie(idSerie);

            if (episodios == null || episodios.length == 0) {
                System.out.println("Nenhum episódio encontrado para esta série.");
                return;
            }

            System.out.println("\nEpisódios da série:");
            for (Episodio episodio : episodios) {
                System.out.println("----------------------------");
                System.out.println("Nome: " + episodio.getNome());
                System.out.println("Temporada: " + episodio.getTemporada());
                System.out.println("Tempo: " + episodio.getDuracao() + " minutos");
                System.out.println("Lançamento: " + episodio.getLancamento());
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar episódios da série!");
            e.printStackTrace();
        }
    }

    public void listarEpisodiosPorTemporada() {

        System.out.println("\nListagem de episódios por temporada");
        String nomeSerie;

        System.out.print("\nDigite o nome da serie:");
        nomeSerie = scanner.nextLine();

        try {
            Serie[] series = SerieArquivo.readNome(nomeSerie);

            if (series == null || series.length == 0) {
                System.out.println("Série não encontrada.");
                return;
            }

            Serie serie = series[0];
            mostraSerie(serie);

            System.out.print("\nDigite o número da temporada desejada: ");
            int temporadaDesejada = Integer.parseInt(scanner.nextLine());

            Episodio[] episodios = EpArquivo.readPorSerie(serie.getID());

            if (episodios == null || episodios.length == 0) {
                System.out.println("Nenhum episódio encontrado para esta série.");
                return;
            }

            System.out.println("\nEpisódios da temporada " + temporadaDesejada + ":");
            boolean encontrouEpisodios = false;
            for (Episodio episodio : episodios) {
                if (episodio.getTemporada() == temporadaDesejada) {
                    System.out.println("----------------------------");
                    System.out.println("Nome: " + episodio.getNome());
                    System.out.println("Temporada: " + episodio.getTemporada());
                    System.out.println("Tempo: " + episodio.getDuracao() + " minutos");
                    System.out.println("Lançamento: " + episodio.getLancamento());
                    encontrouEpisodios = true;
                }
            }

            if (!encontrouEpisodios) {
                System.out.println("Nenhum episódio encontrado para a temporada " + temporadaDesejada + ".");
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar episódios da temporada!");
            e.printStackTrace();
        }
    }

    public void incluirSerie() {
        String nome = "";
        String sinopse = "";
        String streaming = "";
        LocalDate dataLancamento = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        do {
            System.out.print("\nNome: ");
            nome = scanner.nextLine();
            if (nome.length() == 0) {
                return;
            }
        } while (nome.length() < 3);

        try {

            Serie[] series = SerieArquivo.readNome(nome);

            if (series != null && series.length > 0) {
                System.err.println("A serie digitada ja existe!");
                return;
            } else {
                do {
                    System.out.print("Sinopse: ");

                    sinopse = scanner.nextLine();
                    if (sinopse.length() < 10) {
                        System.err.println("A sinopse deve ter no mínimo 10 dígitos.");
                    }

                } while (sinopse.length() < 10);

                do {
                    System.out.print("Streaming: ");
                    streaming = scanner.nextLine();

                } while (streaming.length() < 3);

                boolean bool = false;
                do {
                    System.out.print("Data de lancamento (DD/MM/AAAA): ");
                    String dataStr = scanner.nextLine();

                    try {
                        dataLancamento = LocalDate.parse(dataStr, formatter);
                        bool = true;
                    } catch (Exception e) {
                        System.err.println("Data inválida!.");
                    }

                } while (!bool);

                System.out.print("\nConfirma a inclusão da serie? (S/N) ");

                char resp = scanner.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {
                    try {
                        Serie s = new Serie(nome, dataLancamento, sinopse, streaming);
                        SerieArquivo.create(s);
                        System.out.println("Serie incluída com sucesso.");
                    } catch (Exception e) {
                        System.out.println("Não foi possível incluir a serie!");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Não foi possível buscar a serie!");
            e.printStackTrace();
        }

    }

    public void alterarSerie() {
        System.out.println("\nAlteracao de serie");
        String nome = "";
        boolean nomeValido = false;

        do {
            System.out.print("\nNome: ");
            nome = scanner.nextLine();

            if (nome.isEmpty()) {
                return;
            }

            if (nome.length() > 2) {
                nomeValido = true;
            } else {
                System.out.println("O nome deve conter no mínimo 3 dígitos.");
            }

        } while (!nomeValido);

        try {

            Serie[] leSerie = SerieArquivo.readNome(nome);

            if (leSerie == null || leSerie.length == 0) {
                return;
            }

            Serie serie = leSerie[0];

            if (serie != null) {
                System.out.println("Serie encontrada:");
                mostraSerie(serie);

                Episodio[] epVinculados = EpArquivo.readPorSerie(serie.getID());

                if (epVinculados != null && epVinculados.length > 0) {
                    System.out.println("Não é possível alterar o nome da série pois existem episódios ligado a ela.");
                } else {

                    System.out.print("\nNovo nome: ");
                    String novoNome = scanner.nextLine();

                    if (!novoNome.isEmpty()) {
                        Serie[] seriesComMesmoNome = SerieArquivo.readNome(novoNome);
                        if (seriesComMesmoNome != null && seriesComMesmoNome.length > 0) {
                            System.out.println("Erro: Já existe uma série registrada com este nome.");
                            return;
                        }

                        serie.setNome(novoNome);
                    }
                }

                System.out.print("Nova sinopse: ");
                String novaSinopse = scanner.nextLine();

                if (!novaSinopse.isEmpty()) {
                    serie.setSinopse(novaSinopse);
                }

                System.out.print("Novo streaming: ");
                String novoStreaming = scanner.nextLine();

                if (!novoStreaming.isEmpty()) {
                    serie.setStreaming(novoStreaming);
                }
                System.out.print("Nova data de lancamento: ");
                String novaDataLancamento = scanner.nextLine();

                if (!novaDataLancamento.isEmpty()) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        serie.setLancamento(LocalDate.parse(novaDataLancamento, formatter));
                    } catch (Exception e) {
                        System.err.println("Data inválida.");
                    }
                }

                System.out.print("\nConfirma as alterações? (S/N)");

                char resp = scanner.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {
                    boolean alterado = SerieArquivo.update(serie);

                    if (alterado) {
                        System.out.println("Serie alterada com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar a serie.");
                    }
                } else {
                    System.out.println("Alterações canceladas.");
                }
            } else {
                System.out.println("Serie não encontrada.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar o serie!");
            e.printStackTrace();
        }
    }

    public void excluirSerie() {
        System.out.println("\nExclusão de serie");
        String nome;
        boolean nomeValido = false;

        do {
            System.out.print("\nNome: ");
            nome = scanner.nextLine();
            if (nome.isEmpty()) {
                return;
            }
            if (nome.length() > 2) {
                nomeValido = true;
            } else {
                System.out.println("Nome inválido.");
            }
        } while (!nomeValido);

        try {
            Serie[] leSeries = SerieArquivo.readNome(nome);
            if (leSeries == null || leSeries.length == 0) {
                System.out.println("Serie não encontrada.");
                return;
            }

            Serie serie = leSeries[0];

            Episodio[] epVinculados = EpArquivo.readPorSerie(serie.getID());

            if (epVinculados != null && epVinculados.length > 0) {
                System.out.println("Não é possível excluir a série pois existem episódios ligados a ela.");
                System.out.println("Exclua primeiro todos os episódios dessa série.");
                return;
            }

            System.out.println("Serie encontrada:");
            mostraSerie(serie);

            System.out.print("\nConfirma a exclusão do serie? (S/N) ");

            char resp = scanner.nextLine().charAt(0);

            if (resp == 'S' || resp == 's') {
                boolean excluido = SerieArquivo.delete(serie.getID());
                if (excluido) {
                    System.out.println("Serie excluída com sucesso.");
                } else {
                    System.out.println("Erro ao excluir a serie.");
                }
            } else {
                System.out.println("Exclusão cancelada.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o serie!");
            e.printStackTrace();
        }
    }

    public void mostraSerie(Serie serie) {
        if (serie != null) {
            System.out.println("\nSerie:");
            System.out.println("----------------------");
            System.out.printf("Nome.........: %s\n", serie.getNome());
            System.out.printf("ID...........: %d\n", serie.getID());
            System.out.printf("Streaming....: %s\n", serie.getStreaming());
            System.out.printf("Sinopse......: %s\n", serie.getSinopse());
            System.out.printf("Nascimento: %s\n",
                    serie.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("----------------------");
        }
    }

    public void listarTodasSeries() {
        try {
            List<Serie> series = SerieArquivo.readAll();

            if (series.isEmpty()) {
                System.out.println("Nenhuma série encontrada.");
                return;
            }

            for (Serie serie : series) {
                mostraSerie(serie);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar todas as séries!");
            e.printStackTrace();
        }
    }
}
