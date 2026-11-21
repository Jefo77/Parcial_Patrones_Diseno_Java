package productos.decorator;

public class ExtraQueso extends AdicionDecorator {

    public static final double PRECIO = 1000;

    public ExtraQueso(IProducto producto) {
        super(producto);
    }

    @Override
    protected String getNombreAdicion() {
        return "Extra queso";
    }

    @Override
    public double getPrecio() {
        return productoEnvuelto.getPrecio() + PRECIO;
    }
}
