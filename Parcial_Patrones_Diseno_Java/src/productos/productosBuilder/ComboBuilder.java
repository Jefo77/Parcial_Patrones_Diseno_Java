package productos.productosBuilder;

import productos.productosDecorator.IProducto;

public class ComboBuilder {
    private IProducto platoPrincipal;
    private IProducto bebida;
    private IProducto acompanamiento;
    private IProducto postre;

    public ComboBuilder setPlatoPrincipal(IProducto platoPrincipal) {
        this.platoPrincipal = platoPrincipal;
        return this;
    }

    public ComboBuilder setBebida(IProducto bebida) {
        this.bebida = bebida;
        return this;
    }

    public ComboBuilder setAcompanamiento(IProducto acompanamiento) {
        this.acompanamiento = acompanamiento;
        return this;
    }

    public ComboBuilder setPostre(IProducto postre) {
        this.postre = postre;
        return this;
    }

    public Combo build() {
        if (platoPrincipal == null && bebida == null) {
            throw new IllegalStateException("Un combo debe incluir al menos un plato principal o una bebida.");
        }
        return new Combo(platoPrincipal, bebida, acompanamiento, postre);
    }
}