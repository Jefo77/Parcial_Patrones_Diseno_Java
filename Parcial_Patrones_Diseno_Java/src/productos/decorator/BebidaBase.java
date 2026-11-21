package productos.decorator;

public class BebidaBase implements IProducto {

    private final String id;
    private final String nombre;
    private final double precioBase;

    public BebidaBase(String id, String nombre, double precioBase) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCategoria() {
        return BEBIDA;
    }

    @Override
    public String getDescripcion() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        return precioBase;
    }
}
