# TP1 - Cell Index Method

En este TP se implementa el algoritmo Cell Index Method con visualizacion de los vecinos.

## Generacion de particulas

Se debe ejecutar el main del archivo Generator.java. Su ejecución tomará los datos de configGenerator.json

Tras esto se generaran dos archivos:
- static.txt: con el valor de N, L y el radio de cada particula y su propiedad.
- dynamic.txt: con las posiciones X e Y de las N particulas en el tiempo 0

## Calculo de los vecinos

Tras haber generado el archivo estatico y dinamico con la informacion de las particulas, se debe ejecutar el main del
archivo CalculateNeighbours.java. Su ejecución tomará los datos de configMethod.json

Tras esto se generaran dos archivos:
- time.txt: con el tiempo total que tardo el metodo en calcular los vecinos.
- neighbours: con una lista de los vecinos para todas las particulas

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

| Variables    | Descripción                                                          
|--------------|-------------------------------------------------------------------------|
| staticFile   | Path de donde se creará el archivo estatico                             | 
| dynamicFile  | Path de donde se creará el archivo dinamico                             | 
| N            | Numero natural que representa la cantidad de particulas a generar       | 
| L            | Numero real positivo que representa el lado del area de simulacion      | 
| max_radius   | Numero real positivo que representa el radio maximo de las particulas   | 
| min_radius   | Numero real positivo que representa el radio maximo de las particulas   | 
| times        | Numero natural que representa la cantidad de tiempos                    |   
 
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

| Variables         | Descripción                                                          
|-------------------|--------------------------------------------------------------------------------|
| staticFile        | Path al archivo estatico para tomar su información                             | 
| dynamicFile       | Path al archivo dinamico para tomar su información                             | 
| outNeighborsFile  | Path de donde se creará el archivo con los vecinos de las particulas           | 
| outTimeFile       | Path de donde se creará el archivo con el tiempo                               | 
| method            | Puede tomar dos valors: CIM o BRUTE (representa el metodo a utilizar)          | 
| isPeriodic        | Booleano que representa si la simulacion tiene en cuenta contornos periodicos  | 
| Rc                | Numero real positivo que representa el radio de interaccion de las particulas  | 
| M                 | Numero natural que representa la cantidad de celdas (MxM)                      | 
| particleId        | Numero natural que representa el ID de una particula para visualizar           |   
 