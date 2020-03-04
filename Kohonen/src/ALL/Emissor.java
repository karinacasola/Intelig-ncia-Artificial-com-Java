package ALL;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTextArea;


public abstract class Emissor {

  private static JTextArea cbuff = null;
  private static List<JButton> lb = null;

  public static void setCampo(JTextArea cbuffer) {
    Emissor.cbuff = cbuffer;
  }
  
  public static void addBotao(JButton botao) {
    if (lb == null) {
      lb = new ArrayList<>();
    }
    
    lb.add(botao);
  }
  
  public static void adicionar_botoes(JButton... botoes) {
    if (lb == null) {
      lb = new ArrayList<>();
    }
    
    lb.addAll(Arrays.asList(botoes));
  }
  
  public static void excluir_botoes() {
    lb.clear();
  }
  
  public static void inbuffer(String texto) {
    if (cbuff != null) {
      cbuff.setText(texto);
    }
  }
  
  public static void colocar_buffer(String texto) {
    if (cbuff != null) {
      cbuff.append(String.format("\n%s", texto));
      cbuff.setCaretPosition(cbuff.getText().length());
    }
  }
  
  public static void set_ati_botoes(boolean val) {
    if (lb == null) {
      return;
    }
    
    for (JButton botao : lb) {
      botao.setEnabled(val);
    }
  }

}
