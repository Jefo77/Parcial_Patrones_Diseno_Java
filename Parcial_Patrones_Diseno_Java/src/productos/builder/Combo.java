package productos.builder;

import productos.decorator.IProducto;

public class Combo implements IProducto {

    private final IProducto platoPrincipal;
    private final IProducto bebida;
    private final IProducto acompanamiento; 
    private final IProducto postre;         

    Combo(IProducto platoPrincipal, IProducto bebida, IProducto acompanamiento, IProducto postre) {
        this.platoPrincipal = platoPrincipal;
        this.bebida = bebida;
        this.acompanamiento = acompanamiento;
        this.postre = postre;
    }

    @Override
    public String getId() {
        return COMBO;
    }

    @Override
    public String getCategoria() {
        return COMBO;
    }

    @Override
    public String getDescripcion() {
        StringBuilder sb = new StringBuilder("Combo [")
                .append(platoPrincipal.getDescripcion())
                .append(" + ").append(bebida.getDescripcion());
        if (acompanamiento != null) {
            sb.append(" + ").append(acompanamiento.getDescripcion());
        }
        if (postre != null) {
            sb.append(" + ").append(postre.getDescripcion());
        }
        return sb.append("]").toString();
    }

    @Override
    public double getPrecio() {
        double total = platoPrincipal.getPrecio() + bebida.getPrecio();
        if (acompanamiento != null) {
            total += acompanamiento.getPrecio();
        }
        if (postre != null) {
            total += postre.getPrecio();
        }
        return total;
    }
}
