package servicios;

/** Estrategia del patrón STRATEGY (comportamiento): una regla de descuento. */
public interface DescuentoStrategy {

    /** @return el valor a pagar DESPUÉS de aplicar el descuento. */
    double aplicar(double subtotal);

    String getNombre();
}
