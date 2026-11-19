package productos.productosDecorator;

public abstract class AdicionDecorator implements IProducto {
    protected IProducto producto;

    public AdicionDecorator(IProducto producto) {
        this.producto = producto;
    }

    @Override
    public String getDescripcion() {
        return producto.getDescripcion();
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio();
    }
}