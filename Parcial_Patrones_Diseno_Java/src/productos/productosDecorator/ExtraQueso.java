package productos.productosDecorator;

public class ExtraQueso extends AdicionDecorator {
    public ExtraQueso(IProducto producto) {
        super(producto);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + "+ Queso";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + 1500;
    }
}
