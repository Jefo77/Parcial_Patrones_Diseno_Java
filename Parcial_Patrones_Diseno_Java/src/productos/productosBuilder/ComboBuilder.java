package productos.productosBuilder;

import productos.productosDecorator.IProducto;

public class ComboBuilder {
    private IProducto platoPrincipal;
    private IProducto bebida;
    private IProducto acompanamiento;
    private IProducto postre;

    public ComboBuilder setPlatoPrincipal(IProducto platoPrincipal) {
        return this;
    }

    public ComboBuilder setBebida(IProducto bebida) {
        return this;
    }

    public ComboBuilder setAcompanamiento(IProducto acompanamiento) {
        return this;
    }

    public ComboBuilder setPostre(IProducto postre) {
        return this;
    }

    public Combo build() {
        return null;
    }
}