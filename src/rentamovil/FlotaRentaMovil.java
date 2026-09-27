package rentamovil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Coordina registros, cotizaciones, alquileres, devoluciones y reportes. */
public final class FlotaRentaMovil {
    private final Map<String, Vehiculo> vehiculos = new LinkedHashMap<>();
    private double ingresosAcumulados;

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo es obligatorio.");
        }
        if (vehiculos.containsKey(vehiculo.getPlaca())) {
            throw new IllegalArgumentException("Ya existe un vehiculo con la placa " + vehiculo.getPlaca() + ".");
        }
        vehiculos.put(vehiculo.getPlaca(), vehiculo);
    }

    public Vehiculo buscarVehiculo(String placa) {
        String placaNormalizada = Vehiculo.normalizarPlaca(placa);
        Vehiculo vehiculo = vehiculos.get(placaNormalizada);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehiculo con la placa " + placaNormalizada + ".");
        }
        return vehiculo;
    }

    public List<Vehiculo> consultarFlota() {
        return Collections.unmodifiableList(new ArrayList<>(vehiculos.values()));
    }

    public Cotizacion cotizar(String placa, int dias) {
        Vehiculo.validarDias(dias);
        Vehiculo vehiculo = buscarVehiculo(placa);
        return new Cotizacion(vehiculo, dias, vehiculo.calcularCosto(dias));
    }

    public Cotizacion confirmarAlquiler(String placa, int dias) {
        Vehiculo.validarDias(dias);
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (!vehiculo.isDisponible()) {
            throw new IllegalStateException("El vehiculo con placa " + vehiculo.getPlaca() + " esta ocupado.");
        }

        double total = vehiculo.calcularCosto(dias);
        vehiculo.alquilar();
        ingresosAcumulados += total;
        return new Cotizacion(vehiculo, dias, total);
    }

    public void registrarDevolucion(String placa) {
        buscarVehiculo(placa).devolver();
    }

    public ReporteFlota generarReporte() {
        Map<CategoriaVehiculo, int[]> acumuladores = new EnumMap<>(CategoriaVehiculo.class);
        for (CategoriaVehiculo categoria : CategoriaVehiculo.values()) {
            acumuladores.put(categoria, new int[3]);
        }

        int disponibles = 0;
        for (Vehiculo vehiculo : vehiculos.values()) {
            int[] datos = acumuladores.get(vehiculo.getCategoria());
            datos[0]++;
            if (vehiculo.isDisponible()) {
                datos[1]++;
                disponibles++;
            } else {
                datos[2]++;
            }
        }

        Map<CategoriaVehiculo, ConteoCategoria> conteos = new EnumMap<>(CategoriaVehiculo.class);
        for (CategoriaVehiculo categoria : CategoriaVehiculo.values()) {
            int[] datos = acumuladores.get(categoria);
            conteos.put(categoria, new ConteoCategoria(datos[0], datos[1], datos[2]));
        }

        return new ReporteFlota(
                vehiculos.size(), disponibles, vehiculos.size() - disponibles,
                ingresosAcumulados, conteos);
    }

    public double getIngresosAcumulados() {
        return ingresosAcumulados;
    }
}
