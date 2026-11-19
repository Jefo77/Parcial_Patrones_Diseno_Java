package productos.productosDecorator;

public class BebidaBase implements IProducto {
    private String nombre;
    private double precioBase;

    public BebidaBase(String nombre, double precioBase) {
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    @Override
    public String getDescripcion() {
        return this.nombre;
    }

    @Override
    public double getPrecio() {
        return this.precioBase;
    }
}