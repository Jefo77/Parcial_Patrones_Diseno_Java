package pedidos;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
public abstract class EstacionRol implements IObservadorPedido {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String nombre;
    private final List<Consumer<String>> receptores = new ArrayList<>();

    protected EstacionRol(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarReceptor(Consumer<String> receptor) {
        receptores.add(receptor);
    }

    protected void emitir(String mensaje) {
        String texto = "[" + LocalTime.now().format(HORA) + "] " + mensaje;
        for (Consumer<String> receptor : receptores) {
            receptor.accept(texto);
        }
    }

    @Override
    public abstract void actualizar(Pedido pedido);

    public abstract boolean puedeCrearPedidos();

    public abstract boolean puedeAvanzarA(EstadoPedido nuevoEstado);

    public abstract boolean puedeAdministrar();
}
