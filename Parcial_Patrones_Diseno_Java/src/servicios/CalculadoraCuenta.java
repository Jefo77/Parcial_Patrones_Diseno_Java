package servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contexto del patrón STRATEGY. Calcula el valor final sin conocer las reglas de cada promoción:
 * solo usa la estrategia que tenga asignada. Agregar promociones nuevas NO obliga a tocar esta clase.
 */
public class CalculadoraCuenta {

    private DescuentoStrategy estrategia = new SinDescuento();
    private LocalDateTime fechaHora = LocalDateTime.now();
    private final List<PromocionHorario> promociones = new ArrayList<>();

    public void setEstrategia(DescuentoStrategy estrategia) {
        this.estrategia = estrategia != null ? estrategia : new SinDescuento();
    }

    public DescuentoStrategy getEstrategia() {
        return estrategia;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void agregarPromocion(PromocionHorario promocion) {
        promociones.add(promocion);
    }

    public List<PromocionHorario> getPromociones() {
        return Collections.unmodifiableList(promociones);
    }

    /** Elige automáticamente la promoción vigente (activa y dentro de día/horario) con mayor descuento. */
    public void seleccionarPromocionVigente() {
        PromocionHorario mejor = null;
        for (PromocionHorario p : promociones) {
            if (p.estaVigente(fechaHora) && (mejor == null || p.getPorcentaje() > mejor.getPorcentaje())) {
                mejor = p;
            }
        }
        estrategia = mejor != null ? mejor : new SinDescuento();
    }

    public double calcularTotal(double subtotal) {
        return estrategia.aplicar(subtotal);
    }

    public double calcularDescuento(double subtotal) {
        return subtotal - calcularTotal(subtotal);
    }
}