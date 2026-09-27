package rentamovil;

/** Pruebas ejecutables sin dependencias externas. */
public final class PruebasRentaMovil {
    private static int aprobadas;
    private static int fallidas;

    private PruebasRentaMovil() {
    }

    public static void main(String[] args) {
        probarCalculos();
        probarValidaciones();
        probarOperacionesEIngresos();
        probarReporte();

        System.out.printf("%nRESULTADO: %d aprobadas, %d fallidas%n", aprobadas, fallidas);
        if (fallidas > 0) {
            throw new AssertionError("Existen pruebas fallidas.");
        }
    }

    private static void probarCalculos() {
        verificarDouble("Automovil automatico: Q50 diarios", 700.0,
                new Automovil("T1", "Toyota", "Corolla", 300, 5, true).calcularCosto(2));
        verificarDouble("Automovil manual: sin recargo", 840.0,
                new Automovil("T2", "Honda", "Civic", 280, 5, false).calcularCosto(3));
        verificarDouble("Motocicleta de 250 cc: sin recargo", 600.0,
                new Motocicleta("T3", "Yamaha", "FZ", 150, 250).calcularCosto(4));
        verificarDouble("Motocicleta mayor de 250 cc: Q75 una vez", 575.0,
                new Motocicleta("T4", "Kawasaki", "Ninja", 250, 300).calcularCosto(2));
        verificarDouble("Camioneta decimal: ejemplo del enunciado", 1050.0,
                new CamionCarga("T5", "Isuzu", "NPR", 200, 1.5).calcularCosto(3));
    }

    private static void probarValidaciones() {
        esperarExcepcion("Rechaza placa vacia", IllegalArgumentException.class,
                () -> new Motocicleta("  ", "Marca", "Modelo", 100, 100));
        esperarExcepcion("Rechaza tarifa no positiva", IllegalArgumentException.class,
                () -> new Automovil("V1", "Marca", "Modelo", 0, 4, false));
        esperarExcepcion("Rechaza pasajeros no positivos", IllegalArgumentException.class,
                () -> new Automovil("V2", "Marca", "Modelo", 100, -1, false));
        esperarExcepcion("Rechaza cilindraje no positivo", IllegalArgumentException.class,
                () -> new Motocicleta("V3", "Marca", "Modelo", 100, 0));
        esperarExcepcion("Rechaza capacidad no positiva", IllegalArgumentException.class,
                () -> new CamionCarga("V4", "Marca", "Modelo", 100, 0));
        esperarExcepcion("Rechaza dias no positivos", IllegalArgumentException.class,
                () -> new Motocicleta("V5", "Marca", "Modelo", 100, 100).calcularCosto(0));

        FlotaRentaMovil flota = new FlotaRentaMovil();
        flota.registrarVehiculo(new Motocicleta("dup1", "Marca", "Modelo", 100, 100));
        esperarExcepcion("Rechaza placa repetida sin distinguir mayusculas", IllegalArgumentException.class,
                () -> flota.registrarVehiculo(new Motocicleta(" DUP1 ", "Otra", "Moto", 120, 125)));
        esperarExcepcion("Rechaza placa inexistente", IllegalArgumentException.class,
                () -> flota.cotizar("NO-EXISTE", 1));
    }

    private static void probarOperacionesEIngresos() {
        FlotaRentaMovil flota = new FlotaRentaMovil();
        flota.registrarVehiculo(new CamionCarga("C001", "Isuzu", "NPR", 200, 1.5));

        Cotizacion primera = flota.cotizar("c001", 3);
        verificarDouble("Cotizar calcula el total esperado", 1050.0, primera.getTotal());
        verificarDouble("Cotizar no registra ingresos", 0.0, flota.getIngresosAcumulados());
        verificar("Cotizar no cambia disponibilidad", flota.buscarVehiculo("C001").isDisponible());

        // Una cancelacion en la interfaz equivale a no llamar confirmarAlquiler.
        flota.cotizar("C001", 2);
        verificarDouble("Cancelar despues de cotizar no cambia ingresos", 0.0, flota.getIngresosAcumulados());

        flota.confirmarAlquiler("C001", 3);
        verificarDouble("Confirmar registra el ingreso una sola vez", 1050.0, flota.getIngresosAcumulados());
        verificar("Confirmar cambia el vehiculo a ocupado", !flota.buscarVehiculo("C001").isDisponible());
        verificar("Se puede cotizar un vehiculo ocupado", !flota.cotizar("C001", 1).isDisponible());
        verificarDouble("Cotizar ocupado tampoco cambia ingresos", 1050.0, flota.getIngresosAcumulados());
        esperarExcepcion("Rechaza alquilar un vehiculo ocupado", IllegalStateException.class,
                () -> flota.confirmarAlquiler("C001", 1));

        flota.registrarDevolucion("C001");
        verificar("Devolver restaura disponibilidad", flota.buscarVehiculo("C001").isDisponible());
        verificarDouble("Devolver conserva los ingresos", 1050.0, flota.getIngresosAcumulados());
        esperarExcepcion("Rechaza devolver un vehiculo disponible", IllegalStateException.class,
                () -> flota.registrarDevolucion("C001"));
    }

    private static void probarReporte() {
        FlotaRentaMovil flota = Main.crearFlotaDemostracion();
        flota.confirmarAlquiler("A001", 2);
        ReporteFlota reporte = flota.generarReporte();
        verificarEntero("Reporte: total registrado", 6, reporte.getTotalRegistrados());
        verificarEntero("Reporte: total disponible", 5, reporte.getTotalDisponibles());
        verificarEntero("Reporte: total alquilado", 1, reporte.getTotalAlquilados());
        ConteoCategoria autos = reporte.getConteos().get(CategoriaVehiculo.AUTOMOVIL);
        verificarEntero("Reporte por categoria: automoviles registrados", 2, autos.getRegistrados());
        verificarEntero("Reporte por categoria: automoviles alquilados", 1, autos.getAlquilados());
        verificarDouble("Reporte: ingreso acumulado", 700.0, reporte.getIngresosAcumulados());
    }

    private static void verificar(String nombre, boolean condicion) {
        if (condicion) {
            aprobadas++;
            System.out.println("[OK] " + nombre);
        } else {
            fallidas++;
            System.out.println("[FALLO] " + nombre);
        }
    }

    private static void verificarEntero(String nombre, int esperado, int obtenido) {
        verificar(nombre + " (esperado=" + esperado + ", obtenido=" + obtenido + ")", esperado == obtenido);
    }

    private static void verificarDouble(String nombre, double esperado, double obtenido) {
        verificar(nombre + String.format(" (esperado=Q%.2f, obtenido=Q%.2f)", esperado, obtenido),
                Math.abs(esperado - obtenido) < 0.0001);
    }

    private static void esperarExcepcion(String nombre, Class<? extends Throwable> tipo, Accion accion) {
        try {
            accion.ejecutar();
            verificar(nombre, false);
        } catch (Throwable ex) {
            verificar(nombre, tipo.isInstance(ex));
        }
    }

    @FunctionalInterface
    private interface Accion {
        void ejecutar();
    }
}

