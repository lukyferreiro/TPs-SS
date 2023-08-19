# TP1 - Cell Index Method

En este TP se implementa el algoritmo Cell Index Method con visualización de los vecinos.

## Generación de partículas

Se debe ejecutar el main del archivo Generator.java. Su ejecución tomará los datos de configGenerator.json

Tras esto se generarán dos archivos:
- static.txt: con el valor de N, L y el radio de cada partícula y su propiedad.
- dynamic.txt: con las posiciones X e Y de las N partículas en el tiempo 0

## Cálculo de los vecinos

Tras haber generado el archivo estático y dinámico con la información de las partículas, se debe ejecutar el main del
archivo CalculateNeighbours.java. Su ejecución tomará los datos de configMethod.json

Tras esto se generarán dos archivos:
- time.txt: con el tiempo total que tardo el método en calcular los vecinos.
- neighbours: con una lista de los vecinos para todas las partículas

## Benchmark

También se puede correr el archivo Benchmark.java, utilizado para crear varios archivos con información necesaria
para realizar ciertos análisis variando M y comparando ambos métodos implementados.

## Configuración

#### configGenerator.json 

``` json
{
  "staticFile": "src/main/resources/static.txt",
  "dynamicFile": "src/main/resources/dynamic.txt",
  "N": 50,
  "L": 100.0,
  "max_radius": 1.0,
  "min_radius": 2.0,
  "times": 1
}
```

| Variables    | Descripción                                                           |
|--------------|-----------------------------------------------------------------------|
| staticFile   | Path de donde se creará el archivo estático                           | 
| dynamicFile  | Path de donde se creará el archivo dinámico                           | 
| N            | Numero natural que representa la cantidad de partículas a generar     | 
| L            | Numero real positivo que representa el lado del area de simulación    | 
| max_radius   | Numero real positivo que representa el radio máximo de las partículas | 
| min_radius   | Numero real positivo que representa el radio máximo de las partículas | 
| times        | Numero natural que representa la cantidad de tiempos                  |   
 
#### configMethod.json 

``` json
{
  "staticFile": "src/main/resources/static.txt",
  "dynamicFile": "src/main/resources/dynamic.txt",
  "outNeighborsFile": "src/main/resources/neighbours.txt",
  "outTimeFile": "src/main/resources/time.txt",
  "method": "CIM",
  "isPeriodic": true,
  "Rc": 15.0,
  "M": 5,
  "particleId": 45
}
```

| Variables         | Descripción                                                                   |
|-------------------|-------------------------------------------------------------------------------|
| staticFile        | Path al archivo estático para tomar su información                            | 
| dynamicFile       | Path al archivo dinámico para tomar su información                            | 
| outNeighborsFile  | Path de donde se creará el archivo con los vecinos de las partículas          | 
| outTimeFile       | Path de donde se creará el archivo con el tiempo                              | 
| method            | Puede tomar dos valores: CIM o BRUTE (representa el método a utilizar)        | 
| isPeriodic        | Booleano que representa si la simulación tiene en cuenta contornos periódicos | 
| Rc                | Numero real positivo que representa el radio de interacción de las partículas | 
| M                 | Numero natural que representa la cantidad de celdas (MxM)                     | 
| particleId        | Numero natural que representa el ID de una partícula para visualizar          |   
 