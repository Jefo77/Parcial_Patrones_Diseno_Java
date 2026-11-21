package servicios;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Estrategia configurable: porcentaje de descuento que aplica solo en ciertos días y franja horaria.
 * Con esta única clase se pueden crear todas las promociones por horario (RF-11 y RF-12).
 */
public class PromocionHorario implements DescuentoStrategy {

    private static final Locale ES = Locale.forLanguageTag("es");

    private final String nombre;
    private final double porcentaje;
    private final Set<DayOfWeek> dias;
    private final LocalTime inicio;
    private final LocalTime fin; // exclusivo
    private boolean activa = true;

    public PromocionHorario(String nombre, double porcentaje, Set<DayOfWeek> dias, LocalTime inicio, LocalTime fin) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("La promoción necesita un nombre");
        }
        if (porcentaje <= 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 1 y 100");
        }
        if (dias == null || dias.isEmpty()) {
            throw new IllegalArgumentException("Selecciona al menos un día");
        }
        if (!inicio.isBefore(fin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora final");
        }
        this.nombre = nombre.trim();
        this.porcentaje = porcentaje;
        this.dias = EnumSet.copyOf(dias);
        this.inicio = inicio;
        this.fin = fin;
    }

    public boolean estaVigente(LocalDateTime momento) {
        LocalTime hora = momento.toLocalTime();
        return activa && dias.contains(momento.getDayOfWeek())
                && !hora.isBefore(inicio) && hora.isBefore(fin);
    }

    @Override
    public double aplicar(double subtotal) {
        return subtotal * (1 - porcentaje / 100.0);
    }

    @Override
    public String getNombre() {
        String pct = porcentaje == Math.floor(porcentaje) ? String.valueOf((int) porcentaje) : String.valueOf(porcentaje);
        return nombre + " (" + pct + "%)";
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getDiasTexto() {
        if (dias.size() == 7) {
            return "Todos los días";
        }
        List<String> nombres = new ArrayList<>();
        for (DayOfWeek d : dias) {
            nombres.add(d.getDisplayName(TextStyle.SHORT, ES));
        }
        return String.join(", ", nombres);
    }

    public String getHorarioTexto() {
        if (inicio.equals(LocalTime.MIN) && fin.equals(LocalTime.MAX)) {
            return "Todo el día";
        }
        String fFin = fin.equals(LocalTime.MAX) ? "24:00" : fin.toString();
        return inicio + " - " + fFin;
    }
}

