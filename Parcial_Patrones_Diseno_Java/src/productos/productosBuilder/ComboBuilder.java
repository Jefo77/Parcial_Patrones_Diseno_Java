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
            System.out.println("Aviso: Un combo debe tener al menos un plato o una bebida.");
            return null;
        }
        return new Combo(platoPrincipal, bebida, acompanamiento, postre);
    }
}