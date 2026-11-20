package pedidos;

public abstract class EstacionRol implements IObservadorPedido {
    protected String nombreEstacion;

    public EstacionRol(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }
}