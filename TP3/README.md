# TP3- Dinámica Molecular Dirigida por Eventos

En este TP se implementa el algoritmo de gas diffusion para la visualizacion de particulas en un recinto
## Generación de partículas

Se debe ejecutar el main del archivo Generator.java. Su ejecución tomará los datos de configGenerator.json y creará
los archivos static.txt y dynamic.txt.

## Simulacion Off-Lattice

Se debe ejecutar el main del archivo Simulation.java. Su ejecución tomará los datos de los archivos static.txt y dynamic.txt
y creará dos archivos: off_lattice.txt (con la informacion de las particulas en cada tiempo) y time.txt (con el tiempo tardado
en toda la ejecucion)

## Configuración

#### configGenerator.json 

``` json
{
  "staticFile": "src/main/resources/static.txt",
  "dynamicFile": "src/main/resources/dynamic.txt",
  "N": 400,
  "side": 0.09,
  "max_radius": 0.0015,
  "min_radius": 0.0015,
  "times": 1,
  "mass": 1.0,
  "speed": 0.01
}
```

| Variables    | Descripción                                                           |
|--------------|-----------------------------------------------------------------------|
| staticFile   | Path de donde se creará el archivo estático                           | 
| dynamicFile  | Path de donde se creará el archivo dinámico                           | 
| N            | Numero natural que representa la cantidad de partículas a generar     | 
| side         | Numero real positivo que representa el lado del area de simulación    | 
| max_radius   | Numero real positivo que representa el radio máximo de las partículas | 
| min_radius   | Numero real positivo que representa el radio máximo de las partículas | 
| times        | Numero natural que representa la cantidad de tiempos                  |
| mass         | Numero real que representa la masa de la particula                    |   
| speed        | Numero real que representa la velocidad de las particulas             |      
 
#### configMethod.json 

``` json
{
  "staticFile": "src/main/resources/static.txt",
  "dynamicFile": "src/main/resources/dynamic.txt",
  "outFile": "src/main/resources/gas_diffusion.txt",
  "outTimeFile": "src/main/resources/time.txt",
  "L": 0.09
}
```

| Variables                 | Descripción                                                                          |
|---------------------------|--------------------------------------------------------------------------------------|
| staticFile                | Path al archivo estático para tomar su información                                   | 
| dynamicFile               | Path al archivo dinámico para tomar su información                                   | 
| outFile                   | Path de donde se creará el archivo con las posiciones de las particulas              | 
| outTimeFile               | Path de donde se creará el archivo con el tiempo                                     | 
| L                         | Numero real positivo que representa el alto del 2do recinto                          | 
 