package productos.decorator;

public abstract class AdicionDecorator implements IProducto {

    protected final IProducto productoEnvuelto;

    protected AdicionDecorator(IProducto productoEnvuelto) {
        if (productoEnvuelto == null) {
            throw new IllegalArgumentException("Se necesita un producto para agregarle adiciones");
        }
        this.productoEnvuelto = productoEnvuelto;
    }

    protected abstract String getNombreAdicion();

    @Override
    public String getId() {
        return productoEnvuelto.getId();
    }

    @Override
    public String getCategoria() {
        return productoEnvuelto.getCategoria();
    }

    @Override
    public String getDescripcion() {
        return productoEnvuelto.getDescripcion() + " + " + getNombreAdicion();
    }
}
