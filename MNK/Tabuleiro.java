
import java.util.ArrayList;

/**
 *
 * @author Karina Casola
 */
// Classe Tabuleiro - Representa o Tabuleiro no jogo.
public class Tabuleiro {

    private int M;
    private int N;
    private int K;
    private int[][] tabuleiro;
    private int jogadorRodada;
    private int vencedor = 0;
    private int[] vetorX;
    private int[] vetorO;

    //Construtor da Classe Tabuleiro que é chamado na primeira vez com a entrada do usuário   
    public Tabuleiro(int M, int N, int K, int[][] tabuleiro, int player) {
        this.M = M;
        this.N = N;
        this.K = K;
        this.tabuleiro = tabuleiro;
        this.jogadorRodada = player;
        this.vetorX = new int[K + 1];
        this.vetorO = new int[K + 1];
        for (int i = 0; i <= K; i++) {
            this.vetorX[i] = 0;
            this.vetorO[i] = 0;
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (tabuleiro[i][j] != 0) {
                    atualizaPontuacao(i, j, tabuleiro[i][j], true);
                }
            }
        }
    }
//Construtor da Classe Tabuleiro - Usado no método que marca a movimentação no Tabuleiro

    Tabuleiro(Tabuleiro antecessor, int[] move, int player) {
        this.M = antecessor.M;
        this.N = antecessor.N;
        this.K = antecessor.K;
        this.tabuleiro = new int[M][N];
        this.vetorX = new int[K + 1];
        this.vetorO = new int[K + 1];
        this.vetorX = antecessor.vetorX;
        this.vetorO = antecessor.vetorO;
        marcar(move, antecessor, player);
    }

    private void inicializa() {
        this.tabuleiro = new int[M][N];
        for (int row = 0; row < M; row++) {
            for (int col = 0; col < N; col++) {
                tabuleiro[row][col] = 0;
            }
        }
    }

    public int getVencedor() {
        return vencedor;
    }


    //calcule a pontuacao do jogador X
    public int calcPontuacaoX() {
        int pontuacaoX = 0;
        for (int i = 0; i < K; i++) {
            pontuacaoX += vetorX[i] * i;
        }
        return pontuacaoX;
    }

    //calcule a pontuacao do jogador O
    public int calcPontuacaoO() {
        int pontuacaoO = 0;
        for (int i = 0; i < K; i++) {
            pontuacaoO += vetorO[i] * i;
        }
        return pontuacaoO;
    }

    // verifica se houve vencedor
    public boolean isGameOver() {
        int adiversario = (jogadorRodada == 1) ? 2 : 1;
        if (verificaColunas(jogadorRodada) || verificaLinhas(jogadorRodada) || verificaDiagonal(jogadorRodada) || getPosicoesDisponiveis().isEmpty()) {
            return true;
        } else if (verificaColunas(adiversario) || verificaLinhas(adiversario) || verificaDiagonal(adiversario) || getPosicoesDisponiveis().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    // verifica as linhas para ver se o jogador ganha
    private boolean verificaLinhas(int player) {
        int max_diferente = this.N - this.K;
        int cont = 0;

        for (int i = 0; i < M; i++) {
            cont = 0;
            for (int j = 0; j < N; j++) {
                if (tabuleiro[i][j] == player) {
                    cont++;
                    if (cont == K) {
                        this.vencedor = player;
                        return true;
                    }
                } else {
                    cont = 0;
                    if (j > max_diferente) {
                        break;
                    }
                }
            }
        }
        return false;
    }

    // verifica as colunas para ver se o jogador ganha
    private boolean verificaColunas(int player) {
        int max_diferente = this.M - this.K;
        int cont = 0;
        for (int j = 0; j < N; j++) {
            cont = 0;
            for (int i = 0; i < M; i++) {
                if (tabuleiro[i][j] == player) {
                    cont++;
                    if (cont == K) {
                        this.vencedor = player;
                        return true;
                    }
                } else {
                    cont = 0;
                    if (i > max_diferente) {
                        break;
                    }
                }
            }
        }
        return false;
    }
    // verifica as diagonais para ver se o jogador ganha
    private boolean verificaDiagonal(int player) {
        int d = M - 1;
        for (int i = 0; i <= M - K; i++) {
            for (int j = 0; j <= N - K; j++) {
                int cont1 = 0, cont2 = 0;
                for (int l = 0; l < K; l++) {
                    if (tabuleiro[i + l][j + l] == player) {
                        cont1++;
                    } else {
                        cont1 = 0;
                    }
                    if (tabuleiro[d - l][j + l] == player) {
                        cont2++;
                    } else {
                        cont2 = 0;
                    }
                    if (cont2 == K || cont1 == K) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<int[]> getPosicoesDisponiveis() {
        ArrayList<int[]> posicoes = new ArrayList();

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (tabuleiro[i][j] == 0) {
                    int[] vet = {i, j};
                    posicoes.add(vet);
                }
            }
        }

        return posicoes;
    }

    public int getJogadorRodada() {
        return jogadorRodada;
    }

    // marca a posição no tabuleiro
    public void marcar(int[] move, Tabuleiro antecessor, int player) {
        for (int row = 0; row < M; row++) {
            for (int col = 0; col < N; col++) {
                this.tabuleiro[row][col] = antecessor.tabuleiro[row][col];
            }
        }

        this.tabuleiro[move[0]][move[1]] = player;
        this.jogadorRodada = (jogadorRodada == 1) ? 2 : 1;
        this.atualizaPontuacao(move[0], move[1], player, false);
    }
    
    // realiza a impressão do tabuleiro
    public void imprimir() {

        System.out.print("  ");
        for (int coluna = 0; coluna < N; coluna++) {
            System.out.print(" " + coluna);
        }
        System.out.print("\n - ");
        for (int coluna = 0; coluna < N; coluna++) {
            System.out.print("- ");
        }

        System.out.println("");
        for (int linha = 0; linha < M; linha++) {
            System.out.print(linha + ":");
            for (int coluna = 0; coluna < N; coluna++) {
                System.out.print(" " + this.tabuleiro[linha][coluna]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    /**
     * percorre o tabuleiro em todos os sentidos a partir da posição jogada 
     * pontuando a cada vez que o jogador aparece e onerando quando é o adversário 
     * @param x indice da linha
     * @param y indice da coluna
     */
    public void atualizaPontuacao(int x, int y, int jogador, boolean isInicio) {
        int[] segmento = (jogador == 1) ? this.vetorX : this.vetorO;

        segmento = calculaRetas(x, y, jogador, isInicio, segmento);
        segmento = calculaDiagonais(x, y, jogador, isInicio, segmento);
        
        if (jogador == 1) {
            this.vetorX = segmento;
        } else {
            this.vetorO = segmento;
        }

    }
    // percorre a distancia k entre os indices de ambas diagonais pontuando a cada vez que o jogador aparece e onerando quando é o adversário      
    private int[] calculaDiagonais(int y, int x, int jogador, boolean isInicio, int[] seguimento) {

        int fimLinhaD1 = Math.min(x + K - 1, M - 1);
        int fimColunaD1 = Math.min(x + K - 1, N - 1);

        int adversario = (jogador == 1) ? 2 : 1;
        for (int i = x, j = y; i <= fimLinhaD1 && j <= fimColunaD1; i++, j++) {
            int r = i - (K - 1);
            int c = j - (K - 1);
            if (r < 0 || c < 0) {
                continue;
            }

            int count = 0;
            for (; r <= i && c <= j && count != -1; r++, c++) {
                if (tabuleiro[r][c] == jogador) {
                    count++;
                } else if (tabuleiro[r][c] == adversario) {
                    count = -1;
                }
            }

            if (count > 0) {
                if (!isInicio) {
                    seguimento[count - 1]--;
                }
                seguimento[count]++;
            }
        }

        int fimLinhaD2 = Math.max(x - (K - 1), 0);
        int fimcolunaD2 = Math.min(x + K - 1, N - 1);

        for (int i = x, j = x; i >= fimLinhaD2 && j <= fimcolunaD2; i--, j++) {
            int r = i + K - 1;
            int c = j - (K - 1);
            if (r >= M || c < 0) {
                continue;
            }

            int count = 0;
            for (; r >= i && c <= j && count != -1; r--, c++) {
                if (tabuleiro[r][c] == jogador) {
                    count++;
                } else if (tabuleiro[r][c] == adversario) {
                    count = -1;
                }
            }

            if (count > 0) {
                if (!isInicio) {
                    seguimento[count - 1]--;
                }
                seguimento[count]++;
            }
        }
        return seguimento;
    }

    // percorre  linha e coluna partir dos indices pontuando a cada vez que o jogador aparece e onerando quando é o adversário  
    private int[] calculaRetas(int x, int y, int jogador, boolean isInicio, int[] pontuacao) {
        
        // verifica linha
        int inicioColuna = Math.max(0, y - (K - 1));

        int adversario = (jogador == 1) ? 2 : 1;
        for (int i = inicioColuna; i <= y; i++) {

            int count = 0;

            for (int j = 0; j < K && count != -1; j++) {
                if (i + j == N) {
                    count = -1;
                } else if (tabuleiro[x][i + j] == adversario) {
                    count = -1;
                } else if (tabuleiro[x][i + j] == jogador) {
                    count++;
                }
            }

            if (count > 0) {
                if (!isInicio) {
                    pontuacao[count - 1]--;
                }
                pontuacao[count]++;
            }
        }
        
        // verifica coluna
        int inicioLinha = Math.max(0, x - (K - 1));
        
        for (int i = inicioLinha; i <= x; i++) {
            int count = 0;
            for (int j = 0; j < K && count != -1; j++) {
                if (i + j == M || tabuleiro[i + j][y] == adversario) {
                    count = -1;
                } else if (tabuleiro[i + j][y] == jogador) {
                    count++;
                }
            }

            if (count > 0) {
                if (!isInicio) {
                    pontuacao[count - 1]--;
                }
                pontuacao[count]++;
            }
        }
        return pontuacao;
    }

    public int getM() {
        return M;
    }

    public int getN() {
        return N;
    }
    
    

}
