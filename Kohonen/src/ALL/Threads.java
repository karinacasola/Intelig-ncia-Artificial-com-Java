package ALL;


import javax.swing.JOptionPane;


public class Threads extends Thread {

  private KN redeKN = null;
  private Arquivo treinamento;

  public Threads(KN rede) {
    this.redeKN = rede;
  }

  public void set_treinamento(Arquivo atreinamento) {
    this.treinamento = atreinamento;
  }

  @Override
  public void run() {
    if (redeKN != null && treinamento != null) {
      exibir_msg(redeKN.treinar(treinamento));

      stop();
    }
  }

  private void exibir_msg(boolean val) {
    if (val) {
      JOptionPane.showMessageDialog(null, "Rede neural treinada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
      Emissor.set_ati_botoes(true);
    } else {
      JOptionPane.showMessageDialog(null, "Erro", "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

}
