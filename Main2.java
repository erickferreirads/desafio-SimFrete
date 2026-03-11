import java.io.*;
import java.util.*;

public class Main2 {

    static String menorNaoVistado(
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

    public static void main(String[] args) throws Exception {
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

        Map<String, List<String>> vizinhos = new HashMap<>();
        Map<String, List<Double>> custos = new HashMap<>();

        while (!(linha = br.readLine()).trim().equals("--")) {
            String[] p = linha.split(",");
            String de = p[0].trim();
            String para = p[1].trim();
            double c = Double.parseDouble(p[2].trim());

            vizinhos.computeIfAbsent(de, k -> new ArrayList<>()).add(para);
            custos.computeIfAbsent(de, k -> new ArrayList<>()).add(c);
            vizinhos.computeIfAbsent(para, k -> new ArrayList<>()).add(de);
            custos.computeIfAbsent(para, k -> new ArrayList<>()).add(c);
        }

        String[] pares = br.readLine().trim().split(",");
        long cep1 = Long.parseLong(pares[0].trim());
        long cep2 = Long.parseLong(pares[1].trim());
        br.close();

        String origem = null, destino = null;
        long menorT1 = Long.MAX_VALUE, menorT2 = Long.MAX_VALUE;

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

        Map<String, Double> dist = new HashMap<>();
        Map<String, String> ant = new HashMap<>();
        Set<String> visitados = new HashSet<>();

        for (String c : vizinhos.keySet())
            dist.put(c, Double.MAX_VALUE);
        if (origem != null) {
            dist.put(origem, 0.0);
        }

        while (true) {
            String atual = menorNaoVistado(dist, visitados);
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

        LinkedList<String> rota = new LinkedList<>();
        String passo = destino;
        while (passo != null) {
            rota.addFirst(passo);
            passo = ant.get(passo);

        }

        System.out.println(String.join(" -> ", rota));
        System.out.printf("Custo Total: %.2f%n", dist.get(destino));
    }

}
