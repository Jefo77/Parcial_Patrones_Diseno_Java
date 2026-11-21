package pedidos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import productos.decorator.IProducto;
public class Pedido {

    public static String dinero(double valor) {
        return String.format(Locale.forLanguageTag("es-CO"), "$%,.0f", valor);
    }

    private final int idPedido;
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    private final List<ItemPedido> items = new ArrayList<>();
    private final List<IObservadorPedido> observadores = new ArrayList<>();
    private final LocalDateTime fechaCreacion = LocalDateTime.now();
    private boolean confirmado;
    private boolean pagado;
    private String metodoPago;
    private LocalDateTime fechaPago;
    private double descuento;

    public Pedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void agregarProducto(IProducto producto, int cantidad) {
        verificarEditable();
        if (producto == null) {
            throw new IllegalArgumentException("Producto no válido");
        }
        validarCantidad(cantidad);
        for (ItemPedido item : items) {
            if (item.getProducto().getDescripcion().equals(producto.getDescripcion())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemPedido(producto, cantidad));
    }

    public void modificarCantidad(int indice, int cantidad) {
        verificarEditable();
        validarIndice(indice);
        validarCantidad(cantidad);
        items.get(indice).setCantidad(cantidad);
    }

    public void eliminarItem(int indice) {
        verificarEditable();
        validarIndice(indice);
        items.remove(indice);
    }

    public void confirmar() {
        verificarEditable();
        if (items.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un pedido vacío");
        }
        confirmado = true;
        notificarObservadores();
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (!confirmado) {
            throw new IllegalStateException("El pedido aún no ha sido confirmado");
        }
        if (nuevoEstado != getSiguienteEstado()) {
            throw new IllegalStateException("El estado debe seguir el orden: PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO");
        }
        estado = nuevoEstado;
        notificarObservadores();
    }

    /** @return el siguiente estado del proceso, o null si ya está ENTREGADO. */
    public EstadoPedido getSiguienteEstado() {
        EstadoPedido[] todos = EstadoPedido.values();
        return estado.ordinal() < todos.length - 1 ? todos[estado.ordinal() + 1] : null;
    }

    public void registrarPago(String metodo) {
        if (estado != EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("Solo se pueden cobrar pedidos ENTREGADOS");
        }
        if (pagado) {
            throw new IllegalStateException("El pedido #" + idPedido + " ya fue cobrado");
        }
        if (metodo == null || !(metodo.equalsIgnoreCase("EFECTIVO") || metodo.equalsIgnoreCase("TARJETA"))) {
            throw new IllegalArgumentException("Método de pago no válido (EFECTIVO o TARJETA)");
        }
        pagado = true;
        metodoPago = metodo.toUpperCase();
        fechaPago = LocalDateTime.now();
    }

    public void agregarObservador(IObservadorPedido obs) {
        if (obs != null && !observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    public void eliminarObservador(IObservadorPedido obs) {
        observadores.remove(obs);
    }

    private void notificarObservadores() {
        for (IObservadorPedido obs : observadores) {
            obs.actualizar(this);
        }
    }

    public double getSubtotal() {
        double suma = 0;
        for (ItemPedido item : items) {
            suma += item.getSubtotal();
        }
        return suma;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal() {
        return getSubtotal() - descuento;
    }

    public int getCantidadTotalItems() {
        int total = 0;
        for (ItemPedido item : items) {
            total += item.getCantidad();
        }
        return total;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<ItemPedido> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public boolean isCerrado() {
        return pagado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    private void verificarEditable() {
        if (confirmado) {
            throw new IllegalStateException("El pedido #" + idPedido + " ya fue confirmado y no se puede modificar");
        }
    }

    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= items.size()) {
            throw new IllegalArgumentException("Selecciona un ítem válido del pedido");
        }
    }
}
