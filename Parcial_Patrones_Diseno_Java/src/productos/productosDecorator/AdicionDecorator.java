package productos.productosDecorator;

public abstract class AdicionDecorator implements IProducto {
    protected IProducto productoEnvuelto;

    public AdicionDecorator(IProducto producto) {

    }

    @Override
    public String getDescripcion() {
        return productoEnvuelto.getDescripcion();
    }

    @Override
    public double getPrecio() {
        return productoEnvuelto.getPrecio();
    }
}