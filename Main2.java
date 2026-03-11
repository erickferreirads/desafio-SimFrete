import java.io.*;
import java.util.*;

public class Main2 {

    static String menorNaoVisitado(
            Map<String, Double> distancia, Set<String> visitados) {
        String menor = null;
        for (String c : distancia.keySet()) {
            if (!visitados.contains(c)) {
                if (menor == null || distancia.get(c) < distancia.get(menor)) {
                    menor = c;

                }
            }
        }
        return menor;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out
                    .println("Para testar corretamente (no console): javac Main2.java / depois: java Main2 teste2.txt");
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

            Map<String, List<String>> vizinhos = new HashMap<>();
            Map<String, List<Double>> custos = new HashMap<>();

            while ((linha = br.readLine()) != null && !linha.trim().equals("--")) {
                String[] p = linha.split(",");
                String de = p[0].trim();
                String para = p[1].trim();
                double custo = Double.parseDouble(p[2].trim());

                vizinhos.computeIfAbsent(de, k -> new ArrayList<>()).add(para);
                custos.computeIfAbsent(de, k -> new ArrayList<>()).add(custo);
                vizinhos.computeIfAbsent(para, k -> new ArrayList<>()).add(de);
                custos.computeIfAbsent(para, k -> new ArrayList<>()).add(custo);
            }

            String[] pares = br.readLine().trim().split(",");
            long cep1 = Long.parseLong(pares[0].trim());
            long cep2 = Long.parseLong(pares[1].trim());

            String origem = null;
            String destino = null;
            long menorT1 = Long.MAX_VALUE;
            long menorT2 = Long.MAX_VALUE;

            for (int i = 0; i < cidades.size(); i++) {
                long tam = fim.get(i) - inicio.get(i);
                if (cep1 >= inicio.get(i) && cep1 <= fim.get(i) && tam < menorT1) {
                    menorT1 = tam;
                    origem = cidades.get(i);
                }
                if (cep2 >= inicio.get(i) && cep2 <= fim.get(i) && tam < menorT2) {
                    menorT2 = tam;
                    destino = cidades.get(i);
                }
            }

            if (origem == null || destino == null) {
                System.out.println("CEP nao encontrado");
                return;
            }

            Map<String, Double> dist = new HashMap<>();
            Map<String, String> ant = new HashMap<>();
            Set<String> visitados = new HashSet<>();

            for (String c : vizinhos.keySet())
                dist.put(c, Double.MAX_VALUE);
            dist.put(origem, 0.0);

            while (true) {
                String atual = menorNaoVisitado(dist, visitados);
                if (atual == null || atual.equals(destino))
                    break;
                visitados.add(atual);

                List<String> vs = vizinhos.getOrDefault(atual, new ArrayList<>());
                List<Double> cs = custos.getOrDefault(atual, new ArrayList<>());

                for (int i = 0; i < vs.size(); i++) {
                    String viz = vs.get(i);
                    double novoCusto = dist.get(atual) + cs.get(i);

                    if (novoCusto < dist.getOrDefault(viz, Double.MAX_VALUE)) {
                        dist.put(viz, novoCusto);
                        ant.put(viz, atual);
                    }
                }
            }

            if (dist.getOrDefault(destino, Double.MAX_VALUE) == Double.MAX_VALUE) {
                System.out.println("Sem rota disponivel");
                return;
            }

            LinkedList<String> rota = new LinkedList<>();
            String passo = destino;
            while (passo != null) {
                rota.addFirst(passo);
                passo = ant.get(passo);

            }

            System.out.println(String.join(" -> ", rota));
            System.out.printf("Custo Total: %.2f%n", dist.get(destino));

            
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
