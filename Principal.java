import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner;

        try {
            scanner = new Scanner(System.in);
            int seletor;
            do {

                System.out.println("PUCFlix 1.0\n" +
                        "-----------\n" +
                        "> Inicio\n\n" +
                        "1) Series\n" +
                        "2) Episodios\n" +
                        "0) Sair");

                System.out.print("\nDigite: ");
                try {
                    seletor = Integer.valueOf(scanner.nextLine());
                } catch (NumberFormatException e) {
                    seletor = -1;
                }

                switch (seletor) {
                    case 1:
                        new MenuSeries().menu();
                        break;
                    case 2:
                        new MenuEpisodios().menu();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Tente Novamente!");
                        break;
                }

            } while (seletor != 0);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
