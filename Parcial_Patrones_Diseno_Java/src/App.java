import productos.productosBuilder.Combo;
import productos.productosBuilder.ComboBuilder;
import productos.productosBuilder.ComboDirector;
import productos.productosDecorator.BebidaBase;
import productos.productosDecorator.ExtraLeche;
import productos.productosDecorator.ExtraQueso;
import productos.productosDecorator.IProducto;
import productos.productosDecorator.PlatoBase;
import pedidos.EstadoPedido;
import pedidos.EstacionCaja;
import pedidos.EstacionCocina;
import pedidos.EstacionMesero;
import pedidos.IObservadorPedido;
import pedidos.Pedido;

public class App {
    public static void main(String[] args) {
        System.out.println("PRUEBA PATRÓN DECORATOR");
        IProducto cafe = new BebidaBase("Café", 3000);
        IProducto cafeConLeche = new ExtraLeche(cafe);
        System.out.println(cafeConLeche.getDescripcion() + " = $" + cafeConLeche.getPrecio());

        IProducto sandwich = new PlatoBase("Sándwich", 8000);
        IProducto sandwichConQueso = new ExtraQueso(sandwich);
        System.out.println(sandwichConQueso.getDescripcion() + " = $" + sandwichConQueso.getPrecio());

        System.out.println("\nPRUEBA PATRÓN BUILDER");
        ComboDirector director = new ComboDirector();
        Combo combo1 = director.construirComboBasico(new ComboBuilder(), sandwichConQueso, cafeConLeche);

        if (combo1 != null) {
            System.out.println(combo1.getDescripcion());
            System.out.println("Precio total combo: $" + combo1.getPrecio());
        }

        System.out.println("\nPRUEBA VALIDACIÓN COMBO VACÍO");
        Combo comboVacio = new ComboBuilder().build();
        System.out.println("Resultado combo vacío: " + comboVacio);

        System.out.println("\nPRUEBA PATRÓN OBSERVER");
        Pedido pedido = new Pedido(1);
        pedido.agregarProducto(cafeConLeche);
        if (combo1 != null) {
            pedido.agregarProducto(combo1);
        }
        IObservadorPedido cocina = new EstacionCocina();
        IObservadorPedido mesero = new EstacionMesero();
        IObservadorPedido caja = new EstacionCaja();
        pedido.agregarObservador(cocina);
        pedido.agregarObservador(mesero);
        pedido.agregarObservador(caja);
        System.out.println("\nEstado: EN_PREPARACION");
        pedido.cambiarEstado(EstadoPedido.EN_PREPARACION);
        System.out.println("\nRepitiendo EN_PREPARACION (no debe notificar de nuevo)");
        pedido.cambiarEstado(EstadoPedido.EN_PREPARACION);
        System.out.println("\nEstado: LISTO");
        pedido.cambiarEstado(EstadoPedido.LISTO);
        System.out.println("\nCancelando COCINA y cambiando a ENTREGADO");
        pedido.eliminarObservador(cocina);
        pedido.cambiarEstado(EstadoPedido.ENTREGADO);
    }
}