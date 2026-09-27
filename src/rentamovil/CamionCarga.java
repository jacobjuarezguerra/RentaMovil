package rentamovil;

import java.util.Locale;

/** Camioneta cuyo recargo diario depende de su capacidad maxima. */
public final class CamionCarga extends Vehiculo {
    private static final double RECARGO_POR_TONELADA_DIARIO = 100.0;

    private final double capacidadToneladas;

    public CamionCarga(String placa, String marca, String modelo, double tarifaDiaria,
                       double capacidadToneladas) {
        super(placa, marca, modelo, tarifaDiaria);
        this.capacidadToneladas = validarPositivo(capacidadToneladas, "La capacidad de carga");
    }

    @Override
    public double calcularCosto(int dias) {
        validarDias(dias);
        return costoBase(dias) + RECARGO_POR_TONELADA_DIARIO * capacidadToneladas * dias;
    }

    @Override
    public CategoriaVehiculo getCategoria() {
        return CategoriaVehiculo.CAMION_CARGA;
    }

    @Override
    protected String detalleEspecifico() {
        return String.format(Locale.US, "capacidad %.2f toneladas", capacidadToneladas);
    }

    public double getCapacidadToneladas() {
        return capacidadToneladas;
    }
}

