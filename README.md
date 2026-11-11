# Control de Planta Docente Sénior — Equipo 4

![Java 26](https://img.shields.io/badge/Java-26-orange?logo=openjdk)
![Estado](https://img.shields.io/badge/Estado-Completado-brightgreen)

Proyecto del **2do Examen Parcial de Estructura de Datos** (Equipo 4: *Estadística y Control de Planta Docente Sénior*).
Todas las estructuras están implementadas **a mano con nodos enlazados**: no se usa `java.util.LinkedList`, `Stack` ni `Queue`.

##  Problemática y Objetivo
El presente software tiene como objetivo automatizar el control técnico, operacional e historial de actividades de la planta docente de una universidad. El sistema integra estructuras jerárquicas dinámicas y lineales para resolver problemáticas del mundo real, permitiendo:
*   Gestionar la estructura jerárquica de la **Universidad** dividida en **Departamentos** y estos en **Profesores**.
*   Administrar una **Cola de turnos** para las evaluaciones de desempeño docente, controlando tiempos de espera y atrasos.
*   Mantener un **Historial (Pila)** de los permisos de acceso al laboratorio de informática otorgados a los docentes.
*   Realizar estadísticas demográficas sobre la planta mediante **algoritmos recursivos**.

---
## Estructura del proyecto

```
equipo4-planta-docente/
├── README.md
└── src/
    ├── modelo/
    │   ├── CategoriaDocente.java
    │   ├── GradoCientifico.java
    │   ├── Profesor.java
    │   ├── PermisoLaboratorio.java
    │   ├── TurnoEvaluacion.java
    │   └── ResultadoEvaluacion.java
    ├── estructura/
    │   ├── Node.java
    │   ├── NodoProfesor.java
    │   ├── NodoDepartamento.java
    │   ├── MultilistaUniversidad.java
    │   ├── PilaPermisos.java
    │   └── ColaEvaluacion.java
    └── app/
        └── Main.java
```

## Estructuras implementadas

### A. Multilista (Universidad → Departamentos → Profesores)

```
cabeza -> [Informática] -> [Matemática] -> [Física] -> null
               |               |              |
               v               v              v
           [Ana 52]        [Carlos 30]      null
               |
               v
           [Luis 29] -> [Sofía 35] -> null
```

- `NodoDepartamento`: `nombre`, `primerProfesor`, `next`.
- `NodoProfesor`: `info` (Profesor), `next`.
- `agregarDepartamento(String)` y `agregarProfesor(String, Profesor)`. Los nombres se comparan sin distinguir mayúsculas.

**Método recursivo `contarProfesoresMayores30(String)`** (sin `for` ni `while`):

| Fase | Caso base | Caso recursivo |
|---|---|---|
| Buscar el departamento | No quedan departamentos → no existe. Nombre coincide → encontrado. | Pasar al siguiente departamento. |
| Contar en la sublista | Sublista terminada o departamento vacío → `0`. | `1 si edad > 30, o 0` + conteo del resto. |

Traza para Informática (Ana 52, Luis 29, Sofía 35):

```
contar(Ana)   = 1 + contar(Luis)
contar(Luis)  = 0 + contar(Sofía)
contar(Sofía) = 1 + contar(null)
contar(null)  = 0          <- caso base
Resultado: 1 + 0 + 1 + 0 = 2
```

### B. Pila (LIFO — Permisos de laboratorio)

```
top -> [Permiso 3] -> [Permiso 2] -> [Permiso 1] -> null
```

- `push`, `pop`, `top` e `isEmpty`. `pop` y `top` devuelven `null` si la pila está vacía.
- **`listarUltimosN(int n)`:** recorre desde el tope con un puntero auxiliar y un `while`, sin modificar `top`. Si hay menos de `n`, muestra los que existan. Si está vacía, imprime un mensaje.

### C. Cola (FIFO — Turnos de evaluación)

```
first -> [Turno A] -> [Turno B] -> [Turno C] <- last
(sale)                                 (entra)
```

- `add`, `poll`, `peek` e `isEmpty`. `poll` y `peek` devuelven `null` si la cola está vacía.
- **`evaluarProfesor(int nuevoLimiteDias)`:**
  1. Si la cola está vacía, muestra un mensaje de error.
  2. Extrae el primer turno con `poll`.
  3. Si `fechaSolicitud + limiteDias < hoy`, imprime `¡ALERTA! ... ATRASADA`; si no, `... EVALUADO exitosamente`.
  4. Crea un nuevo turno para el mismo profesor con fecha de hoy y `nuevoLimiteDias`, y lo encola al final.

## Compilación y ejecución

Requisito: **JDK 26** (también compila con JDK 17 o superior).

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.Main
```

En Windows (PowerShell):

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out app.Main
```

Si los acentos se ven mal en la consola, ejecuta con `java -Dstdout.encoding=UTF-8 -cp out app.Main`.

Todas las pruebas están en `Main`: cada caso imprime `[OK]` o `[FALLO]` y al final se muestra el total (`35/35`). Si alguna falla, el programa termina con código de salida 1.

## Casos de prueba (ejecutados en `Main`)

| ID | Estructura | Tipo | Escenario | Resultado esperado |
|---|---|---|---|---|
| M-01 | Multilista | Positiva | Agregar departamento y profesor válidos | Se registran y el conteo funciona |
| M-02 | Multilista | Negativa | Departamento duplicado | Lanza excepción |
| M-03 | Multilista | Negativa | Nombre de departamento vacío o nulo | Lanza excepción |
| M-04 | Multilista | Negativa | Profesor en departamento inexistente | Lanza excepción |
| M-05 | Multilista | Negativa | Profesor nulo o con edad fuera de 18–100 | Lanza excepción |
| R-01 | Recursividad | Positiva | Informática con edades 52, 29 y 35 | Devuelve `2` |
| R-02 | Recursividad | Positiva | Profesor con exactamente 30 años | No se cuenta (`0`) |
| R-03 | Recursividad | Negativa | Departamento vacío | Devuelve `0` |
| R-04 | Recursividad | Negativa | Departamento inexistente | Lanza excepción |
| R-05 | Recursividad | Positiva | Búsqueda con otras mayúsculas y espacios | Encuentra el departamento |
| R-06 | Recursividad | Positiva | Sublista de 3000 profesores | Devuelve `3000` sin desbordar la pila |
| P-01 | Pila | Positiva | Pila recién creada | Está vacía |
| P-02 | Pila | Positiva | `top()` repetido | Devuelve el mismo elemento, no elimina |
| P-03 | Pila | Positiva | `listarUltimosN(2)` con 3 permisos | Muestra 2 |
| P-04 | Pila | Positiva | `listarUltimosN(10)` con 3 permisos | Muestra los 3 existentes |
| P-05 | Pila | Positiva | `pop` tras listar | Salen C, B, A (la pila no se destruyó) |
| P-06 | Pila | Negativa | `pop` y `top` en pila vacía | Devuelven `null` |
| P-07 | Pila | Negativa | `listarUltimosN` en pila vacía | Mensaje informativo, devuelve `0` |
| P-08 | Pila | Negativa | `listarUltimosN` con `n <= 0` | Lanza excepción |
| P-09 | Pila | Negativa | `push(null)` | Lanza excepción |
| P-10 | Pila | Negativa | Permiso con salida anterior a la solicitud | Lanza excepción |
| C-01 | Cola | Positiva | Cola recién creada | Está vacía |
| C-02 | Cola | Positiva | Encolar A y B y extraer | Salen A, B (FIFO) |
| C-03 | Cola | Positiva | `peek()` | Devuelve el frente sin eliminarlo |
| C-04 | Cola | Negativa | `poll` y `peek` en cola vacía | Devuelven `null` |
| C-05 | Cola | Positiva | Vaciar la cola y volver a encolar | Funciona (`last` se reinicia) |
| C-06 | Cola | Negativa | `add(null)` | Lanza excepción |
| C-07 | Cola | Negativa | Turno vencido hace 10 días | `ATRASADO` |
| C-08 | Cola | Positiva | Turno vigente | `EVALUADO` |
| C-09 | Cola | Positiva | Turno que vence exactamente hoy | `EVALUADO` (no está atrasado) |
| C-10 | Cola | Positiva | Tras evaluar | El profesor se reencola al final con fecha de hoy y el nuevo límite |
| C-11 | Cola | Negativa | `evaluarProfesor` con cola vacía | `COLA_VACIA` y mensaje de error |
| C-12 | Cola | Negativa | `nuevoLimiteDias < 1` | Lanza excepción y la cola no se altera |
| C-13 | Cola | Positiva | Escenario del examen: un turno vencido y uno de hoy, 4 evaluaciones | `ATRASADO`, `EVALUADO`, `EVALUADO`, `EVALUADO` |
| C-14 | Cola | Positiva | Mismo escenario con `LocalDate.now()` | `ATRASADO` y luego `EVALUADO` |

## Decisiones de diseño

- No se definen excepciones propias: los datos inválidos lanzan `IllegalArgumentException`, y las consultas sobre estructuras vacías devuelven `null`, igual que en las conferencias.
- `poll()` reinicia `last` a `null` al vaciar la cola, para que no quede apuntando a un nodo ya extraído.
- `evaluarProfesor` tiene una sobrecarga que recibe la fecha de "hoy", para que las pruebas no dependan del reloj del sistema. La versión sin parámetro de fecha usa `LocalDate.now()`.
- No hay interfaz gráfica: todo se muestra por consola.

## Declaración de uso de Inteligencia Artificial

Se utilizó una herramienta de IA generativa para estructurar el código fuente del proyecto, documentarlo, idear la lógica de fechas de la cola de evaluaciones (`fechaSolicitud + límite < hoy`) y refactorizar clases y métodos. Todo el código, las pruebas y la documentación fueron revisados, compilados, ejecutados y validados por los integrantes del equipo, que son los responsables del contenido entregado y pueden explicar cada una de sus partes.
