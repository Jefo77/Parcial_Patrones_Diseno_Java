package productos.productosDecorator;

public class BebidaBase implements IProducto {
    // 1. Atributos privados (Encapsulamiento)
    private String nombre;
    private double precioBase;

    // 2. Constructor
    public BebidaBase(String nombre, double precioBase) {
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    // 3. Métodos obligatorios del contrato IProducto
    @Override
    public String getDescripcion() {
        return this.nombre;
    }

    @Override
    public double getPrecio() {
        return this.precioBase;
    }
}