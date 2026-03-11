import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;

public class Main1 {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out
                    .println("Para testar corretamente (no console): javac Main1.java / depois: java Main1 teste1.txt");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {

            List<String> cidades = new ArrayList<>();
            List<Long> inicio = new ArrayList<>();
            List<Long> fim = new ArrayList<>();

            String linha;
            while ((linha = br.readLine()) != null && !linha.trim().equals("--")) {
                String[] p = linha.split(",");
                cidades.add(p[0].trim());
                inicio.add(Long.parseLong(p[1].trim()));
                fim.add(Long.parseLong(p[2].trim()));
            }

            long cep = Long.parseLong(br.readLine().trim());

            String resultado = "CEP Nao encontrado";
            long menorTamanho = Long.MAX_VALUE;

            for (int i = 0; i < cidades.size(); i++) {
                boolean dentro = cep >= inicio.get(i) && cep <= fim.get(i);

                if (dentro) {
                    long tamanho = fim.get(i) - inicio.get(i);

                    if (tamanho < menorTamanho) {
                        menorTamanho = tamanho;
                        resultado = cidades.get(i);
                    }
                }
            }

            System.out.println(resultado);
        } catch (FileNotFoundException e) {
            System.out.println("Erro: arquivo '" + args[0] + "' nao encontrado.");
            System.out.println("Pasta atual: " + System.getProperty("user.dir"));

        } catch (NumberFormatException e) {
            System.out.println("Erro: valor invalido no arquivo: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

    }
}
