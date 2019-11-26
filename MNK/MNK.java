/**
 *
 * @author Karina Casola 151150995
 */

public class MNK {

    public MNK(){
        
    }
/**
 * Método responsável pela leitura dos parâmetros m,n,jogador e k
 * @param args 
 */
    public static void main(String[] args) {
        MNK main = new MNK();
        main.executar(args);
    }
/**
 * Método que verifica se foi passado os argumentos
 * @param args 
 */
    private void executar(String[] args) {
        
        int m = 4, n = 3, k = 3, jogador = 2;
        if(args.length> 4){
            m=Integer.valueOf(args[0]);
            n=Integer.valueOf(args[1]);
            jogador = Integer.valueOf(args[2]);          
            k = Integer.valueOf(args[3]);
        }else{
            
        }
        String valores ="";
        
        for(int i = 4; i<args.length; i++){
            valores+=args[i]+" ";
        }
        
       
        String[] split = valores.split("\\s");
        int[][] t = new int[m][n];
        
        int count = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                t[i][j] = Integer.valueOf(split[count]);
                count++;
            }
        }
        
        // Objeto da classe IA
        IA ia = new IA(jogador);
        //Objeto da classe Tabuleiro
        Tabuleiro tabuleiro = new Tabuleiro(m, n, k, t, jogador);
        
        //System.out.println("Tabuleiro inicial");
       // tabuleiro.imprimir();
        
        //run retorna a posição que a IA tem que jogar
        int[] r = ia.run( tabuleiro, jogador);
        //Marca a posição que a IA jogou
        //tabuleiro.marcar(r, tabuleiro, jogador);
        
        //imprime o tabuleiro na tela
       // tabuleiro.imprimir();
        System.out.println(ia.melhorMovimento[0]+" " +ia.melhorMovimento[1]);
        
    }
}
