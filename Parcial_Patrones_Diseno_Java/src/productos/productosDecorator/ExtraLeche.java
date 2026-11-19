package productos.productosDecorator;

public class ExtraLeche extends AdicionDecorator {
    public ExtraLeche(IProducto producto) {
        super(producto);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + "+ Leche";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + 2000;
    }
}
