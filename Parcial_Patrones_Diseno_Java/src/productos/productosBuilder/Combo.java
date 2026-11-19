package productos.productosBuilder;

import productos.productosDecorator.IProducto;

public class Combo implements IProducto {
    private IProducto platoPrincipal;
    private IProducto bebida;
    private IProducto acompanamiento;
    private IProducto postre;

    public Combo(IProducto platoPrincipal, IProducto bebida, IProducto acompanamiento, IProducto postre) {
        this.platoPrincipal = platoPrincipal;
        this.bebida = bebida;
        this.acompanamiento = acompanamiento;
        this.postre = postre;
    }

    @Override
    public String getDescripcion() {
        return "";
    }

    @Override
    public double getPrecio() {
        return 0.0;
    }
}