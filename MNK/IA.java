
/**
 *
 * @author Karina Casola 151150995
 */
public class IA {

    int jogador;
    int[] melhorMovimento;

    /**
     * Método construtor da classe
     *
     * @param jogador
     */
    public IA(int jogador) {
        this.jogador = jogador;
        melhorMovimento = new int[2];
        melhorMovimento[0] = -1;
        melhorMovimento[1] = -1;
    }

    public int[] run(Tabuleiro tabuleiro, int jogador) {
        int[] pai = {-1, -1};
        if (tabuleiro.getPosicoesDisponiveis().size() == tabuleiro.getM() * tabuleiro.getN()) {
            int meioLinha = (int) tabuleiro.getM() / 2;
            int meioColuna = (int) tabuleiro.getN() / 2;
            pai[0] = meioLinha;
            pai[1] = meioColuna;
            this.melhorMovimento=pai;
            return pai;
        }
        //inicializa em alfa para ir se aprimorando
        minimax(tabuleiro, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, pai, jogador);
        return melhorMovimento;
    }

    public int minimax(Tabuleiro tabuleiro, double alpha, double beta, int profundidade, int[] pai, int jogador) {
        if (tabuleiro.isGameOver()) {
            melhorMovimento = pai;
            // System.out.println("pai" + pai[0] + pai[1]);
            return pontuacao(tabuleiro, profundidade);
        }

        if (jogador == this.jogador) {
            return getMax(tabuleiro, alpha, beta, profundidade, jogador);
        } else {
            return getMin(tabuleiro, alpha, beta, profundidade, jogador);
        }

    }

    /**
     * Método que retorna a posição e posição mínima do jogador realizando a
     * poda alfa beta
     *
     * @param tabuleiro
     * @param alpha
     * @param beta
     * @param profundidade
     * @param jogador
     * @return
     */

    private int getMin(Tabuleiro tabuleiro, double alpha, double beta, int profundidade, int jogador) {

        for (int[] move : tabuleiro.getPosicoesDisponiveis()) {
            Tabuleiro novo = new Tabuleiro(tabuleiro, move, jogador);
            int pontuacao = minimax(novo, alpha, beta, profundidade + 1, move, trocaJogador(jogador));

            if (pontuacao < beta) {
                beta = pontuacao;
                this.melhorMovimento = move;
                //        System.out.println("Achei beta: " + beta + " " + melhorMovimento[0] + " " + melhorMovimento[1]);
            }

            if (alpha >= beta) {
                break;
            }
        }
        // System.out.println("Beta:" + beta);

        return (int) beta + profundidade;
    }

    /**
     * retorna a posição e pontuação máxima do jogador utilizando o algoritmo
     * alfa beta
     *
     * @param tabuleiro
     * @param alpha
     * @param beta
     * @param profundidade
     * @param jogador
     * @return
     */
    private int getMax(Tabuleiro tabuleiro, double alpha, double beta, int profundidade, int jogador) {

        for (int[] move : tabuleiro.getPosicoesDisponiveis()) {
            Tabuleiro novo = new Tabuleiro(tabuleiro, move, jogador);
            int pontucao = minimax(novo, alpha, beta, profundidade + 1, move, trocaJogador(jogador));

            if (pontucao > alpha) {
                alpha = pontucao;
                this.melhorMovimento = move;
                //  System.out.println(profundidade + "Achei alpha: " + alpha + " " + melhorMovimento[0] + " " + melhorMovimento[1]);
            }
            if (alpha >= beta) {
                break;
            }

        }

        return (int) alpha - profundidade;
    }

    /**
     * Verifica a pontuação da IA venceu e retorna valor alto, se a IA perdeu
     * perdeu valor baixo, senão irá avaliar o estado atual das posições
     *
     * @param tabuleiro
     * @param profundidade
     * @return
     */
    public int pontuacao(Tabuleiro tabuleiro, int profundidade) {
        if (tabuleiro.getVencedor() == jogador) {
            return 1000 - profundidade;
        } else if (tabuleiro.getVencedor() == trocaJogador(jogador)) {
            return -1000 + profundidade;
        } else if (tabuleiro.getJogadorRodada() == 1) { // empatado
            return tabuleiro.calcPontuacaoX() - tabuleiro.calcPontuacaoO();
        } else if (tabuleiro.getJogadorRodada() == 0) { // empatado
            return tabuleiro.calcPontuacaoO() - tabuleiro.calcPontuacaoX();
        } else {
            return 0;
        }

    }

    /**
     * retorna o próximo jogador
     *
     * @param jogador
     * @return
     */
    private int trocaJogador(int jogador) {
        if (jogador == 1) {
            return 2;
        } else {
            return 1;
        }
    }

}
