package productos.productosBuilder;

import productos.productosDecorator.IProducto;

public class ComboDirector {

    public Combo construirComboBasico(ComboBuilder builder, IProducto plato, IProducto bebida) {
        return builder.setPlatoPrincipal(plato).setBebida(bebida).build();
    }

    public Combo construirComboCompleto(ComboBuilder builder, IProducto plato, IProducto bebida,
            IProducto acompanamiento, IProducto postre) {
        return builder.setPlatoPrincipal(plato).setBebida(bebida).setAcompanamiento(acompanamiento).setPostre(postre)
                .build();
    }
}