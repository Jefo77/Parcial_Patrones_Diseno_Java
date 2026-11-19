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
        String desc = "Combo [";
        if (platoPrincipal != null) {
            desc += platoPrincipal.getDescripcion() + ", ";
        }
        if (bebida != null) {
            desc += bebida.getDescripcion() + ", ";
        }
        if (acompanamiento != null) {
            desc += acompanamiento.getDescripcion() + ", ";
        }
        if (postre != null) {
            desc += postre.getDescripcion() + ", ";
        }
        if (desc.endsWith(", ")) {
            desc = desc.substring(0, desc.length() - 2);
        }
        desc += "]";
        return desc;
    }

    @Override
    public double getPrecio() {
        double total = 0.0;
        if (platoPrincipal != null) {
            total += platoPrincipal.getPrecio();
        }
        if (bebida != null) {
            total += bebida.getPrecio();
        }
        if (acompanamiento != null) {
            total += acompanamiento.getPrecio();
        }
        if (postre != null) {
            total += postre.getPrecio();
        }
        return total;
    }
}