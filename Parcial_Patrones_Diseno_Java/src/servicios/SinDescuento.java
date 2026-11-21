package servicios;

public class SinDescuento implements DescuentoStrategy {

    @Override
    public double aplicar(double subtotal) {
        return subtotal;
    }

    @Override
    public String getNombre() {
        return "Sin descuento";
    }
}

