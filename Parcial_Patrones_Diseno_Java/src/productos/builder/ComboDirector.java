package productos.builder;

import productos.decorator.IProducto;

public class ComboDirector {

    private final ComboBuilder builder;

    public ComboDirector(ComboBuilder builder) {
        this.builder = builder;
    }

    public Combo armarCombo(IProducto plato, IProducto bebida, IProducto acompanamiento, IProducto postre) {
        if (plato != null) {
            builder.setPlatoPrincipal(plato);
        }
        if (bebida != null) {
            builder.setBebida(bebida);
        }
        if (acompanamiento != null) {
            builder.setAcompanamiento(acompanamiento);
        }
        if (postre != null) {
            builder.setPostre(postre);
        }
        return builder.build();
    }
}

