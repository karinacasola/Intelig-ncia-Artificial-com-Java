package ALL;



import ALL.Emissor;
import ALL.Arquivo;
import ALL.TTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class KN {

  public static final int QUANTIDADE_ENTRADAS = 3;
  private final int QUANTIDADE_LINHAS = 4;
  private final int QTD_COLUNAS = 4;
  private final int QTD_NEURONIOS = QUANTIDADE_LINHAS * QTD_COLUNAS;
  private final double MUDANCA_MINIMA = 0.01;
  private final int QTD_TOTAL_EPOCAS = 10000;

  private final double TAXA_APRENDIZAGEM = 0.001;
  private final double RAIO_VIZINHANCA = 1D;

  private final int[][] mapa;
  private final List[] vizinhos;
  private final double pesos[][];
  private final double entrada[];
  private final HashMap<Integer, String> classeNeuronio;

  private int qtdepocas;

  
  public KN() {
    int neuronio, linhaNeuronio, colunaNeuronio;
    double distancia;

    linhaNeuronio = colunaNeuronio = 0;

    mapa = new int[QUANTIDADE_LINHAS][QTD_COLUNAS];
    vizinhos = new List[QTD_NEURONIOS];
    pesos = new double[QTD_NEURONIOS][QUANTIDADE_ENTRADAS];
    entrada = new double[QUANTIDADE_ENTRADAS];
    classeNeuronio = new HashMap<>();

   
    neuronio = 0;
    for (int linha = 0; linha < QUANTIDADE_LINHAS; linha++) {
      for (int coluna = 0; coluna < QTD_COLUNAS; coluna++) {
        classeNeuronio.put(neuronio, "-");
        mapa[linha][coluna] = neuronio++;
      }
    }

    
    for (neuronio = 0; neuronio < QTD_NEURONIOS; neuronio++) {
      vizinhos[neuronio] = new ArrayList();

      for (int linha = 0; linha < QUANTIDADE_LINHAS; linha++) {
        for (int coluna = 0; coluna < QTD_COLUNAS; coluna++) {
          if (mapa[linha][coluna] == neuronio) {
            linhaNeuronio = linha;
            colunaNeuronio = coluna;
            coluna = QTD_COLUNAS;
            linha = QUANTIDADE_LINHAS;
          }
        }
      }

      for (int linha = 0; linha < QUANTIDADE_LINHAS; linha++) {
        for (int coluna = 0; coluna < QTD_COLUNAS; coluna++) {
          if (mapa[linha][coluna] != neuronio) {
            distancia = Math.sqrt(Math.pow((double) (linhaNeuronio - linha), 2D) + Math.pow((double) (colunaNeuronio - coluna), 2D));

            if (distancia <= RAIO_VIZINHANCA) {
              vizinhos[neuronio].add(mapa[linha][coluna]);
            }
          }
        }
      }
    }
  }

 
  public boolean treinar(Arquivo arquivoTreinamento) {
    String classe;
    double mudanca;
    int neuronioMenorDistancia;
    int numAmostra;

    Emissor.inbuffer("Iniciando treinamento da rede...");

   
    for (int neuronio = 0; neuronio < QTD_NEURONIOS; neuronio++) {
      separarEntradas(arquivoTreinamento.lerArquivo().split("\n")[neuronio]);
      normalizarVetor(entrada);
      copiarVetor(entrada, pesos[neuronio]);
    }

    
    this.qtdepocas = 0;

    
    do {
      mudanca = 0D;

      
      for (String linha : arquivoTreinamento.lerArquivo().split("\n")) {
        separarEntradas(linha);
        normalizarVetor(entrada);

       
        neuronioMenorDistancia = calcNeuronioMenorDistancia();

       
        for (int i = 0; i < QUANTIDADE_ENTRADAS; i++) {
          pesos[neuronioMenorDistancia][i] += TAXA_APRENDIZAGEM * (entrada[i] - pesos[neuronioMenorDistancia][i]);
          mudanca += Math.abs(TAXA_APRENDIZAGEM * (entrada[i] - pesos[neuronioMenorDistancia][i]));
        }
        normalizarVetor(pesos[neuronioMenorDistancia]);

       
        for (Object obj : vizinhos[neuronioMenorDistancia]) {
          int neuronio = (int) obj;

          for (int i = 0; i < QUANTIDADE_ENTRADAS; i++) {
            pesos[neuronio][i] += (TAXA_APRENDIZAGEM / 2D) * (entrada[i] - pesos[neuronioMenorDistancia][i]);
          }
          normalizarVetor(pesos[neuronio]);
        }
      }

      Emissor.colocar_buffer(String.format("%d %.8f", qtdepocas, mudanca));

      qtdepocas++;
    } while (mudanca >= MUDANCA_MINIMA && qtdepocas <= QTD_TOTAL_EPOCAS);
    
    Emissor.colocar_buffer("Fim do treinamento!");
    Emissor.colocar_buffer("Iniciando identificação da classe de cada neurônio...");
    
    
    numAmostra = 0;
    
    for (String linha : arquivoTreinamento.lerArquivo().split("\n")) {
      numAmostra++;
      separarEntradas(linha);
      normalizarVetor(entrada);
      
      neuronioMenorDistancia = calcNeuronioMenorDistancia();
      
      classe = "A";
      if (numAmostra >= 21 && numAmostra <= 60) {
        classe = "B";
      }
      if (numAmostra >= 61 && numAmostra <= 120) {
        classe = "C";
      }
      
      classeNeuronio.put(neuronioMenorDistancia, classe);
    }
    
    mostrar();

    return true;
  }
  
 
  public void testar(Arquivo arquivoTeste) {
    String texto;
    int numAmostra;
    int neuronio;
    
    Emissor.inbuffer("Iniciando execução da rede");
    numAmostra = 0;
    
    for (String linha : arquivoTeste.lerArquivo().split("\n")) {
      numAmostra++;
      separarEntradas(linha);
      normalizarVetor(entrada);
      
      neuronio = calcNeuronioMenorDistancia();
      
      texto = "" + numAmostra + " ";
      
      for (int i = 0; i < QUANTIDADE_ENTRADAS; i++) {
        texto += String.format("%f ", entrada[i]);
      }
      
      texto += String.format("-> %s", classeNeuronio.get(neuronio));
      
      Emissor.colocar_buffer(texto);
    }
    
  }

  
  private void separarEntradas(String linha) {
    String[] vetor;
    int i;

    vetor = linha.split("\\s+");
    i = 0;

    if (vetor[0].equals("")) {
      i++;
    }

   
    for (int j = 0; j < QUANTIDADE_ENTRADAS; j++) {
      entrada[j] = TTO.parseDouble(vetor[i++]);
    }
  }

 
  private void normalizarVetor(double vetor[]) {
    double modulo;

    
    modulo = 0D;
    for (int i = 0; i < vetor.length; i++) {
      modulo += Math.pow(vetor[i], 2D);
    }
    modulo = Math.sqrt(modulo);

    //Normalizando cada elemento do vetor
    for (int i = 0; i < vetor.length; i++) {
      vetor[i] /= modulo;
    }
  }

 
  private double distanciaEuclidiana(double vetor1[], double vetor2[]) {
    double distancia;

    distancia = 0D;

    for (int i = 0; i < vetor1.length; i++) {
      distancia += Math.pow(vetor1[i] - vetor2[i], 2D);
    }

    distancia = Math.sqrt(distancia);

    return distancia;
  }

  
  private void copiarVetor(double[] origem, double[] destino) {
    for (int i = 0; i < origem.length && i < destino.length; i++) {
      destino[i] = origem[i];
    }
  }

 
  private int calcNeuronioMenorDistancia() {
    double distancia;
    double menorDistancia;
    int neuronioMenorDistancia;

    menorDistancia = Double.MAX_VALUE;
    neuronioMenorDistancia = 0;

    for (int neuronio = 0; neuronio < QTD_NEURONIOS; neuronio++) {
      distancia = distanciaEuclidiana(pesos[neuronio], entrada);

      if (distancia < menorDistancia) {
        menorDistancia = distancia;
        neuronioMenorDistancia = neuronio;
      }
    }

    return neuronioMenorDistancia;
  }

 
  private void mostrar() {
    String texto;
    
    Emissor.colocar_buffer("Mapa de contexto:");
    
    for (int linha = 0; linha < QUANTIDADE_LINHAS; linha++) {
      texto = "";
      
      for (int coluna = 0; coluna < QTD_COLUNAS; coluna++) {
        texto += classeNeuronio.get(mapa[linha][coluna]) + "  ";
      }
      
      Emissor.colocar_buffer(texto);
    }
    
    Emissor.colocar_buffer("Classe de cada neurônio:");
    for (int neuronio = 0; neuronio < QTD_NEURONIOS; neuronio++) {
      Emissor.colocar_buffer(String.format("%d -> %s", neuronio+1, classeNeuronio.get(neuronio)));
    }
  }
}
