package productos.decorator;

public class ExtraLeche extends AdicionDecorator {

    public static final double PRECIO = 1000;

    public ExtraLeche(IProducto producto) {
        super(producto);
    }

    @Override
    protected String getNombreAdicion() {
        return "Extra leche";
    }

    @Override
    public double getPrecio() {
        return productoEnvuelto.getPrecio() + PRECIO;
    }
}
