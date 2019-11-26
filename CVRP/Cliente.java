
/**
 * @author Karina Casola Fernandes Matricula: 151150995
 */
public class Cliente {

    private int index;
    private int coordX;
    private int coordY;
    private int demanda;

    public Cliente(int id, int coordX, int coordY, int demanda) {
        this.index = id;
        this.coordX = coordX;
        this.coordY = coordY;
        this.demanda = demanda;
    }

    public int getIndex() {
        return index;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public int getDemanda() {
        return demanda;
    }

}
