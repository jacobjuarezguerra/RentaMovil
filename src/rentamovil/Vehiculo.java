package rentamovil;

import java.util.Locale;

/**
 * Representa los datos y comportamientos que comparten todos los vehiculos.
 */
public abstract class Vehiculo {
    private final String placa;
    private final String marca;
    private final String modelo;
    private final double tarifaDiaria;
    private boolean disponible;

    protected Vehiculo(String placa, String marca, String modelo, double tarifaDiaria) {
        this.placa = normalizarPlaca(placa);
        this.marca = validarTexto(marca, "La marca");
        this.modelo = validarTexto(modelo, "El modelo");
        this.tarifaDiaria = validarPositivo(tarifaDiaria, "La tarifa diaria");
        this.disponible = true;
    }

    public static String normalizarPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacia.");
        }
        return placa.trim().toUpperCase(Locale.ROOT);
    }

    protected static String validarTexto(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no puede estar vacio(a).");
        }
        return valor.trim();
    }

    protected static double validarPositivo(double valor, String nombreCampo) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(nombreCampo + " debe ser mayor que cero.");
        }
        return valor;
    }

    protected static int validarEnteroPositivo(int valor, String nombreCampo) {
        if (valor <= 0) {
            throw new IllegalArgumentException(nombreCampo + " debe ser mayor que cero.");
        }
        return valor;
    }

    public final double costoBase(int dias) {
        validarDias(dias);
        return tarifaDiaria * dias;
    }

    public static void validarDias(int dias) {
        validarEnteroPositivo(dias, "La cantidad de dias");
    }

    public final void alquilar() {
        if (!disponible) {
            throw new IllegalStateException("El vehiculo con placa " + placa + " ya esta alquilado.");
        }
        disponible = false;
    }

    public final void devolver() {
        if (disponible) {
            throw new IllegalStateException("El vehiculo con placa " + placa + " ya esta disponible.");
        }
        disponible = true;
    }

    public abstract double calcularCosto(int dias);

    public abstract CategoriaVehiculo getCategoria();

    /** Devuelve solamente las caracteristicas propias de la subclase. */
    protected abstract String detalleEspecifico();

    public final String descripcionCompleta() {
        return String.format(
                Locale.US,
                "%s | placa=%s | %s %s | tarifa=Q%.2f | %s | %s",
                getCategoria().getNombre(), placa, marca, modelo, tarifaDiaria,
                detalleEspecifico(), disponible ? "Disponible" : "Alquilado");
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public boolean isDisponible() {
        return disponible;
    }
}

