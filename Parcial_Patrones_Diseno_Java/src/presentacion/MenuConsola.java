package presentacion;

import datos.BaseDatosMemoria;
import java.util.List;
import java.util.Scanner;
import pedidos.EstacionRol;
import pedidos.ItemPedido;
import pedidos.Pedido;
import productos.decorator.IProducto;
import servicios.PromocionHorario;

public class MenuConsola {

    private final ControladorPedidos ctrl;
    private final Scanner scanner = new Scanner(System.in);

    public MenuConsola(ControladorPedidos ctrl) {
        this.ctrl = ctrl;
        for (EstacionRol estacion : ctrl.getEstaciones()) {
            estacion.agregarReceptor(msg -> System.out.println("   >> AVISO " + estacion.getNombre().toUpperCase() + ": " + msg));
        }
    }

    public static void main(String[] args) {
        BaseDatosMemoria db = new BaseDatosMemoria();
        new MenuConsola(new ControladorPedidos(db, db)).iniciar();
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n=========== SISTEMA DE PEDIDOS (rol activo: " + ctrl.getRolActivo().getNombre() + ") ===========");
            System.out.println(" 1. Ver menú");
            System.out.println(" 2. Agregar producto al pedido (con adiciones)");
            System.out.println(" 3. Armar combo paso a paso");
            System.out.println(" 4. Ver / modificar pedido en curso");
            System.out.println(" 5. Confirmar pedido");
            System.out.println(" 6. Ver pedidos y avanzar estado");
            System.out.println(" 7. Cobrar pedido");
            System.out.println(" 8. Promociones");
            System.out.println(" 9. Cierre de caja");
            System.out.println("10. Cambiar rol");
            System.out.println(" 0. Salir");
            opcion = leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> verMenu(null);
                    case 2 -> agregarProducto();
                    case 3 -> armarCombo();
                    case 4 -> verPedidoActual(true);
                    case 5 -> {
                        Pedido p = ctrl.confirmarPedido();
                        System.out.println("Pedido #" + p.getIdPedido() + " confirmado. Total: " + Pedido.dinero(p.getTotal()));
                    }
                    case 6 -> pedidos();
                    case 7 -> cobrar();
                    case 8 -> promociones();
                    case 9 -> System.out.println(ctrl.cierreDeCaja());
                    case 10 -> cambiarRol();
                    case 0 -> System.out.println("Hasta pronto.");
                    default -> System.out.println("Opción no válida. Intenta de nuevo.");
                }
            } catch (RuntimeException ex) {
                System.out.println("AVISO: " + ex.getMessage());
            }
        } while (opcion != 0);
    }

    private void verMenu(String categoria) {
        System.out.printf("%-5s %-24s %-15s %s%n", "ID", "PRODUCTO", "CATEGORÍA", "PRECIO");
        for (IProducto p : ctrl.listarMenu(categoria)) {
            System.out.printf("%-5s %-24s %-15s %s%n", p.getId(), p.getDescripcion(), p.getCategoria(), Pedido.dinero(p.getPrecio()));
        }
    }

    private void agregarProducto() {
        verMenu(null);
        String id = leerTexto("ID del producto");
        int cantidad = leerEntero("Cantidad");
        int leche = leerEntero("Extras de leche (0 = ninguno)");
        int queso = leerEntero("Extras de queso (0 = ninguno)");
        ctrl.agregarProducto(id, cantidad, leche, queso);
        verPedidoActual(false);
    }

    private void armarCombo() {
        System.out.println("--- Paso 1: plato principal (obligatorio) ---");
        verMenu("PLATO");
        String plato = leerTexto("ID del plato");
        System.out.println("--- Paso 2: bebida (obligatoria) ---");
        verMenu("BEBIDA");
        String bebida = leerTexto("ID de la bebida");
        System.out.println("--- Paso 3: acompañamiento (Enter para omitir) ---");
        verMenu("ACOMPANAMIENTO");
        String acomp = leerTexto("ID del acompañamiento");
        System.out.println("--- Paso 4: postre (Enter para omitir) ---");
        verMenu("POSTRE");
        String postre = leerTexto("ID del postre");
        int cantidad = leerEntero("Cantidad de combos");
        ctrl.armarCombo(plato, bebida, acomp, postre, cantidad);
        verPedidoActual(false);
    }

    private void verPedidoActual(boolean permitirEditar) {
        Pedido p = ctrl.getPedidoActual();
        if (p == null) {
            System.out.println("No hay un pedido en curso.");
            return;
        }
        System.out.println("--- Pedido en curso #" + p.getIdPedido() + " ---");
        int i = 1;
        for (ItemPedido item : p.getItems()) {
            System.out.printf("%d) %d x %s  (%s c/u) = %s%n", i++, item.getCantidad(), item.getProducto().getDescripcion(),
                    Pedido.dinero(item.getProducto().getPrecio()), Pedido.dinero(item.getSubtotal()));
        }
        System.out.println("Promoción: " + ctrl.getNombrePromocionAplicada());
        System.out.println("Subtotal : " + Pedido.dinero(p.getSubtotal()));
        System.out.println("Descuento: " + Pedido.dinero(p.getDescuento()));
        System.out.println("TOTAL    : " + Pedido.dinero(p.getTotal()));
        if (permitirEditar) {
            System.out.println("a) Cambiar cantidad  b) Eliminar ítem  (otra tecla: volver)");
            String op = leerTexto("Opción");
            if (op.equalsIgnoreCase("a")) {
                int idx = leerEntero("Número de ítem");
                int cant = leerEntero("Nueva cantidad");
                ctrl.modificarCantidad(idx - 1, cant);
                verPedidoActual(false);
            } else if (op.equalsIgnoreCase("b")) {
                ctrl.eliminarItem(leerEntero("Número de ítem") - 1);
                verPedidoActual(false);
            }
        }
    }

    private void pedidos() {
        List<Pedido> lista = ctrl.listarPedidos();
        if (lista.isEmpty()) {
            System.out.println("Aún no hay pedidos confirmados.");
            return;
        }
        for (Pedido p : lista) {
            System.out.println("#" + p.getIdPedido() + "  " + p.getEstado() + "  " + p.getCantidadTotalItems()
                    + " unidad(es)  " + Pedido.dinero(p.getTotal()) + (p.isCerrado() ? "  [COBRADO]" : ""));
        }
        int id = leerEntero("ID del pedido a avanzar (0 = volver)");
        if (id > 0) {
            Pedido p = ctrl.avanzarEstado(id);
            System.out.println("Pedido #" + id + " ahora está " + p.getEstado());
        }
    }

    private void cobrar() {
        List<Pedido> porCobrar = ctrl.pedidosPorCobrar();
        if (porCobrar.isEmpty()) {
            System.out.println("No hay pedidos ENTREGADOS pendientes de cobro.");
            return;
        }
        for (Pedido p : porCobrar) {
            System.out.println("#" + p.getIdPedido() + "  " + Pedido.dinero(p.getTotal()));
        }
        int id = leerEntero("ID del pedido");
        String metodo = leerTexto("Método (EFECTIVO / TARJETA)");
        ctrl.cobrarPedido(id, metodo);
        System.out.println("Pedido #" + id + " cobrado y cerrado.");
    }

    private void promociones() {
        List<PromocionHorario> lista = ctrl.getPromociones();
        int i = 1;
        for (PromocionHorario p : lista) {
            System.out.println(i++ + ") " + p.getNombre() + " | " + p.getDiasTexto() + " | " + p.getHorarioTexto()
                    + " | " + (p.isActiva() ? "ACTIVA" : "INACTIVA"));
        }
        int n = leerEntero("Número de promoción para activar/desactivar (0 = volver)");
        if (n > 0 && n <= lista.size()) {
            ctrl.cambiarEstadoPromocion(n - 1, !lista.get(n - 1).isActiva());
            System.out.println("Promoción actualizada.");
        }
    }

    private void cambiarRol() {
        System.out.println("1) Mesero  2) Cocina  3) Caja");
        int op = leerEntero("Rol");
        switch (op) {
            case 1 -> ctrl.setRol("Mesero");
            case 2 -> ctrl.setRol("Cocina");
            case 3 -> ctrl.setRol("Caja");
            default -> System.out.println("Rol no válido.");
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "";
    }

    private int leerEntero(String mensaje) {
        String texto = leerTexto(mensaje);
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            System.out.println("AVISO: '" + texto + "' no es un número válido.");
            return -1;
        }
    }
}