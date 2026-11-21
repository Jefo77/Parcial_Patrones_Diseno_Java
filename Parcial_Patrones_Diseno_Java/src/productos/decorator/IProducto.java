package productos.decorator;

public interface IProducto {

    String BEBIDA = "BEBIDA";
    String PLATO = "PLATO";
    String ACOMPANAMIENTO = "ACOMPANAMIENTO";
    String POSTRE = "POSTRE";
    String COMBO = "COMBO";

    String getId();

    String getCategoria();

    String getDescripcion();

    double getPrecio();
}
