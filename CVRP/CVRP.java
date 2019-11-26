
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Karina Casola Fernandes
 */
public class CVRP {

    private Cliente deposito;
    private List<Cliente> clientes;
    private HashMap<Integer, Cliente> clientesAtendidos;
    private double[][] matrizCustos;

    public CVRP() {
        clientes = new ArrayList<>();
        clientesAtendidos = new HashMap<>();
    }

    /**
     * Método que realiza a leitura do arquivo passado por parametro e resolve
     * as rotas
     *
     * @param arquivo
     */
    private void resolve(String arquivo) throws Exception {
        int totalClientes, capacidadeCaminhao, coordX, coordY, demanda;
        String[] linha;
        BufferedReader bfReader = new BufferedReader(new FileReader(arquivo));

        linha = bfReader.readLine().split(" ");
        totalClientes = Integer.parseInt(linha[1].trim());
        capacidadeCaminhao = Integer.parseInt(linha[2].trim());
        linha = bfReader.readLine().split(" ");
        coordX = Integer.parseInt(linha[1]);
        coordY = Integer.parseInt(linha[2]);
        deposito = new Cliente(0, coordX, coordY, 0);
        clientes.add(deposito);

        for (int i = 1; i <= totalClientes; i++) {
            linha = bfReader.readLine().split(" ");
            coordX = Integer.parseInt(linha[1]);
            coordY = Integer.parseInt(linha[2]);
            demanda = Integer.parseInt(linha[3]);

            Cliente cliente = new Cliente(i, coordX, coordY, demanda);
            clientes.add(cliente);
        }
        bfReader.close();

        geraDistanciaClientes(); // realiza o calculo entre as distáncias
        while (clientesAtendidos.size() < totalClientes) {
            programaRotaCaminhao(capacidadeCaminhao);
        }

    }

    /**
     * Método que utiliza a heuristica do vizinho mais próximo não visitado para
     * traçar rotas entre os clientes enquanto houver capacidade no veículo.
     * Sempre saindo do depósito e retornando para ele.
     *
     * @param capacidadeCaminhao
     */
    public void programaRotaCaminhao(int capacidadeCaminhao) {
        ArrayList<Integer> rota = new ArrayList<>();
        Cliente cliente = deposito;
        rota.add(cliente.getIndex()); // inicia no depósito

        int cargaRota = 0;
        while (cargaRota < capacidadeCaminhao) {
            cliente = buscaVizinhoMaisProximo(cliente, capacidadeCaminhao - cargaRota);
            if (cliente != null) {
                if (cliente.getDemanda() + cargaRota <= capacidadeCaminhao) {
                    rota.add(cliente.getIndex());
                    cargaRota += cliente.getDemanda();
                    clientesAtendidos.put(cliente.getIndex(), cliente);
//                    System.out.println(cliente.getIndex() + " demanda " + cliente.getDemanda() + " " + (capacidadeCaminhao - cargaRota));
                }
            } else {
                break;
            }
        }

        rota.add(deposito.getIndex()); // finaliza voltando ao depósito

        // imprimir a rota 
        for (Integer integer : rota) {
            System.out.print(integer + " ");
        }
        System.out.println("");

    }

    /**
     * Metodo que gera a matriz de custo com as distancias entre os clientes
     */
    public void geraDistanciaClientes() {
        matrizCustos = new double[clientes.size()][clientes.size()];
        for (int i = 0; i < clientes.size(); i++) {
            for (int j = 0; j < clientes.size(); j++) {
                matrizCustos[i][j] = distanciaEuclidiana(clientes.get(i), clientes.get(j));
            }
        }
    }

    /**
     * Método que realiza o cálculo distância Euclidiana
     *
     * @param inicio
     * @param fim
     * @return distância entre os Clientes
     */
    public double distanciaEuclidiana(Cliente inicio, Cliente fim) {
        int dX = (fim.getCoordX() - inicio.getCoordX());
        int dY = (fim.getCoordY() - inicio.getCoordY());
        return Math.sqrt(dX * dX + dY * dY);
    }

    /**
     * Retorna o cliente mais proximo do cliente que possua uma demanda que
     * caiba no caminhão
     *
     * @param cliente cliente de origem
     * @param capacidadeMaxima
     * @return Cliente mais proximo
     */
    public Cliente buscaVizinhoMaisProximo(Cliente cliente, int capacidadeMaxima) {
        int idCliente = cliente.getIndex();
        Cliente proximo = null;
        double custo = 99999;
        for (int i = 0; i < clientes.size(); i++) {
            if (matrizCustos[idCliente][i] < custo // verifica se a distancia é mais proxima do que a encontrada
                    && matrizCustos[idCliente][i] > 0) { // se 0 é o mesmo cliente
                if (clienteNaoVisitado(clientes.get(i))) {
                    if (cabeNoCaminhao(cliente, capacidadeMaxima)) {
                        proximo = clientes.get(i);
                        custo = matrizCustos[idCliente][i];
                    }
                }
            }
        }
        return proximo;
    }

    /**
     * Verifica se o cliente está contido no map de clientesAtendidos
     *
     * @param cliente
     * @return
     */
    public boolean clienteNaoVisitado(Cliente cliente) {
        if (clientesAtendidos.get(cliente.getIndex()) == null
                && !cliente.equals(deposito)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se a demanda do cliente não ultrapassa a capacidade do caminhão
     *
     * @param cliente
     * @param capacidade capacidade do caminhão
     * @return true caso ok, false caso ultrapasse
     */
    public boolean cabeNoCaminhao(Cliente cliente, int capacidade) {
        if (capacidade >= cliente.getDemanda()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            new CVRP().resolve(args[0]);
            //new CVRP().resolve("vrpnc1.txt");
        } catch (Exception ex) {
            System.out.println("Erro");
        }

    }
}
