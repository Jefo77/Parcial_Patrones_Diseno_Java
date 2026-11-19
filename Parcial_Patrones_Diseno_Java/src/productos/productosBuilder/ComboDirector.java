package productos.productosBuilder;

import productos.productosDecorator.IProducto;

public class ComboDirector {

    public Combo construirComboBasico(ComboBuilder builder, IProducto plato, IProducto bebida) {
        return null;
    }

    public Combo construirComboCompleto(ComboBuilder builder, IProducto plato, IProducto bebida,
            IProducto acompanamiento, IProducto postre) {
        return null;
    }
}