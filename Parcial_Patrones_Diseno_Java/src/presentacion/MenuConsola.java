package presentacion;

import datos.BaseDatosMemoria;
import datos.IRepositorioPedidos;
import java.util.Scanner;

public class MenuConsola {
    private Scanner scanner;
    private ControladorPedidos controlador;

    public MenuConsola(ControladorPedidos controlador) {
        this.scanner = new Scanner(System.in);
        this.controlador = controlador;
    }

    public void iniciar() {

    }

    private void mostrarMenuPrincipal() {

    }

    private void capturarPedido() {

    }

    public static void main(String[] args) {
        IRepositorioPedidos repo = new BaseDatosMemoria();
        ControladorPedidos controlador = new ControladorPedidos(repo);
        MenuConsola menu = new MenuConsola(controlador);
        menu.iniciar();
    }
}