import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CarritoComprasIterador {

    // Interfaz Producto: define el "contrato" que debe cumplir cualquier producto.
    // Todo producto debe tener un método getPrecio().
    interface Producto {
        double getPrecio();
    }

    // Clase genérica Carrito.
    // <T extends Producto> significa que T puede ser cualquier tipo,
    // siempre y cuando implemente la interfaz Producto.
    // Así el carrito sirve para libros, camisetas, zapatos, etc.
    // implements Iterable<T> significa "este carrito se puede recorrer con for-each".
    // Por eso está obligado a tener el método iterator().
    static class Carrito<T extends Producto> implements Iterable<T> {

        // Lista que guarda los productos del carrito.
        // Solo acepta objetos del tipo T.
        private ArrayList<T> lista = new ArrayList<>();

        // Requisito 1: agregar un producto al carrito.
        // p es la variable que representa el producto que entra.
        public void agregar(T p) {
            lista.add(p);
        }

        // Requisito 2: obtener el producto de mayor precio.
        public T mayorPrecio() {
            // Si el carrito está vacío no hay mayor, se devuelve null
            // (así se evita el error de lista.get(0) con la lista vacía).
            if (lista.isEmpty()) return null;

            // Se supone que el primer producto es el de mayor precio.
            T mayor = lista.get(0);

            // Se recorre el carrito comparando cada producto con el mayor actual.
            // for-each se lee: para cada elemento de tipo T, al que llamo p,
            // que está dentro de this (el carrito).
            // "this" es el carrito mismo, así que se usa NUESTRO iterador.
            for (T p : this) {
                // Si el precio de p es mayor, p pasa a ser el nuevo mayor.
                if (p.getPrecio() > mayor.getPrecio()) {
                    mayor = p;
                }
            }
            // Se devuelve el producto con el precio más alto.
            return mayor;
        }

        // Requisito 3: calcular el precio total del carrito.
        public double total() {
            // Acumulador que empieza en 0.
            double suma = 0;

            // Se suma el precio de cada producto del carrito.
            // Igual que arriba: para cada p dentro de this (el carrito).
            for (T p : this) {
                suma += p.getPrecio();
            }
            // Se devuelve la suma de todos los precios.
            return suma;
        }

        // Método exigido por Iterable.
        // Java lo llama automáticamente cuando se usa un for-each sobre el carrito.
        // Devuelve un objeto nuevo de NUESTRO iterador, que empieza en la posición 0.
        @Override
        public Iterator<T> iterator() {
            return new IteradorCarrito();
        }

        // ITERADOR PROPIO: clase interna que sabe cómo recorrer la lista.
        // Implementa Iterator<T>, por eso debe tener hasNext() y next().
        // No es static porque necesita usar la lista y el tipo T del carrito.
        // Es private porque solo el carrito necesita crearla.
        private class IteradorCarrito implements Iterator<T> {

            // Posición del siguiente producto que se va a entregar.
            // Empieza en 0, o sea, en el primer producto.
            private int posicion = 0;

            // ¿Quedan productos por recorrer?
            // Responde true mientras la posición sea menor que el tamaño de la lista.
            @Override
            public boolean hasNext() {
                return posicion < lista.size();
            }

            // Entrega el producto actual y avanza una posición.
            @Override
            public T next() {
                // Si ya no quedan productos, se lanza un error.
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                // Se toma el producto de la posición actual.
                T producto = lista.get(posicion);
                // Se avanza a la siguiente posición para la próxima vez.
                posicion++;
                // Se entrega el producto.
                return producto;
            }
        }
    }

    // Clase Libro: un producto de ejemplo.
    // Implementa Producto, por eso está obligada a tener getPrecio().
    static class Libro implements Producto {
        // Atributos del libro: título (texto) y precio (número con decimales).
        private String titulo;
        private double precio;

        // Constructor: recibe el título y el precio del libro.
        public Libro(String titulo, double precio) {
            this.titulo = titulo;
            this.precio = precio;
        }

        // Implementación del método de la interfaz Producto.
        public double getPrecio() {
            return precio;
        }

        // Define cómo se imprime el libro (título y precio).
        // Java lo llama automáticamente al hacer System.out.println(libro).
        public String toString() {
            return titulo + " $" + precio;
        }
    }

    // Método principal: aquí se prueba el carrito.
    public static void main(String[] args) {
        // Se crea un carrito de Libros (T se reemplaza por Libro).
        Carrito<Libro> c = new Carrito<>();

        // Se agregan dos libros al carrito.
        c.agregar(new Libro("Java", 50000));
        c.agregar(new Libro("Estructuras", 80000));

        // Como el carrito es Iterable, se puede recorrer directamente con for-each.
        // En cada vuelta, l es un libro del carrito.
        for (Libro l : c) {
            System.out.println(l);
        }

        // Se imprime el libro más caro y el total del carrito.
        System.out.println("Mayor: " + c.mayorPrecio());
        System.out.println("Total: " + c.total());
    }
}