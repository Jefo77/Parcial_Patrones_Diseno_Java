package productos.decorator;

public class PlatoBase implements IProducto {

    private final String id;
    private final String categoria;
    private final String nombre;
    private final double precioBase;

    public PlatoBase(String id, String categoria, String nombre, double precioBase) {
        if (!PLATO.equals(categoria) && !ACOMPANAMIENTO.equals(categoria) && !POSTRE.equals(categoria)) {
            throw new IllegalArgumentException("Categoría no válida para PlatoBase: " + categoria);
        }
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCategoria() {
        return categoria;
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
