package productos.productosDecorator;

public abstract class AdicionDecorator implements IProducto {
    protected IProducto productoEnvuelto;

    public AdicionDecorator(IProducto producto) {
        if (productoEnvuelto == null) {
            throw new IllegalArgumentException("No se puede agregar una adición a un producto vacío.");
        }
        this.productoEnvuelto = productoEnvuelto;
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