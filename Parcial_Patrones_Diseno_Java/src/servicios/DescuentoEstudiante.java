package servicios;

public class DescuentoEstudiante implements DescuentoStrategy {

    @Override
    public double aplicar(double subtotal) {
        return subtotal * 0.9;
    }

    @Override
    public String getNombre() {
        return "Descuento estudiante (10%)";
    }
}
