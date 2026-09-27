package rentamovil;

import java.util.Locale;

/** Resultado inmutable de consultar el costo de un posible alquiler. */
public final class Cotizacion {
    private final String placa;
    private final String descripcionVehiculo;
    private final boolean disponible;
    private final int dias;
    private final double total;

    public Cotizacion(Vehiculo vehiculo, int dias, double total) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo de la cotizacion es obligatorio.");
        }
        Vehiculo.validarDias(dias);
        this.placa = vehiculo.getPlaca();
        this.descripcionVehiculo = vehiculo.descripcionCompleta();
        this.disponible = vehiculo.isDisponible();
        this.dias = dias;
        this.total = total;
    }

    public String getPlaca() {
        return placa;
    }

    public String getDescripcionVehiculo() {
        return descripcionVehiculo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public int getDias() {
        return dias;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s%nDias: %d%nTotal: Q%.2f", descripcionVehiculo, dias, total);
    }
}

