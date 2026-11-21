package productos.builder;

import productos.decorator.IProducto;

public class ComboBuilder {

    private IProducto platoPrincipal;
    private IProducto bebida;
    private IProducto acompanamiento;
    private IProducto postre;

    public ComboBuilder setPlatoPrincipal(IProducto producto) {
        this.platoPrincipal = validar(producto, IProducto.PLATO, "plato principal");
        return this;
    }

    public ComboBuilder setBebida(IProducto producto) {
        this.bebida = validar(producto, IProducto.BEBIDA, "bebida");
        return this;
    }

    public ComboBuilder setAcompanamiento(IProducto producto) {
        this.acompanamiento = validar(producto, IProducto.ACOMPANAMIENTO, "acompañamiento");
        return this;
    }

    public ComboBuilder setPostre(IProducto producto) {
        this.postre = validar(producto, IProducto.POSTRE, "postre");
        return this;
    }

    public Combo build() {
        if (platoPrincipal == null) {
            throw new IllegalStateException("El combo necesita un plato principal");
        }
        if (bebida == null) {
            throw new IllegalStateException("El combo necesita una bebida");
        }
        return new Combo(platoPrincipal, bebida, acompanamiento, postre);
    }

    private IProducto validar(IProducto producto, String categoria, String paso) {
        if (producto == null || !categoria.equals(producto.getCategoria())) {
            throw new IllegalArgumentException("Opción no válida para " + paso);
        }
        return producto;
    }
}
