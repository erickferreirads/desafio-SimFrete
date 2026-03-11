import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;

public class Main1 {
    public static void main(String[] args) throws Exception{
        List<String> cidades = new ArrayList<>();
        List<Long> inicio = new ArrayList<>();
        List<Long> fim = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String linha;
        
        while (!(linha = br.readLine()).trim().equals("--")) {
            String[] p = linha.split(",");
            cidades.add(p[0]);
            inicio.add(Long.parseLong(p[1].trim()));
            fim.add(Long.parseLong(p[2].trim()));
        }

        long cep = Long.parseLong(br.readLine().trim() );
        br.close();

        String resultado = "CEP Não encontrado";
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
    }
}

