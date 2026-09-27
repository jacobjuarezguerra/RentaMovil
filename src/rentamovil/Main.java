package rentamovil;

import java.util.Locale;
import java.util.Scanner;

/** Punto de entrada y capa de interaccion por consola. */
public final class Main {
    private final Scanner scanner;
    private final FlotaRentaMovil flota;

    public Main(Scanner scanner, FlotaRentaMovil flota) {
        this.scanner = scanner;
        this.flota = flota;
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        FlotaRentaMovil flota = crearFlotaDemostracion();
        new Main(new Scanner(System.in), flota).ejecutar();
    }

    public static FlotaRentaMovil crearFlotaDemostracion() {
        FlotaRentaMovil flota = new FlotaRentaMovil();
        flota.registrarVehiculo(new Automovil("A001", "Toyota", "Corolla", 300.0, 5, true));
        flota.registrarVehiculo(new Automovil("A002", "Honda", "Civic", 280.0, 5, false));
        flota.registrarVehiculo(new Motocicleta("M001", "Yamaha", "FZ", 150.0, 150));
        flota.registrarVehiculo(new Motocicleta("M002", "Kawasaki", "Ninja", 250.0, 300));
        flota.registrarVehiculo(new CamionCarga("C001", "Isuzu", "NPR", 200.0, 1.5));
        flota.registrarVehiculo(new CamionCarga("C002", "Hino", "300", 300.0, 3.0));
        return flota;
    }

    public void ejecutar() {
        System.out.println("====================================");
        System.out.println("       BIENVENIDO A RENTAMOVIL");
        System.out.println("====================================");

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1 -> registrarVehiculo();
                    case 2 -> consultarFlota();
                    case 3 -> cotizar();
                    case 4 -> alquilar();
                    case 5 -> devolver();
                    case 6 -> mostrarReporte();
                    case 0 -> continuar = false;
                    default -> System.out.println("Opcion inexistente. Intente de nuevo.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Operacion rechazada: " + ex.getMessage());
            }
            System.out.println();
        }
        System.out.println("Gracias por utilizar RentaMovil.");
    }

    private void mostrarMenu() {
        System.out.println("1. Registrar vehiculo");
        System.out.println("2. Consultar flota");
        System.out.println("3. Cotizar alquiler");
        System.out.println("4. Confirmar alquiler");
        System.out.println("5. Registrar devolucion");
        System.out.println("6. Reporte general");
        System.out.println("0. Salir");
    }

    private void registrarVehiculo() {
        System.out.println("Categorias: 1=Automovil, 2=Motocicleta, 3=Camioneta de carga");
        int categoria = leerEntero("Categoria: ");
        if (categoria < 1 || categoria > 3) {
            throw new IllegalArgumentException("La categoria indicada no existe.");
        }

        String placa = leerTexto("Placa: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        double tarifa = leerDoublePositivo("Tarifa diaria: Q");
        Vehiculo vehiculo;

        if (categoria == 1) {
            int pasajeros = leerEnteroPositivo("Cantidad de pasajeros: ");
            boolean automatico = leerSiNo("¿La transmision es automatica? (S/N): ");
            vehiculo = new Automovil(placa, marca, modelo, tarifa, pasajeros, automatico);
        } else if (categoria == 2) {
            int cilindraje = leerEnteroPositivo("Cilindraje en cc: ");
            vehiculo = new Motocicleta(placa, marca, modelo, tarifa, cilindraje);
        } else {
            double capacidad = leerDoublePositivo("Capacidad maxima en toneladas: ");
            vehiculo = new CamionCarga(placa, marca, modelo, tarifa, capacidad);
        }

        flota.registrarVehiculo(vehiculo);
        System.out.println("Vehiculo registrado correctamente y disponible para alquiler.");
    }

    private void consultarFlota() {
        System.out.println("--- FLOTA REGISTRADA ---");
        if (flota.consultarFlota().isEmpty()) {
            System.out.println("No hay vehiculos registrados.");
            return;
        }
        for (Vehiculo vehiculo : flota.consultarFlota()) {
            System.out.println(vehiculo.descripcionCompleta());
        }
    }

    private void cotizar() {
        String placa = leerTexto("Placa del vehiculo: ");
        int dias = leerEnteroPositivo("Dias de alquiler: ");
        Cotizacion cotizacion = flota.cotizar(placa, dias);
        System.out.println("--- COTIZACION (no modifica el estado) ---");
        System.out.println(cotizacion);
    }

    private void alquilar() {
        String placa = leerTexto("Placa del vehiculo: ");
        int dias = leerEnteroPositivo("Dias de alquiler: ");
        Cotizacion cotizacion = flota.cotizar(placa, dias);
        System.out.println("--- TOTAL ANTES DE CONFIRMAR ---");
        System.out.println(cotizacion);

        if (!cotizacion.isDisponible()) {
            throw new IllegalStateException("El vehiculo esta ocupado y no se puede alquilar.");
        }
        if (!leerSiNo("¿Confirma el alquiler y el cobro? (S/N): ")) {
            System.out.println("Alquiler cancelado. No se modifico la disponibilidad ni los ingresos.");
            return;
        }

        Cotizacion confirmada = flota.confirmarAlquiler(placa, dias);
        System.out.printf(Locale.US, "Alquiler confirmado. Ingreso registrado: Q%.2f%n", confirmada.getTotal());
    }

    private void devolver() {
        String placa = leerTexto("Placa del vehiculo devuelto: ");
        flota.registrarDevolucion(placa);
        System.out.println("Devolucion registrada. El vehiculo vuelve a estar disponible.");
    }

    private void mostrarReporte() {
        ReporteFlota reporte = flota.generarReporte();
        System.out.println("--- REPORTE GENERAL ---");
        System.out.printf("Registrados: %d | Disponibles: %d | Alquilados: %d%n",
                reporte.getTotalRegistrados(), reporte.getTotalDisponibles(), reporte.getTotalAlquilados());
        for (CategoriaVehiculo categoria : CategoriaVehiculo.values()) {
            ConteoCategoria conteo = reporte.getConteos().get(categoria);
            System.out.printf("%s: %d registrados, %d disponibles, %d alquilados%n",
                    categoria.getNombre(), conteo.getRegistrados(),
                    conteo.getDisponibles(), conteo.getAlquilados());
        }
        System.out.printf(Locale.US, "Ingresos acumulados: Q%.2f%n", reporte.getIngresosAcumulados());
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException ex) {
                System.out.println("Entrada invalida: debe escribir un numero entero.");
            }
        }
    }

    private int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor > 0) {
                return valor;
            }
            System.out.println("Entrada invalida: el valor debe ser mayor que cero.");
        }
    }

    private double leerDoublePositivo(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje).replace(',', '.');
            try {
                double valor = Double.parseDouble(entrada);
                if (Double.isFinite(valor) && valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException ignored) {
                // El mensaje comun se muestra abajo.
            }
            System.out.println("Entrada invalida: escriba un numero mayor que cero.");
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje);
            if (respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("SI")) {
                return true;
            }
            if (respuesta.equalsIgnoreCase("N") || respuesta.equalsIgnoreCase("NO")) {
                return false;
            }
            System.out.println("Entrada invalida: responda S o N.");
        }
    }
}

