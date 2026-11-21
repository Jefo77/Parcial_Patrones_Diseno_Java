package presentacion;

import datos.IRepositorioPedidos;
import datos.IRepositorioProductos;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import pedidos.EstacionCaja;
import pedidos.EstacionCocina;
import pedidos.EstacionMesero;
import pedidos.EstacionRol;
import pedidos.EstadoPedido;
import pedidos.Pedido;
import productos.builder.Combo;
import productos.builder.ComboBuilder;
import productos.builder.ComboDirector;
import productos.decorator.ExtraLeche;
import productos.decorator.ExtraQueso;
import productos.decorator.IProducto;
import servicios.CalculadoraCuenta;
import servicios.DescuentoEstudiante;
import servicios.PromocionHorario;
import servicios.ReporteCaja;
import servicios.ServicioCierreCaja;

public class ControladorPedidos {

    private final IRepositorioPedidos repoPedidos;
    private final IRepositorioProductos repoProductos;
    private final CalculadoraCuenta calculadora = new CalculadoraCuenta();
    private final ServicioCierreCaja cierreCaja;

    private final EstacionCocina cocina = new EstacionCocina();
    private final EstacionMesero mesero = new EstacionMesero();
    private final EstacionCaja caja = new EstacionCaja();
    private EstacionRol rolActivo = mesero;

    private Pedido pedidoActual;
    private boolean descuentoEstudiante;
    private LocalDateTime fechaSimulada;

    public ControladorPedidos(IRepositorioPedidos repoPedidos, IRepositorioProductos repoProductos) {
        this.repoPedidos = repoPedidos;
        this.repoProductos = repoProductos;
        this.cierreCaja = new ServicioCierreCaja(repoPedidos);
        cargarPromocionesBase();
    }

