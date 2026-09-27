# RentaMovil

Aplicacion de consola para administrar una flota de automoviles, motocicletas y camionetas de carga. El proyecto demuestra herencia, polimorfismo, encapsulamiento y separacion de responsabilidades en Java.

## Requisitos

- JDK 17 o posterior.
- No utiliza librerias externas, base de datos ni archivos de datos.

## Estructura

- `src/rentamovil/`: codigo fuente Java.
- `bin/`: archivos compilados; se genera localmente y no se versiona.
- `docs/Informe_RentaMovil.pdf`: analisis, UML y evidencia de pruebas.
- `docs/Informe_RentaMovil.html`: fuente editable del informe.

## Compilar

Desde la raiz del proyecto, en PowerShell:

```powershell
New-Item -ItemType Directory -Force bin
javac -encoding UTF-8 -d bin src/rentamovil/*.java
```

## Ejecutar la aplicacion

```powershell
java -cp bin rentamovil.Main
```

El programa comienza con dos vehiculos de cada categoria, todos disponibles y con ingresos en Q0.00.

## Ejecutar las pruebas

```powershell
java -cp bin rentamovil.PruebasRentaMovil
```

El proceso finaliza normalmente cuando todas las pruebas son exitosas y muestra el total de pruebas aprobadas y fallidas.

## Funciones principales

1. Registrar vehiculos con placas unicas.
2. Consultar la flota y su disponibilidad.
3. Cotizar sin modificar el estado ni los ingresos.
4. Confirmar o cancelar un alquiler.
5. Registrar devoluciones.
6. Consultar conteos por categoria e ingresos acumulados.

Los montos se muestran con dos decimales y las entradas incorrectas se vuelven a solicitar sin cerrar inesperadamente el programa.
