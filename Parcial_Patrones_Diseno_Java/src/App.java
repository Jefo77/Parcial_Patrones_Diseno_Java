import productos.productosBuilder.Combo;
import productos.productosBuilder.ComboBuilder;
import productos.productosBuilder.ComboDirector;
import productos.productosDecorator.BebidaBase;
import productos.productosDecorator.ExtraLeche;
import productos.productosDecorator.ExtraQueso;
import productos.productosDecorator.IProducto;
import productos.productosDecorator.PlatoBase;

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
    }
}