    private void cargarPromocionesBase() {
        calculadora.agregarPromocion(new PromocionHorario("Tarde feliz", 20,
                EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), LocalTime.of(14, 0), LocalTime.of(18, 0)));
        calculadora.agregarPromocion(new PromocionHorario("Fin de semana", 10,
                EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY), LocalTime.MIN, LocalTime.MAX));
    }


    public List<EstacionRol> getEstaciones() {
        return Arrays.asList(cocina, mesero, caja);
    }

    public EstacionCocina getCocina() {
        return cocina;
    }

    public EstacionMesero getMesero() {
        return mesero;
    }

    public EstacionCaja getCaja() {
        return caja;
    }

    public EstacionRol getRolActivo() {
        return rolActivo;
    }

    public void setRol(String nombre) {
        if ("Mesero".equalsIgnoreCase(nombre)) {
            rolActivo = mesero;
        } else if ("Cocina".equalsIgnoreCase(nombre)) {
            rolActivo = cocina;
        } else if ("Caja".equalsIgnoreCase(nombre)) {
            rolActivo = caja;
        } else {
            throw new IllegalArgumentException("Rol no válido: " + nombre);
        }
    }

    private void exigirCrearPedidos() {
        if (!rolActivo.puedeCrearPedidos()) {
            throw new IllegalStateException("El rol " + rolActivo.getNombre() + " no puede crear ni modificar pedidos");
        }
    }

    private void exigirAdministrar(String accion) {
        if (!rolActivo.puedeAdministrar()) {
            throw new IllegalStateException("El rol " + rolActivo.getNombre() + " no tiene permiso para " + accion
                    + ". Cambia al rol Caja.");
        }
    }

    public List<IProducto> listarMenu(String categoria) {
        return repoProductos.obtenerPorCategoria(categoria);
    }

    public IProducto buscarProducto(String id) {
        return repoProductos.buscarPorId(id);
    }


    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    public Pedido agregarProducto(String idProducto, int cantidad, int extrasLeche, int extrasQueso) {
        exigirCrearPedidos();
        IProducto producto = repoProductos.buscarPorId(idProducto);
        if (producto == null) {
            throw new IllegalArgumentException("El producto '" + idProducto + "' no existe en el menú");
        }
        if (extrasLeche < 0 || extrasQueso < 0) {
            throw new IllegalArgumentException("Las adiciones no pueden ser negativas");
        }
        for (int i = 0; i < extrasLeche; i++) {
            producto = new ExtraLeche(producto);   
        }
        for (int i = 0; i < extrasQueso; i++) {
            producto = new ExtraQueso(producto);   
        }
        Pedido pedido = obtenerPedidoEditable();
        pedido.agregarProducto(producto, cantidad);
        recalcular(pedido);
        return pedido;
    }

    public Pedido armarCombo(String idPlato, String idBebida, String idAcomp, String idPostre, int cantidad) {
        exigirCrearPedidos();
        Combo combo = new ComboDirector(new ComboBuilder()).armarCombo(
                repoProductos.buscarPorId(idPlato),
                repoProductos.buscarPorId(idBebida),
                repoProductos.buscarPorId(idAcomp),
                repoProductos.buscarPorId(idPostre));
        Pedido pedido = obtenerPedidoEditable();
        pedido.agregarProducto(combo, cantidad);
        recalcular(pedido);
        return pedido;
    }

    public void modificarCantidad(int indice, int cantidad) {
        exigirCrearPedidos();
        Pedido pedido = exigirPedidoActual();
        pedido.modificarCantidad(indice, cantidad);
        recalcular(pedido);
    }

    public void eliminarItem(int indice) {
        exigirCrearPedidos();
        Pedido pedido = exigirPedidoActual();
        pedido.eliminarItem(indice);
        recalcular(pedido);
    }

    public Pedido confirmarPedido() {
        exigirCrearPedidos();
        Pedido pedido = exigirPedidoActual();
        recalcular(pedido);
        pedido.confirmar();           
        repoPedidos.guardarPedido(pedido);
        pedidoActual = null;
        return pedido;
    }

    public void descartarPedido() {
        exigirCrearPedidos();
        pedidoActual = null;
    }

    private Pedido obtenerPedidoEditable() {
        if (pedidoActual == null) {
            pedidoActual = new Pedido(repoPedidos.siguienteId());
            pedidoActual.agregarObservador(cocina);
            pedidoActual.agregarObservador(mesero);
            pedidoActual.agregarObservador(caja);
        }
        return pedidoActual;
    }

    private Pedido exigirPedidoActual() {
        if (pedidoActual == null) {
            throw new IllegalStateException("No hay un pedido en curso. Agrega un producto primero");
        }
        return pedidoActual;
    }

    private void actualizarEstrategia() {
        calculadora.setFechaHora(fechaSimulada != null ? fechaSimulada : LocalDateTime.now());
        if (descuentoEstudiante) {
            calculadora.setEstrategia(new DescuentoEstudiante());
        } else {
            calculadora.seleccionarPromocionVigente();
        }
    }

    private void recalcular(Pedido pedido) {
        actualizarEstrategia();
        pedido.setDescuento(calculadora.calcularDescuento(pedido.getSubtotal()));
    }

    public void refrescarPedidoActual() {
        if (pedidoActual != null) {
            recalcular(pedidoActual);
        }
    }

    public String getNombrePromocionAplicada() {
        actualizarEstrategia();
        return calculadora.getEstrategia().getNombre();
    }

    public void setDescuentoEstudiante(boolean activo) {
        this.descuentoEstudiante = activo;
        refrescarPedidoActual();
    }

    public void setFechaSimulada(LocalDateTime fecha) {
        this.fechaSimulada = fecha;
        refrescarPedidoActual();
    }

    public List<PromocionHorario> getPromociones() {
        return calculadora.getPromociones();
    }

    public void cambiarEstadoPromocion(int indice, boolean activa) {
        exigirAdministrar("administrar promociones");
        List<PromocionHorario> lista = calculadora.getPromociones();
        if (indice < 0 || indice >= lista.size()) {
            throw new IllegalArgumentException("Selecciona una promoción de la tabla");
        }
        lista.get(indice).setActiva(activa);
        refrescarPedidoActual();
    }

    public void crearPromocion(String nombre, double porcentaje, Set<DayOfWeek> dias, LocalTime inicio, LocalTime fin) {
        exigirAdministrar("crear promociones");
        calculadora.agregarPromocion(new PromocionHorario(nombre, porcentaje, dias, inicio, fin));
        refrescarPedidoActual();
    }

    public List<Pedido> listarPedidos() {
        return repoPedidos.obtenerPedidos();
    }

    public Pedido buscarPedido(int idPedido) {
        for (Pedido p : repoPedidos.obtenerPedidos()) {
            if (p.getIdPedido() == idPedido) {
                return p;
            }
        }
        throw new IllegalArgumentException("No existe el pedido #" + idPedido);
    }

    public Pedido avanzarEstado(int idPedido) {
        Pedido pedido = buscarPedido(idPedido);
        EstadoPedido siguiente = pedido.getSiguienteEstado();
        if (siguiente == null) {
            throw new IllegalStateException("El pedido #" + idPedido + " ya está en el estado final (ENTREGADO)");
        }
        if (!rolActivo.puedeAvanzarA(siguiente)) {
            throw new IllegalStateException("El rol " + rolActivo.getNombre() + " no puede mover un pedido a " + siguiente);
        }
        pedido.cambiarEstado(siguiente);   // notifica a las estaciones
        return pedido;
    }

    public List<Pedido> pedidosPorCobrar() {
        return cierreCaja.obtenerPedidosEntregados();
    }

    public Pedido cobrarPedido(int idPedido, String metodo) {
        exigirAdministrar("registrar pagos");
        Pedido pedido = buscarPedido(idPedido);
        pedido.registrarPago(metodo);
        return pedido;
    }

    public ReporteCaja cierreDeCaja() {
        exigirAdministrar("realizar el cierre de caja");
        return cierreCaja.generarReporte(LocalDate.now());
    }
}
