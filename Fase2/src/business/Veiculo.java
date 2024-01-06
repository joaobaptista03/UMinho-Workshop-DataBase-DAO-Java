package src.business;

public class Veiculo {

    public enum TipoMotor {
        DIESEL, GASOLINA, ELECTRICO, HIBRIDOGASOLINA, HIBRIDODIESEL
    }
    
    String matricula;
    String marca;
    String modelo;
    TipoMotor tipoMotor;
    String informacoes;

    public Veiculo(String matricula, String marca, String modelo, TipoMotor tipoMotor, String informacoes) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipoMotor = tipoMotor;
        this.informacoes = informacoes;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula= matricula;
    }

    public String getMarca() {
        return this.marca;
    }

    public void setMarca(String marca) {
        this.marca= marca;
    }

    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo= modelo;
    }

    public TipoMotor getTipoMotor() {
        return this.tipoMotor;
    }

    public void setTipoMotor(TipoMotor tipoMotor) {
        this.tipoMotor= tipoMotor;
    }

    public String getInformacoes() {
        return this.informacoes;
    }

    public void setInformacoes(String informacoes) {
        this.informacoes= informacoes;
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "matricula=" + matricula +
                ", marca=" + marca +
                ", modelo=" + modelo +
                ", motor=" + tipoMotor +
                ", informacoes=" + informacoes +
                '}';
    }
}