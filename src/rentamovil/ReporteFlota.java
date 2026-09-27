package rentamovil;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Fotografia inmutable del estado general de la flota. */
public final class ReporteFlota {
    private final int totalRegistrados;
    private final int totalDisponibles;
    private final int totalAlquilados;
    private final double ingresosAcumulados;
    private final Map<CategoriaVehiculo, ConteoCategoria> conteos;

    public ReporteFlota(int totalRegistrados, int totalDisponibles, int totalAlquilados,
                        double ingresosAcumulados,
                        Map<CategoriaVehiculo, ConteoCategoria> conteos) {
        this.totalRegistrados = totalRegistrados;
        this.totalDisponibles = totalDisponibles;
        this.totalAlquilados = totalAlquilados;
        this.ingresosAcumulados = ingresosAcumulados;
        this.conteos = Collections.unmodifiableMap(new EnumMap<>(conteos));
    }

    public int getTotalRegistrados() {
        return totalRegistrados;
    }

    public int getTotalDisponibles() {
        return totalDisponibles;
    }

    public int getTotalAlquilados() {
        return totalAlquilados;
    }

    public double getIngresosAcumulados() {
        return ingresosAcumulados;
    }

    public Map<CategoriaVehiculo, ConteoCategoria> getConteos() {
        return conteos;
    }
}

