package one.digitalinnovation.gof.strategy;

public class Robo {

    private Comportamento comportamentoStrategico;

    public void setComportamentoStrategico(Comportamento comportamentostrategico) {
        this.comportamentoStrategico = comportamentostrategico;
    }

    public void mover() {
        comportamentoStrategico.mover();
    }
}
