import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
public class InventarioApp {
static ArrayList<String> nombres = new ArrayList<>();
    static double[] precios = new double[0];
    static HashMap<String, Integer> stock = new HashMap<>();


    static double totalCompras = 0;

    public static void main(String[] args) {
        boolean ejecutando = true;

        while (ejecutando) {
            String opcion = JOptionPane.showInputDialog(null,
                    "Seleccione una opción:\n" +
                            "1. Agregar producto\n" +
                            "2. Listar inventario\n" +
                            "3. Comprar producto\n" +
                            "4. Mostrar estadísticas\n" +
                            "5. Buscar producto\n" +
                            "6. Salir",
                    "Menú Principal",
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion == null) break;

            switch (opcion) {
                case "1":
                    agregarProducto();
                    break;
                case "2":
                    listarInventario();
                    break;
                case "3":
                    comprarProducto();
                    break;
                case "4":
                    mostrarEstadisticas();
                    break;
                case "5":
                    buscarProducto();
                    break;
                case "6":
                    mostrarTicketFinal();
                    ejecutando = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
            }
        }
    }

   

    static void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog("Ingrese nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nombre no puede estar vacío.");
                return;
            }

            if (nombres.contains(nombre)) {
                JOptionPane.showMessageDialog(null, "Producto ya existe.");
                return;
            }

            String precioStr = JOptionPane.showInputDialog("Ingrese precio:");
            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                JOptionPane.showMessageDialog(null, "Precio debe ser mayor que 0.");
                return;
            }

            String stockStr = JOptionPane.showInputDialog("Ingrese cantidad en stock:");
            int cantidad = Integer.parseInt(stockStr);
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(null, "Stock no puede ser negativo.");
                return;
            }

  
            nombres.add(nombre);
            expandPrecios(precio);
            stock.put(nombre, cantidad);

            JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: formato numérico inválido.");
        }
    }

    static void expandPrecios(double nuevoPrecio) {
        double[] nuevoArray = new double[precios.length + 1];
        for (int i = 0; i < precios.length; i++) {
            nuevoArray[i] = precios[i];
        }
        nuevoArray[precios.length] = nuevoPrecio;
        precios = nuevoArray;
    }

    static int indexOfNombre(String nombre) {
        return nombres.indexOf(nombre);
    }

 

    static void listarInventario() {
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inventario vacío.");
            return;
        }

        StringBuilder sb = new StringBuilder("Inventario:\n");
        for (int i = 0; i < nombres.size(); i++) {
            String nombre = nombres.get(i);
            double precio = precios[i];
            int cantidad = stock.get(nombre);
            sb.append(String.format("%d. %s - $%.2f - Stock: %d\n", i + 1, nombre, precio, cantidad));
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    static void comprarProducto() {
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos para comprar.");
            return;
        }

        String nombre = JOptionPane.showInputDialog("Ingrese nombre del producto a comprar:");
        if (!nombres.contains(nombre)) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.");
            return;
        }

        int index = indexOfNombre(nombre);
        int disponible = stock.get(nombre);

        try {
            String cantidadStr = JOptionPane.showInputDialog("Ingrese cantidad a comprar:");
            int cantidad = Integer.parseInt(cantidadStr);

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad inválida.");
                return;
            }

            if (cantidad > disponible) {
                JOptionPane.showMessageDialog(null, "Stock insuficiente. Disponible: " + disponible);
                return;
            }

            double costo = cantidad * precios[index];
            int nuevoStock = disponible - cantidad;
            stock.put(nombre, nuevoStock);
            totalCompras += costo;

            JOptionPane.showMessageDialog(null, String.format("Compra exitosa: %d x %s = $%.2f", cantidad, nombre, costo));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: formato numérico inválido.");
        }
    }

    static void mostrarEstadisticas() {
        if (precios.length == 0) {
            JOptionPane.showMessageDialog(null, "No hay productos para analizar.");
            return;
        }

        double min = precios[0];
        double max = precios[0];
        String prodMin = nombres.get(0);
        String prodMax = nombres.get(0);

        for (int i = 1; i < precios.length; i++) {
            if (precios[i] < min) {
                min = precios[i];
                prodMin = nombres.get(i);
            }
            if (precios[i] > max) {
                max = precios[i];
                prodMax = nombres.get(i);
            }
        }

        String mensaje = String.format("Producto más barato: %s ($%.2f)\nProducto más caro: %s ($%.2f)",
                prodMin, min, prodMax, max);
        JOptionPane.showMessageDialog(null, mensaje);
    }

    static void buscarProducto() {
        String termino = JOptionPane.showInputDialog("Ingrese nombre o parte del nombre:");
        if (termino == null || termino.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Entrada vacía.");
            return;
        }

        StringBuilder sb = new StringBuilder("Resultados de búsqueda:\n");
        boolean encontrado = false;

        for (int i = 0; i < nombres.size(); i++) {
            String nombre = nombres.get(i);
            if (nombre.toLowerCase().contains(termino.toLowerCase())) {
                sb.append(String.format("%s - $%.2f - Stock: %d\n", nombre, precios[i], stock.get(nombre)));
                encontrado = true;
            }
        }

        if (!encontrado) {
            sb.append("No se encontraron coincidencias.");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    static void mostrarTicketFinal() {
        String mensaje = String.format("Gracias por usar el sistema.\nTotal de compras: $%.2f", totalCompras);
        JOptionPane.showMessageDialog(null, mensaje);
    }
}