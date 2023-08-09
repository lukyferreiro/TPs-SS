## TP1 - Cell Index Method

En este TP se implementa el algoritmo Cell Index Method con visualizacion de los vecinos.

## Generacion de particulas

Se debe ejecutar el main del archivo Generator.java. Su ejecución tomará los datos de configGenerator.json

Tras esto se generaran dos archivos:
- static.txt:
    N         (Heading con el Nro. total de Partículas) 
    L         (Longitud del lado del área de simulación)  
    r1  pr1   (radio y propiedad de la partícula 1)
    r2  pr2   (radio y propiedad de la partícula 2)
    ....
    rN  prN   (radio y propiedad de la partícula N)
- dynamic.txt:
    t0          (tiempo)
    x1  y1      (partícula 1)
    ....       
    xN  yN      (partícula N)


## Calcular los vecinos

Tras haber generado el archivo estatico y dinamico con la informacion de las particulas, se debe ejecutar el main del
archivo CalculateNeighbours.java. Su ejecución tomará los datos de configMethod.json

Tras esto se generaran dos archivos:
- time.txt:
    Total time - 00:00:00.006
- neighbours
    id1 v1 v2 ... vQ
    id2 v1 v2 ... vM
    ...
    idN v1 v2 ... vS

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
 