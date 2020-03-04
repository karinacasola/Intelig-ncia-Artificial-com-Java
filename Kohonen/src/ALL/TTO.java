package ALL;



public class TTO {

  public static double parseDouble(String quantidade) {
    String qtd;
    double resultado;
    
    qtd = "";
    
    for (char c : quantidade.toCharArray()) {
      if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '.' || c == ',' || c == '-') {
        qtd += c;
      }
    }

    try {
      qtd = qtd.replaceAll(",", ".");
      resultado = Double.parseDouble(qtd);
    } catch (Exception e) {
      resultado = 0D;
    }

    return resultado;
  }

}
